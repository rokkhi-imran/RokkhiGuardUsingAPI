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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.StringAdapter;
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


public class ParcelActivity extends AppCompatActivity {

    CircleImageView parcelphoto;
    EditText cname, gphone, ptype, flat;
    Button done;
    Map<String, Object> doc;
    String mFileUri = "";
    Context context;
    FirebaseFirestore firebaseFirestore;
    ArrayList<Guards> guards;
    ArrayList<ActiveFlats> allflats;
    ArrayList<String> types;
    private Bitmap bitmap = null;
    FirebaseUser firebaseUser;
    private static final String TAG = "ParcelActivity";

    private long mLastClickTime = 0;
    Timestamp out = new Timestamp(0, 0);

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    ProgressBar progressBar;
    StorageReference photoRef;
    String parcelid;
    String typeselected;
    ActiveFlats flatselected;
    Guards guardselected;
    Calendar myCalendar;
    Normalfunc normalfunc;
    String flatid = "", buildid = "", commid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = ParcelActivity.this;
        normalfunc= new Normalfunc();

        done = findViewById(R.id.done);
        cname = findViewById(R.id.cname);
        gphone = findViewById(R.id.gphone);
        flat = findViewById(R.id.flat);
        ptype = findViewById(R.id.type);
        parcelphoto = findViewById(R.id.parcel_photo);
        progressBar = findViewById(R.id.progressBar1);
        myCalendar = Calendar.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");

        firebaseFirestore.collection("ptype").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


    public void addallguards() {
        guards = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore
                .collection(getString(R.string.col_guards)).whereEqualTo("build_id",buildid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    guards.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Guards guard = documentSnapshot.toObject(Guards.class);
                        guards.add(guard);
                    }
                    progressBar.setVisibility(View.GONE);

                    final GuardAdapter valueAdapter = new GuardAdapter(guards, context);
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
                            gphone.setText(editText.getText().toString());
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
                            guardselected = (Guards) lv.getItemAtPosition(position);
                            //cname.setText(myoffice.getName());
                            gphone.setText(guardselected.getG_name());
                            alertcompany.dismiss();
                        }
                    });
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
                ptype.setText(editText.getText().toString());
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
                ptype.setText(typeselected);
                alertcompany.dismiss();
            }
        });

    }

    public void initonclick() {


        flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addallflats();

            }
        });

        gphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addallguards();
            }
        });

        ptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addalltypes();
            }
        });

        parcelphoto.setOnClickListener(new View.OnClickListener() {
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
                            //progressBar.setVisibility(View.VISIBLE);
                            parcelphoto.setImageURI(null);

                            mFileUri = r.getUri().toString();
                            bitmap = r.getBitmap();
                            parcelphoto.setImageBitmap(r.getBitmap());


                        } else {
                            Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }).show(ParcelActivity.this);
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


                gphone.setError(null);
                flat.setError(null);
                ptype.setError(null);

                // Store values at the time of the login attempt.
                final String flatname = flat.getText().toString();
                final String phoneno = gphone.getText().toString();
                final String type = ptype.getText().toString();


                boolean cancel = false;
                View focusView = null;


                if (TextUtils.isEmpty(flatname)) {
                    flat.setError(getString(R.string.error_field_required));
                    focusView = flat;
                    cancel = true;
                }

                if (TextUtils.isEmpty(phoneno)) {
                    gphone.setError(getString(R.string.error_field_required));
                    focusView = gphone;
                    cancel = true;
                }
                if (TextUtils.isEmpty(type)) {
                    ptype.setError(getString(R.string.error_field_required));
                    focusView = ptype;
                    cancel = true;
                }


                if (cancel) {
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

    public void addallflats() {
        allflats = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection(getString(R.string.col_activeflat))
                .whereEqualTo("build_id",buildid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    allflats.clear();
                    Log.d(TAG, "onComplete: ");
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        ActiveFlats activeFlat = documentSnapshot.toObject(ActiveFlats.class);
                        allflats.add(activeFlat);
                    }
                    progressBar.setVisibility(View.GONE);

                    final ActiveFlatAdapter valueAdapter = new ActiveFlatAdapter(allflats, context);
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
                            flat.setText(editText.getText().toString());
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
                            flatselected = (ActiveFlats) lv.getItemAtPosition(position);
                            //cname.setText(myoffice.getName());
                            flat.setText(flatselected.getF_no());
                            alertcompany.dismiss();

                        }
                    });
                }
            }
        });
    }


//    private String build_id;
//    private String comm_id;
//    private String flat_id;
//    private String family_id;
//    private String p_com;
//    private String g_uid;
//    private Date p_rtime;
//    private String p_type;
//    private String p_pic;
//    private String p_thumb;
//    private String p_uid; //auto
//    private List<String> p_array;

    public void upload() {

        Log.d(TAG, "upload: yyyy");

        doc = new HashMap<>();
        doc.put("p_rtime", FieldValue.serverTimestamp());
        doc.put("p_com", cname.getText().toString());
        doc.put("build_id", buildid);
        doc.put("comm_id", commid);
        if (guardselected != null) {
            doc.put("g_uid", guardselected.getG_uid());
        }
        doc.put("p_type", typeselected);
        doc.put("flat_id", flatselected.getFlat_id());
        doc.put("p_pic", "");
        doc.put("p_thumb", "");

        List<String>ll= normalfunc.splitstring(cname.getText().toString());
        ll.add(flatselected.getF_no());
        ll.addAll(normalfunc.splitchar(guardselected.getG_phone()));

        doc.put("p_array",ll);


        // [END get_child_ref]

        parcelid = firebaseFirestore
                .collection(getString(R.string.col_parcels)).document().getId();

        doc.put("p_uid", parcelid);

        photoRef = FirebaseStorage.getInstance().getReference()
                .child("parcels/" + parcelid + "/pic");


        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

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
                                    doc.put("p_pic", uri.toString());
                                    doc.put("p_thumb", uri.toString());
                                    // Log.d(TAG, "onSuccess: yyyy");
                                    WriteBatch batch = firebaseFirestore.batch();

                                    DocumentReference off = firebaseFirestore
                                            .collection(getString(R.string.col_parcels)).
                                                    document(parcelid);
                                    batch.set(off, doc);

                                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
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

            WriteBatch batch = firebaseFirestore.batch();

            DocumentReference off = firebaseFirestore
                    .collection(getString(R.string.col_parcels)).
                            document(parcelid);
            batch.set(off, doc);

            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
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
        Intent intent= new Intent(ParcelActivity.this,MainPage.class);
        startActivity(intent);
        finish();
    }

}
