package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.SLastHistory;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.StringAdapter;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateProfile extends AppCompatActivity implements ActiveFlatAdapter.MyInterface, IPickResult {

    CircleImageView userphoto;
    ArrayList<String> types;
    ArrayList<ActiveFlats> activeFlats;
    EditText username, phone, type, flats;
    Button done;
    Map<String, Object> doc, shistory;
    String mFileUri = "";
    Context context;
    FirebaseFirestore firebaseFirestore;
    private Bitmap bitmap = null;
    FirebaseUser firebaseUser;
    private static final String TAG = "CreateProfile";
    private long mLastClickTime = 0;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    ProgressBar progressBar;
    StorageReference photoRef;
    Calendar myCalendar;
    String typeselected;
    ActiveFlats flatselected;
    AlertDialog alertDialog;
    Normalfunc normalfunc;
    List<ActiveFlats> historyFlats;
    String totaltext = "";
    boolean flag;
    List<Swroker> list;
    String buildid;

    int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        normalfunc = new Normalfunc();
        flag = false;

        context = CreateProfile.this;

        done = findViewById(R.id.done);
        username = findViewById(R.id.user_name);
        phone = findViewById(R.id.user_mail);
        //changepic = findViewById(R.id.changeProfilePhoto);
        userphoto = findViewById(R.id.user_photo);
        progressBar = findViewById(R.id.progressBar1);
        myCalendar = Calendar.getInstance();
        type = findViewById(R.id.user_wtype);
        flats = findViewById(R.id.user_flat);
        historyFlats = new ArrayList<>();


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        initonclick();

        firebaseFirestore.collection("stype").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                types = new ArrayList<>();
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        String name = documentSnapshot.getId();
                        types.add(name);
                    }
                }
            }
        });

        firebaseFirestore.collection(getString(R.string.col_activeflat)).orderBy("f_no", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                activeFlats = new ArrayList<>();
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        ActiveFlats activeFlat = documentSnapshot.toObject(ActiveFlats.class);
                        activeFlats.add(activeFlat);
                    }
                }
            }
        });

    }

    Dialog mdialog;

    public void initdialog() {
        mdialog = new Dialog(this);

        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void showdialog() {
        mdialog.show();
    }

    public void dismissdialog() {
        mdialog.dismiss();
    }

    public void addallflats() {

        final ActiveFlatAdapter activeFlatAdapter = new ActiveFlatAdapter(activeFlats, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_list, null);
        final EditText editText = convertView.findViewById(R.id.sear);
        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.done);
        final TextView tt = convertView.findViewById(R.id.selected);
        tt.setVisibility(View.VISIBLE);
        totaltext = "";


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flats.setText(totaltext);
                alertcompany.dismiss();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                activeFlatAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                typeselected = (String) lv.getItemAtPosition(position);
//                //cname.setText(myoffice.getName());
//                type.setText(typeselected);
//                alertcompany.dismiss();

                ActiveFlats ss = (ActiveFlats) lv.getItemAtPosition(position);


                //selected na hoile selected er moto kaj korbe.. selection er subidhar jnno
                if (!historyFlats.contains(ss)) {

                    view.setBackground(ContextCompat.getDrawable(context, R.color.orange_light));
                    activeFlatAdapter.changedata(ss.getF_no(), true);
                    historyFlats.add(ss);
                    totaltext = totaltext + "  " + ss.getF_no();
                    tt.setText(totaltext);
                    //activeFlatAdapter.notifyDataSetChanged();

                } else {
                    view.setBackground(ContextCompat.getDrawable(context, R.color.white));
                    activeFlatAdapter.changedata(ss.getF_no(), false);
                    historyFlats.remove(ss);
                    totaltext = totaltext.replace("  " + ss.getF_no(), "");
                    tt.setText(totaltext);
                    // activeFlatAdapter.notifyDataSetChanged();
                }


            }
        });

    }

    public void addalltypes() {

        final StringAdapter valueAdapter = new StringAdapter(types, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_list, null);
        final EditText editText = convertView.findViewById(R.id.sear);
        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.done);
        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(valueAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type.setText(editText.getText().toString());
                alertcompany.dismiss();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                valueAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeselected = (String) lv.getItemAtPosition(position);
                //cname.setText(myoffice.getName());
                type.setText(typeselected);
                alertcompany.dismiss();
            }
        });
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

                    firebaseFirestore.collection(getString(R.string.col_sworker)).whereEqualTo("s_phone",pp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            list = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Swroker swroker = document.toObject(Swroker.class);
                                list.add(swroker);
                            }

                            if(list.size()>0){
                                alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setCancelable(false);
                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.item_person, null);
                                TextView name= convertView.findViewById(R.id.name);
                                TextView gatepass = convertView.findViewById(R.id.pass);
                                CircleImageView pic= convertView.findViewById(R.id.propic);
                                Button cancel= convertView.findViewById(R.id.cancel);
                                TextView cc= convertView.findViewById(R.id.cc);
                                cc.setVisibility(View.GONE);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });

                                name.setText(list.get(0).getS_name());
                                gatepass.setText("Gatepass: "+ list.get(0).getS_pass());
                                UniversalImageLoader.setImage(list.get(0).getThumb_s_pic(), pic, null, "");

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

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addalltypes();
            }
        });
        flats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addallflats();
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
                        .show(CreateProfile.this);
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


                username.setError(null);
                phone.setError(null);
                type.setError(null);

                // Store values at the time of the login attempt.
                final String iname = username.getText().toString();
                final String phoneno = phone.getText().toString();
                final String typetext = type.getText().toString();


                boolean cancel = false;
                View focusView = null;


                if (TextUtils.isEmpty(iname)) {
                    username.setError(getString(R.string.error_field_required));
                    focusView = username;
                    cancel = true;

                }

                if (TextUtils.isEmpty(phoneno)) {
                    phone.setError(getString(R.string.error_field_required));
                    focusView = phone;
                    cancel = true;
                }
                if (TextUtils.isEmpty(typetext)) {
                    type.setError(getString(R.string.error_field_required));
                    focusView = type;
                    cancel = true;
                }

                if (!normalfunc.isvalidphone(phoneno)) {
                    phone.setError("Invalid Phone no");
                    focusView = phone;
                    cancel = true;
                }


                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    if (firebaseUser == null) return;
                    initdialog();
                    showdialog();
                    // progressBar.setVisibility(View.VISIBLE);
                    upload();
                }


            }


        });
    }


    private Date futuredate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, 4000);
        Date date = calendar.getTime();
        return date;
    }


    public void upload() {


        Log.d(TAG, "upload: yyyy");

        List<String> ll = normalfunc.splitstring(username.getText().toString());
        ll.addAll(normalfunc.splitchar(phone.getText().toString().toLowerCase()));
        ll.addAll(normalfunc.splitchar(typeselected.toLowerCase()));
//        ll.add(mail.getText().toString().toLowerCase());


        final String s_id = firebaseFirestore.collection(getString(R.string.col_sworker)).document().getId();

        doc = new HashMap<>();
        doc.put("s_name", username.getText().toString());
        doc.put("s_id", s_id);
        doc.put("s_phone", phone.getText().toString());
        doc.put("s_mail", "");
        doc.put("s_pic", "");
        doc.put("thumb_s_pic", "");
        doc.put("s_bday", futuredate());
        doc.put("experience", futuredate());
        doc.put("starttime", 0);
        doc.put("endtime", 0);
        doc.put("nid", "");
        doc.put("type", typeselected);
        doc.put("who_add", firebaseUser.getUid());
        doc.put("when_add", FieldValue.serverTimestamp());
        doc.put("s_pass", normalfunc.getPassForGuards5(phone.getText().toString()));
        doc.put("address", new ArrayList<>());
        doc.put("s_array", ll);


        photoRef = FirebaseStorage.getInstance().getReference()
                .child("sworkers/" + s_id + "/s_pic");


        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        if (bitmap != null) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] data = baos.toByteArray();

            UploadTask uploadTask = photoRef.putBytes(data);
            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded
                            Log.d(TAG, "uploadFromUri:onSuccess yyyy");

                            // Get the public download URL
                            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    doc.put("s_pic", uri.toString());
                                    doc.put("thumb_s_pic", uri.toString());

                                    WriteBatch batch = firebaseFirestore.batch();


                                    //office update

                                    DocumentReference setsworker = firebaseFirestore.collection(getString(R.string.col_sworker))
                                            .document(s_id);

                                    batch.set(setsworker, doc);
                                    ArrayList<String>stringid= new ArrayList<>();
                                    ArrayList<String>stringno= new ArrayList<>();

                                    for (int i = 0; i < historyFlats.size(); i++) {
                                        stringid.add(historyFlats.get(i).getFlat_id());
                                        stringno.add(historyFlats.get(i).getF_no());
                                    }

                                    SLastHistory sLastHistory = new SLastHistory(s_id,buildid ,stringid,stringno,Calendar.getInstance().getTime());
                                    DocumentReference setflat = firebaseFirestore.collection(getString(R.string.col_sworker))
                                            .document(s_id).collection("shistory").document(buildid);

                                    batch.set(setflat, sLastHistory);


                                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "onComplete: yyyy");
                                                //progressBar.setVisibility(View.GONE);
                                                dismissdialog();
                                                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                                                username.setText("");
                                                phone.setText("");
                                                flats.setText("");
                                                type.setText("");
                                                UniversalImageLoader.setImage("", userphoto, null, "");
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
            WriteBatch batch = firebaseFirestore.batch();


            DocumentReference setsworker = firebaseFirestore.collection(getString(R.string.col_sworker))
                    .document(s_id);

            batch.set(setsworker, doc);

            ArrayList<String>stringid= new ArrayList<>();
            ArrayList<String>stringno= new ArrayList<>();

            for (int i = 0; i < historyFlats.size(); i++) {
                stringid.add(historyFlats.get(i).getFlat_id());
                stringno.add(historyFlats.get(i).getF_no());
            }

            SLastHistory sLastHistory = new SLastHistory(s_id,buildid ,stringid,stringno,Calendar.getInstance().getTime());
            DocumentReference setflat = firebaseFirestore.collection(getString(R.string.col_sworker))
                    .document(s_id).collection("shistory").document(buildid);

            batch.set(setflat, sLastHistory);


            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: yyyy");
                        //progressBar.setVisibility(View.GONE);
                        dismissdialog();
                        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                        username.setText("");
                        phone.setText("");
                        flats.setText("");
                        type.setText("");
                        UniversalImageLoader.setImage("", userphoto, null, "");
                    }
                }
            });
        }
    }

    @Override
    public int foo() {
        return mPosition;
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
    }
}
