package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateProfileforGuards extends AppCompatActivity implements IPickResult {

    CircleImageView userphoto;
    EditText guardname, phone;
    Button done;

    String mFileUri = "";
    Context context;
    FirebaseFirestore firebaseFirestore;
    private Bitmap bitmap = null;
    FirebaseUser firebaseUser;
    private static final String TAG = "CreateProfileforGuards";

    private long mLastClickTime = 0;
    Timestamp out = new Timestamp(0, 0);

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    ProgressBar progressBar;
    StorageReference photoRef;
    String id;
    Calendar myCalendar;
    Normalfunc normalfunc;
    String flatid = "", buildid = "", commid = "";
    private boolean flag;
    AlertDialog alertDialog;
    List<Guards> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_forguard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        normalfunc= new Normalfunc();
        flag=false;


        context = CreateProfileforGuards.this;

        done = findViewById(R.id.done);
        guardname = findViewById(R.id.user_name);
        phone = findViewById(R.id.user_mail);
        userphoto = findViewById(R.id.user_photo);
        progressBar = findViewById(R.id.progressBar1);
        myCalendar = Calendar.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        initonclick();

    }

    Dialog mdialog;

    public void initdialog(){
        mdialog=new Dialog(this);

        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);

    }

    public void showdialog(){
        mdialog.show();
    }
    public void dismissdialog(){
        mdialog.dismiss();
    }

    public void initonclick() {

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11 && !flag) {
                    String pp = s.toString();
                    flag = true;

                    firebaseFirestore.collection(getString(R.string.col_guards)).whereEqualTo("g_phone",pp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            list = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Guards guards = document.toObject(Guards.class);
                                list.add(guards);
                            }

                            if(list.size()>0){
                                alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setCancelable(false);
                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.item_person, null);
                                TextView name= convertView.findViewById(R.id.name);
                                TextView gatepass = convertView.findViewById(R.id.pass);
                                CircleImageView pic= convertView.findViewById(R.id.propic);
                                gatepass.setVisibility(View.GONE);
                                Button cancel= convertView.findViewById(R.id.cancel);
                                TextView cc= convertView.findViewById(R.id.cc);
                                cc.setVisibility(View.GONE);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });

                                name.setText(list.get(0).getG_name());
                                UniversalImageLoader.setImage(list.get(0).getThumb_g_pic(), pic, null, "");

                                alertDialog.setView(convertView);
                                alertDialog.show();
                            }
                        }
                    });

                } else if (s.length() != 11) flag = false;
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup setup = new PickSetup()
                        .setTitle("Choose Photo")
                        .setBackgroundColor(Color.WHITE)
                        .setButtonOrientation(LinearLayout.HORIZONTAL)
                        .setGalleryButtonText("Gallery")
                        .setCameraIcon(R.mipmap.camera_colored)
                        .setGalleryIcon(R.mipmap.gallery_colored)
                        .setCameraToPictures(false)
                        .setWidth(480)
                        .setHeight(640)
                        .setMaxSize(300);

                PickImageDialog.build(setup)
                        //.setOnClick(this)
                        .show(CreateProfileforGuards.this);



            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: yyyy");


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                guardname.setError(null);
                phone.setError(null);

                // Store values at the time of the login attempt.
                final String iname = guardname.getText().toString();
                final String phonenumber = phone.getText().toString();


                boolean cancel = false;
                View focusView = null;


                if (TextUtils.isEmpty(iname)) {
                    guardname.setError(getString(R.string.error_field_required));
                    focusView = guardname;
                    cancel = true;
                }

                if (TextUtils.isEmpty(phonenumber)) {
                    phone.setError(getString(R.string.error_field_required));
                    focusView = phone;
                    cancel = true;
                }

                if (!normalfunc.isvalidphone(phonenumber)) {
                    phone.setError(getString(R.string.error_invalid_phone));
                    focusView = phone;
                    cancel = true;
                }


                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    if (firebaseUser == null) return;
                    //progressBar.setVisibility(View.VISIBLE);
                    initdialog();
                    showdialog();
                    upload();
                }


            }


        });
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.

            userphoto.setImageURI(null);

            mFileUri = r.getUri().toString();
            bitmap = r.getBitmap();
            userphoto.setImageBitmap(r.getBitmap());

            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }

        //scrollToTop();
    }



    public void upload() {

        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "upload: yyyy");

        id= firebaseFirestore.collection(getString(R.string.col_guards))
                .document().getId();

        List<String>ll= normalfunc.splitstring(guardname.getText().toString());
        ll.addAll(normalfunc.splitchar(phone.getText().toString()));
        final String phoneno=  phone.getText().toString();
//        ll.add(phone.getText().toString());
        Date date= new Date();
        Calendar calendar= Calendar.getInstance();
        date= calendar.getTime();

        final Guards guards= new Guards(buildid,commid,guardname.getText().toString()
        ,normalfunc.getRandomNumberString5(),"",date,normalfunc.futuredate(),"","","","",
                phoneno ,id,ll);







        photoRef = FirebaseStorage.getInstance().getReference()
                .child("guards/" + id + "/g_pic");


        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] data = baos.toByteArray();

            UploadTask uploadTask = photoRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded
                            Log.d(TAG, "uploadFromUri:onSuccess yyyy");

                            // Get the public download URL
                            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    guards.setG_pic(uri.toString());
                                    // Log.d(TAG, "onSuccess: yyyy");
                                    firebaseFirestore.collection(getString(R.string.col_guards))
                                .document(id).set(guards).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d(TAG, "onComplete: yyyy");
                                                //progressBar.setVisibility(View.GONE);
                                                dismissdialog();
                                                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                                                gomainpage();
                                            }
                                        }
                                    });

                                }
                            });


                            // [END_EXCLUDE]
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Upload failed
                            Log.w(TAG, "uploadFromUri:onFailure", exception);

                        }
                    });
        } else {

            firebaseFirestore.collection(getString(R.string.col_guards))
                    .document(id).set(guards).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: yyyy");
                        //progressBar.setVisibility(View.GONE);
                        dismissdialog();
                        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                        gomainpage();
                    }
                }
            });
        }
    }

    public void gomainpage(){
        Intent intent= new Intent(CreateProfileforGuards.this,MainPage.class);
        startActivity(intent);
        finish();
    }

}
