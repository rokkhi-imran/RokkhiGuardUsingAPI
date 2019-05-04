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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
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


public class CreateProfileforGuards extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_forguard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        normalfunc= new Normalfunc();


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

        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup setup = new PickSetup().setWidth(100).setHeight(100)
                        .setTitle("Choose Photo")
                        .setBackgroundColor(Color.WHITE)
                        .setButtonOrientation(LinearLayout.HORIZONTAL)
                        .setGalleryButtonText("Gallery")
                        .setCameraIcon(R.mipmap.camera_colored)
                        .setGalleryIcon(R.mipmap.gallery_colored);


                PickImageDialog.build(setup, new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        if (r.getError() == null) {
                            mFileUri = r.getUri().toString();
                            bitmap = r.getBitmap();
                            userphoto.setImageBitmap(r.getBitmap());

                        } else {
                            Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }).show(CreateProfileforGuards.this);
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
        ,normalfunc.getRandomNumberString5(),"",date,normalfunc.futuredate(),"","","",
                phoneno ,id,ll);







        photoRef = FirebaseStorage.getInstance().getReference()
                .child("guards/" + id + "/pic");


        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);

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
                                    guards.setG_thumb(uri.toString());
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
