package com.rokkhi.rokkhiguard;

import android.Manifest;
import android.app.Dialog;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.BuildingChanges;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Model.Vsearch;
import com.rokkhi.rokkhiguard.Model.Whitelist;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;
import com.rokkhi.rokkhiguard.data.FlatsRepository;
import com.rokkhi.rokkhiguard.data.WhiteListRepository;
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

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddVisitor extends AppCompatActivity implements IPickResult{


    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    CircleImageView userphoto;
    EditText username, phone, purpose, idcardno, org, flat, vehicle;
    Button done;
    Map<String, Object> doc;
    String phoneno="";
    String mFileUri = "";
    Context context;
    FirebaseFirestore firebaseFirestore;
    ArrayList<ActiveFlats> allflats;
    private Bitmap bitmap = null;
    FirebaseUser firebaseUser;
    Date low, high;
    private static final String TAG = "AddVisitor";

    private long mLastClickTime = 0;
    Timestamp out = new Timestamp(0, 0);

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    ProgressBar progressBar;
    StorageReference photoRef;
    String visitorid;
    String flatid = "", buildid = "", commid = "", famid = "", userid = "";
    ActiveFlats selected;

    Calendar myCalendar;
    Normalfunc normalfunc;

    private String res = "pending",vtype="visitor";
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    boolean flag;
    ImageView cut;

    ArrayList<String> wflats;
    ArrayList<Whitelist> whitelists;

    String linkFromSearch = "";
    boolean approve;
    String thismobileuid;
    FlatsRepository flatsRepository;
    WhiteListRepository whiteListRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = AddVisitor.this;
        normalfunc = new Normalfunc();
        flag = false;


        done = findViewById(R.id.done);
        username = findViewById(R.id.user_name);
        phone = findViewById(R.id.phone_no);
        org = findViewById(R.id.fromwhere);
        purpose = findViewById(R.id.pupose);
        flat = findViewById(R.id.flat);
        vehicle = findViewById(R.id.vehicle);
        idcardno = findViewById(R.id.card_no);
        //changepic = findViewById(R.id.changeProfilePhoto);
        userphoto = findViewById(R.id.user_photo);
        progressBar = findViewById(R.id.progressBar1);
        myCalendar = Calendar.getInstance();
        cut = findViewById(R.id.cut);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        editor = sharedPref.edit();
        thismobileuid = FirebaseAuth.getInstance().getUid();
        flatsRepository = new FlatsRepository(this);
        whiteListRepository = new WhiteListRepository(this);


        initonclick();
        listener();


    }

    public void getAllWhiteListAndSaveToLocalDatabase(final BuildingChanges buildingChanges) {
        //allWhiteLists = new ArrayList<>();
        //final FlatsRepository flatsRepository = new FlatsRepository(this);


        firebaseFirestore.collection(getString(R.string.col_whitelists))
                .whereEqualTo("build_id", buildid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Whitelist whitelist = documentSnapshot.toObject(Whitelist.class);
                        whiteListRepository.deleteWhiteList(whitelist);
                        whiteListRepository.insert(whitelist);
                    }


                    Map<String, Object> data = new HashMap<>();
                    ArrayList<String> wldata = new ArrayList<>();
                    wldata = buildingChanges.getWhitelists();
                    wldata.add(thismobileuid);
                    data.put("whitelists", wldata);


                    firebaseFirestore.collection("buildingChanges").document(buildid)
                            .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Whitelists data changed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    whiteListRepository.deleteTask(buildid);

                } else {
                    Log.d(TAG, "onComplete: xxx5");
                }
            }
        });

    }



    public void getAllActiveFlatsAndSaveToLocalDatabase(final BuildingChanges buildingChanges) {
        // allActiveFlats = new ArrayList<>();
        // final FlatsRepository flatsRepository = new FlatsRepository(this);

        firebaseFirestore.collection(getString(R.string.col_activeflat))
                .whereEqualTo("build_id", buildid).orderBy("f_no", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: ");
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        ActiveFlats activeFlat = documentSnapshot.toObject(ActiveFlats.class);
                        flatsRepository.deleteActiveFlat(activeFlat);
                        flatsRepository.insertActiveFlat(activeFlat);
                        // allActiveFlats.add(activeFlat);
                    }

                    Map<String, Object> data = new HashMap<>();
                    ArrayList<String> flatdata = new ArrayList<>();
                    flatdata = buildingChanges.getFlats();
                    flatdata.add(thismobileuid);
                    data.put("flats", flatdata);


                    firebaseFirestore.collection("buildingChanges").document(buildid)
                            .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Flat data changed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseFirestore.getInstance().collection("buildingChanges").document(buildid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    BuildingChanges buildingChanges = documentSnapshot.toObject(BuildingChanges.class);
                    ArrayList<String> flats = buildingChanges.getFlats();
                    ArrayList<String> whitelists = buildingChanges.getWhitelists();
                    ArrayList<String> vehicles = buildingChanges.getVehicles();


                    if (!flats.contains(thismobileuid)) {
                        Log.d("firebase", "Getting new Flats data because data is changed or updated");
                        getAllActiveFlatsAndSaveToLocalDatabase(buildingChanges);


                        //TODO alhn ei uid add korte hbe database a

                    } else {
                        Log.d("firebase", " Flats data is not changed or updated");
                    }


                    if (!whitelists.contains(thismobileuid)) {
                        getAllWhiteListAndSaveToLocalDatabase(buildingChanges);
                    }


                }
            }
        });


        whiteListRepository.getAllWhiteList().observe(this, new Observer<List<Whitelist>>() {
            @Override
            public void onChanged(@Nullable List<Whitelist> allWhiteLists) {
                wflats = new ArrayList<>();
                whitelists= new ArrayList<>();
                for (Whitelist whiteList : allWhiteLists) {
                    wflats.add(whiteList.getW_phone()+whiteList.getFlat_id());
                    whitelists.add(whiteList);
                    Log.d("room", "found a new WhiteList   " + whiteList.getF_no() + "  -- > " + whiteList.getFlat_id());
                }
            }
        });

        addallflats();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void cleardata() {
        Intent intent = new Intent(AddVisitor.this, AddVisitor.class);
        startActivity(intent);
        finish();
    }

    public void getdate() {
        Calendar cal = Calendar.getInstance(); //current date and time
        cal.add(Calendar.DAY_OF_MONTH, 0); //add a day
        cal.set(Calendar.HOUR_OF_DAY, 23); //set hour to last hour
        cal.set(Calendar.MINUTE, 59); //set minutes to last minute
        cal.set(Calendar.SECOND, 59); //set seconds to last second
        cal.set(Calendar.MILLISECOND, 999); //set milliseconds to last millisecond

        high = cal.getTime();
        Calendar cal1 = Calendar.getInstance(); //current date and time
        cal1.add(Calendar.DAY_OF_MONTH, 0); //add a day
        cal1.set(Calendar.HOUR_OF_DAY, 0); //set hour to last hour
        cal1.set(Calendar.MINUTE, 0); //set minutes to last minute
        cal1.set(Calendar.SECOND, 0); //set seconds to last second
        cal1.set(Calendar.MILLISECOND, 0); //set milliseconds to last millisecond
        low = cal1.getTime();

    }

    public void listener() {
        getdate();
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11 && !flag) {
                    flag = true;

                    String xx="+88"+ phone.getText().toString();





                    Log.d(TAG, "onTextChanged:  nn1");
                    firebaseFirestore.collection("search")
                            .document(xx).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot != null && documentSnapshot.exists()) {
                                            Log.d(TAG, "onComplete: rrr " + "ashse ");
                                            final Vsearch vsearch = documentSnapshot.toObject(Vsearch.class);
                                            alertDialog = new AlertDialog.Builder(context).create();
                                            alertDialog.setCancelable(false);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View convertView = (View) inflater.inflate(R.layout.item_person, null);
                                            CircleImageView propic = convertView.findViewById(R.id.propic);
                                            TextView name = convertView.findViewById(R.id.name);
                                            Button cancel = convertView.findViewById(R.id.cancel);
                                            TextView pass = convertView.findViewById(R.id.pass);
                                            RelativeLayout relativeLayout = convertView.findViewById(R.id.relativeLayout);
                                            pass.setVisibility(View.GONE);



                                            relativeLayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    username.setText(vsearch.getV_name());
                                                    phone.setText(vsearch.getV_phone());
                                                    org.setText(vsearch.getV_where());
                                                    if (!vsearch.getV_purpose().isEmpty())
                                                        purpose.setText(vsearch.getV_purpose());


                                                    if (!vsearch.getV_thumb().isEmpty()) {
                                                        linkFromSearch = vsearch.getV_thumb();
                                                    }
                                                    username.requestFocus();
                                                    alertDialog.dismiss();
                                                    UniversalImageLoader.setImage(vsearch.getV_thumb(), userphoto, null, "");
                                                }
                                            });

                                            name.setText(vsearch.getV_name());
                                            UniversalImageLoader.setImage(vsearch.getV_thumb(), propic, null, "");

                                            cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.dismiss();
                                                }
                                            });
                                            alertDialog.setView(convertView);
                                            alertDialog.show();

                                        }
                                    }
                                }
                            });
                } else if (s.length() < 11) flag = false;
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }


    public void upload() {

        String wlcheck= phone.getText().toString()+selected.getFlat_id();
        if(wflats.contains(wlcheck)){
            res = "whitelisted";
            vtype="whitelisted";
        }
        else{
            res = "pending"; //TODO this should be pending
            vtype="visitor";

        }

        Log.d(TAG, "upload: yyyy");

        List<String> ll = normalfunc.splitstring(username.getText().toString());
        ll.addAll(normalfunc.splitchar(phone.getText().toString().toLowerCase()));
        ll.add(selected.getF_no());
        // ll.add(selected.getE_login().replace("+88",""));
        if (!org.getText().toString().isEmpty())
            ll.addAll(normalfunc.splitchar(org.getText().toString().toLowerCase()));


        doc = new HashMap<>();
        doc.put("time", FieldValue.serverTimestamp());
        doc.put("another_uid", "");
        doc.put("v_name", username.getText().toString());
        doc.put("v_mail", "");
        doc.put("v_phone", phone.getText().toString());
        doc.put("v_purpose", purpose.getText().toString());
        doc.put("v_where", org.getText().toString());
        doc.put("v_gpass", idcardno.getText().toString());
        doc.put("flat_id", selected.getFlat_id());
        doc.put("f_no", selected.getF_no());
        doc.put("comm_id", selected.getComm_id());
        doc.put("build_id", selected.getBuild_id());
        doc.put("v_vehicleno", vehicle.getText().toString());
        doc.put("v_pic", "");
        doc.put("thumb_v_pic", "");
        doc.put("in", true);
        doc.put("completed", false);
        doc.put("response", res);
        doc.put("v_type", vtype);
        doc.put("v_array", ll);
        doc.put("responder", firebaseUser.getUid());
        
        
        
        if (!linkFromSearch.isEmpty()) {
            doc.put("v_pic", linkFromSearch);
            doc.put("thumb_v_pic", linkFromSearch);
        }


        // [END get_child_ref]

        visitorid = firebaseFirestore
                .collection(getString(R.string.col_visitors)).document().getId();
        doc.put("v_uid", visitorid);

        photoRef = FirebaseStorage.getInstance().getReference()
                .child("visitors/" + visitorid + "/v_pic");




        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
                                    doc.put("v_pic", uri.toString());
                                    doc.put("thumb_v_pic", uri.toString());


                                    firebaseFirestore
                                            .collection(getString(R.string.col_visitors)).document(visitorid)
                                            .set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //progressBar.setVisibility(View.GONE);
                                                dismissdialog();
                                                dialogconfirmation(visitorid);
                                                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
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

            Log.d(TAG, "upload: yyy1  ");

            firebaseFirestore
                    .collection(getString(R.string.col_visitors)).document(visitorid)
                    .set(doc).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: yyy2 ");
                        dialogconfirmation(visitorid);
                        //progressBar.setVisibility(View.GONE);
                        dismissdialog();
                        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d(TAG, "onComplete: yyy3");
                    }
                }
            });
        }
    }

    public void dialogconfirmation(final String uid) {


        if (selected.isVacant() || !selected.isUsing()) approve = false;
        else approve = true;

        firebaseFirestore.collection(getString(R.string.col_eintercom)).document(selected.getFlat_id())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if(documentSnapshot.exists()){
                            phoneno= documentSnapshot.getString("number");
                            Log.d(TAG, "onEvent: oooo "+ phoneno);
                        }
                    }
                });




        

        Log.d(TAG, "dialogconfirmation:  approve " + approve);

        final AlertDialog alertconfirm = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_confrimation, null);
        final TextView status = convertView.findViewById(R.id.status);
        final Button submit = convertView.findViewById(R.id.submit);
        final Button call = convertView.findViewById(R.id.call);
        final CircleImageView enter = convertView.findViewById(R.id.enter);
        final CircleImageView cancel = convertView.findViewById(R.id.cancel);

        final ProgressBar progressBar = convertView.findViewById(R.id.dialogprogress);
        alertconfirm.setView(convertView);
        alertconfirm.setCancelable(false);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection(getString(R.string.col_visitors)).document(uid)
                        .update("response", "accepted"
                        ,"responder",FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            alertconfirm.dismiss();
                            cleardata();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseFirestore.collection(getString(R.string.col_visitors)).document(uid)
                        .update("response", "rejected",
                                "in",false,"responder",
                                FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            alertconfirm.dismiss();
                            cleardata();
                        }
                    }
                });
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                alertconfirm.dismiss();
                cleardata();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCallBtnClick();
            }
        });


        if (approve) {

            if (res.equals("whitelisted")) {
                Log.d(TAG, "dialogconfirmation: xxx");
                enter.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                status.setText("Accepted  ( গৃহীত )");
                status.setTextColor(Color.GREEN);
            }

            new CountDownTimer(60000, 100) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    if (res.equals("pending") ) {
                        progressBar.setVisibility(View.GONE);
                        status.setText("No response ( সাড়া পাওয়া যায়নি )");
                        status.setTextColor(Color.BLUE);
                        call.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }.start();
            firebaseFirestore.collection(getString(R.string.col_visitors)).document(uid)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }

                            if (documentSnapshot != null && documentSnapshot.exists()) {

                                Visitors visitors = documentSnapshot.toObject(Visitors.class);
                                res = visitors.getResponse();
                                if (res.equals("rejected")) {

                                    enter.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);
                                    submit.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    status.setText("Rejected  ( বাতিল )");
                                    status.setTextColor(Color.RED);
                                } else if (res.equals("accepted")) {
                                    enter.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);
                                    submit.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    status.setText("Accepted  ( গৃহীত )");
                                    status.setTextColor(Color.GREEN);
                                }

                            } else {
                                Log.d(TAG, "Current data: null");
                            }
                        }
                    });
        } else {


            progressBar.setVisibility(View.GONE);
            status.setText("No response ( সাড়া পাওয়া যায়নি )");
            status.setTextColor(Color.BLUE);
            call.setVisibility(View.VISIBLE);

        }


        alertconfirm.show();
    }


    public void addallflats() {



        //getting the data from repository example
        //final FlatsRepository flatsRepository = new FlatsRepository(this);
        flatsRepository.getAllActiveFlats().observe(this, new Observer<List<ActiveFlats>>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable List<ActiveFlats> allFlatss) {
                allflats = new ArrayList<>();
                for (ActiveFlats flat : allFlatss) {
                    allflats.add(flat);
                    Log.d("room", "found a new Flat   " + flat.getF_no() + "  -- > " + flat.getFlat_id());
                }
            }
        });


    }


    public void showallflats() {
        Log.d(TAG, "showallflats: kk "+allflats.size());
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
                selected = (ActiveFlats) lv.getItemAtPosition(position);
                //cname.setText(myoffice.getName());


                flat.setText(selected.getF_no());
                alertcompany.dismiss();

            }
        });
    }



    public void initonclick() {


        flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showallflats();


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
                        .setMaxSize(300);

                PickImageDialog.build(setup)
                        //.setOnClick(this)
                        .show(AddVisitor.this);



            }
        });

        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purpose.setText("");
                purpose.requestFocus();
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
                flat.setError(null);

                // Store values at the time of the login attempt.
                final String iname = username.getText().toString();
                final String phoneno = phone.getText().toString();
                final String flattext = flat.getText().toString();


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
                if (TextUtils.isEmpty(flattext)) {
                    flat.setError(getString(R.string.error_field_required));
                    focusView = flat;
                    cancel = true;
                }

                if (!normalfunc.isvalidphone(phoneno)) {
                    phone.setError("Invalid phone no!");
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




    private void onCallBtnClick(){

        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
            requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
        }

        else phoneCall();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;


        switch(requestCode){
            case PERMISSION_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted: " + PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
                    permissionGranted=true;
                } else {
                    Toast.makeText(this, "Permission NOT granted: " + PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
                }


            }
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }



    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if (phoneno.isEmpty())callIntent.setData(Uri.parse("tel:01521110045"));
            else callIntent.setData(Uri.parse("tel:"+ phoneno));
            startActivity(callIntent);
        }else{
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            mFileUri = r.getUri().toString();
            bitmap = r.getBitmap();
            if(bitmap==null) Log.d(TAG, "onPickResult: bitmapnull");
            else Log.d(TAG, "onPickResult:  bitmapnullna");
            userphoto.setImageBitmap(r.getBitmap());

            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
