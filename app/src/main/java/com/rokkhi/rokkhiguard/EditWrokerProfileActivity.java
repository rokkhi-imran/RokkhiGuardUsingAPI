package com.rokkhi.rokkhiguard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.Model.SLastHistory;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Model.Types;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditWrokerProfileActivity extends AppCompatActivity implements View.OnClickListener{


    protected TextView userPhoneTV;
    protected Button generatepin;
    protected EditText userPin;
    protected EditText userName;
    protected EditText userWtype;
    protected EditText userFlat;
    protected Button done;
    CircleImageView userPhoto;
    Intent intent;
    Context context;
    String sID;
    FirebaseFirestore firebaseFirestore;
    StorageReference photoRef;
    Swroker swroker;
    String buildid = "";
    String commid = "";
    String total = "";
    Normalfunc normalfunc;
    String picurl="";
    String mFileUri = "";
    private Bitmap bitmap = null;

    ArrayList<ActiveFlats> allflats;
    ArrayList<String> historyflatno;
    ArrayList<String> historyflatId;
    ArrayList<Types> types;

    Types typeselected;
    String flatName = "";

    private static final int TAKE_PICTURE_CODE = 101;
    private static final int PICK_IMAGE_REQUEST = 10;
    Intent cameraIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_edit_worker_profile);
        initView();
        context = EditWrokerProfileActivity.this;
        intent = getIntent();
        sID = intent.getStringExtra("s_id");
        normalfunc = new Normalfunc();
        Log.e("TAG", "onCreate: " + sID);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");

        loadUserData(sID);
        getFlatList(sID);

        //load Flat
        loadFlat();

        //get all user Type
        getAlluserType();
    }

    private void getAlluserType() {

        firebaseFirestore.collection("stype").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                types = new ArrayList<>();
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Types types1 = documentSnapshot.toObject(Types.class);

                        types.add(types1);
                    }

                    userWtype.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAlltypes();
                        }
                    });
                }
            }
        });
    }

    private void loadFlat() {

        firebaseFirestore.collection(getString(R.string.col_activeflat)).whereEqualTo("build_id", buildid).
                orderBy("f_no", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    allflats.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                        ActiveFlats activeFlat = documentSnapshot.toObject(ActiveFlats.class);
                        allflats.add(activeFlat);
                    }
                }
            }
        });

    }

    public void addallflats() {

        final ActiveFlatAdapter activeFlatAdapter = new ActiveFlatAdapter(allflats, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View convertView = (View) inflater.inflate(R.layout.custom_list_multiple, null);
        final EditText editText = convertView.findViewById(R.id.sear);
        //convert listView to gridView
        final GridView lv = (GridView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.done);
        final Button selectbutton = convertView.findViewById(R.id.select);
        final Button unselectbutton = convertView.findViewById(R.id.deselect);
        final TextView tt = convertView.findViewById(R.id.selected);
        tt.setVisibility(View.VISIBLE);
        tt.setText(flatName);


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

//        if (!flatName.isEmpty()){
//
//            unSelectAllBtn.setVisibility(View.VISIBLE);
//            selectAllBtn.setVisibility(View.GONE);
//        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFlat.setText(flatName);
                alertcompany.dismiss();
            }
        });

        selectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flatName = "";
                historyflatId.clear();
                historyflatno.clear();
                for (int i = 0; i < allflats.size(); i++) {
                    activeFlatAdapter.changedata(allflats.get(i).getF_no(), true);

                    historyflatId.add(allflats.get(i).getFlat_id());
                    historyflatno.add(allflats.get(i).getF_no());

                    flatName = flatName + " " + allflats.get(i).getF_no();
//                    Log.e(TAG, "onClick: "+flatName.length() );
                    tt.setText(flatName);

                    activeFlatAdapter.notifyDataSetChanged();

                    unselectbutton.setVisibility(View.VISIBLE);
                    selectbutton.setVisibility(View.GONE);
                }

            }
        });

        unselectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                flatName = "";
                tt.setText("");
                historyflatId.clear();
                historyflatno.clear();
                for (int i = 0; i < allflats.size(); i++) {
                    activeFlatAdapter.changedata(allflats.get(i).getF_no(), false);
                    activeFlatAdapter.notifyDataSetChanged();
                    unselectbutton.setVisibility(View.GONE);
                    selectbutton.setVisibility(View.VISIBLE);
                }

                /*for(int i=0;i<allflats.size();i++){
//                    view.setBackground(ContextCompat.getDrawable(context, R.color.orange_light));
                    activeFlatAdapter.changedata(allflats.get(i).getF_no(), false);
                    activeFlatAdapter.notifyDataSetChanged();
//                    holder.historyflatno.remove(allflats.get(i));
                    flatName=  flatName .replace(" " + allflats.get(i).getF_no(), "");
                    tt.setText( flatName);
                    unSelectAllBtn.setVisibility(View.GONE);
                    selectAllBtn.setVisibility(View.VISIBLE);
                }*/

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

        for (int i = 0; i < historyflatno.size(); i++) {
            activeFlatAdapter.changedata(historyflatno.get(i), true);
        }
        activeFlatAdapter.notifyDataSetChanged();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ActiveFlats ss = (ActiveFlats) lv.getItemAtPosition(position);


                //selected na hoile selected er moto kaj korbe.. selection er subidhar jnno
                if (!historyflatno.contains(ss.getF_no())) {

//                    Log.d(TAG, "onItemClick: rrr1");
//
//                    view.findViewById(R.id.buildingNameTV).setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_textsize_with_bg));
//                    holder.ename.setTextColor(ContextCompat.getColor(context,R.color.white));
                    activeFlatAdapter.changedata(ss.getF_no(), true);
                    activeFlatAdapter.notifyDataSetChanged();
                    historyflatId.add(ss.getFlat_id());
                    historyflatno.add(ss.getF_no());
//                    historyFlats.add(ss);
                    flatName = flatName + " " + ss.getF_no();
                    tt.setText(flatName);
                    //

                } else {
////                    Log.d(TAG, "onItemClick: rrr2");
//                    view.findViewById(R.id.buildingNameTV).setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_textsize));
//                    holder.ename.setTextColor(ContextCompat.getColor(context,R.color.Black));

                    activeFlatAdapter.changedata(ss.getF_no(), false);
                    activeFlatAdapter.notifyDataSetChanged();
                    historyflatId.remove(ss.getFlat_id());
                    historyflatno.remove(ss.getF_no());

//                    historyFlats.remove(ss);
                    flatName = flatName.replace(" " + ss.getF_no(), "");
                    tt.setText(flatName);
                    // activeFlatAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getFlatList(String sID) {


        firebaseFirestore.collection(this.getString(R.string.col_sworker))
                .document(sID)
                .collection("shistory")
                .document(buildid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    userFlat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addallflats();
                        }
                    });
                    if (documentSnapshot.exists()) {
                        SLastHistory sLastHistory = documentSnapshot.toObject(SLastHistory.class);

                        historyflatno = sLastHistory.getFlatsNo();
                        flatName = "";
                        for (int i = 0; i < historyflatno.size(); i++) {
//                            Log.e("TAG", "onComplete: " + historyflatno.get(i));

                            flatName = flatName + " " + historyflatno.get(i);
                        }
                        userFlat.setText(flatName);


                    }
                }
            }
        });
    }

    private void loadUserData(final String sID) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Executing Action.....");
        progressDialog.show();

        DocumentReference docRef = firebaseFirestore
                .collection("sworkers")
                .document(sID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               swroker =  task.getResult().toObject(Swroker.class);

                userPhoneTV.setText(normalfunc.getNumberWithoutCountryCode(swroker.getS_phone()));
                userPin.setText(swroker.getS_pass());
                userName.setText(swroker.getS_name());
                final String typeID = swroker.getType();

                picurl=  swroker.getS_pic();

                Log.e(TAG, "onSuccess: picurl = "+picurl );
                Log.e(TAG, "onSuccess: bitmap = "+bitmap);

                if(bitmap==null && !swroker.getS_pic().isEmpty() && !swroker.getS_pic().equals("none")){

                    Glide.with(context).load(swroker.getS_pic()).placeholder(R.drawable.male1).into(userPhoto);

                }




                firebaseFirestore.collection("stype")
                        .whereEqualTo("type_id", typeID).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    Types types = documentSnapshot.toObject(Types.class);
                                    userWtype.setText(types.getBangla());
                                }
                            }
                        });


//                userWtype.setText(swroker.getType());


                progressDialog.dismiss();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        /*
        DocumentReference docRef = firebaseFirestore
                .collection("sworkers")
                .document(sID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                swroker = documentSnapshot.toObject(Swroker.class);

                userPhoneTV.setText(normalfunc.getNumberWithoutCountryCode(swroker.getS_phone()));
                userPin.setText(swroker.getS_pass());
                userName.setText(swroker.getS_name());
                final String typeID = swroker.getType();

                picurl=  swroker.getS_pic();

                Log.e(TAG, "onSuccess: picurl = "+picurl );
                Log.e(TAG, "onSuccess: bitmap = "+bitmap);

                if(bitmap!=null && !swroker.getS_pic().isEmpty() && !swroker.getS_pic().equals("none")){

                    Glide.with(context).load(swroker.getS_pic()).placeholder(R.drawable.male1).into(userPhoto);

                }




                firebaseFirestore.collection("stype")
                        .whereEqualTo("type_id", typeID).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    Types types = documentSnapshot.toObject(Types.class);
                                    userWtype.setText(types.getBangla());
                                }
                            }
                        });


//                userWtype.setText(swroker.getType());


                progressDialog.dismiss();


            }
        });*/

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.done) {


            upload();


        }
        /*if (view.getId() == R.id.user_wtype) {
            Toast.makeText(context, "user Type", Toast.LENGTH_SHORT).show();
            showAlltypes();
        }*/
    }

    //show dialog for select user type

    public void showAlltypes() {

        final TypesAdapter valueAdapter = new TypesAdapter(types, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_designation_list, null);
        final EditText editText = convertView.findViewById(R.id.sear);

        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.done);
        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        editText.setVisibility(View.GONE);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(valueAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //type.setText(editText.getText().toString());
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
                typeselected = (Types) lv.getItemAtPosition(position);
                //cname.setText(myoffice.getName());
                userWtype.setText(typeselected.getBangla());
                alertcompany.dismiss();
            }
        });

    }    //show dialog for user type END


    private static final String TAG = "EditWrokerProfileActivi";
    Map<String, Object> doc;
    //start Upload
    public void upload() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Executing Action...");



        photoRef = FirebaseStorage.getInstance().getReference()
                .child("sworkers/" + sID + "/s_pic");
        // Store values at the time of the login attempt.
        final String iname = userName.getText().toString();
        final String phoneno = userPhoneTV.getText().toString();
        final String typetext = userWtype.getText().toString();
        final String pintext = userPin.getText().toString();

/*
        boolean cancel = false;
        View focusView = null;
*/


        if (TextUtils.isEmpty(iname)) {
            userName.setError(getString(R.string.error_field_required));
            userName.requestFocus();
            return;

        }
        if (TextUtils.isEmpty(typetext)) {
            userWtype.setError(getString(R.string.error_field_required));
            userWtype.requestFocus();
            return;

        }

        Log.e(TAG, "upload: bitmap - "+bitmap );
        Log.e(TAG, "upload: picurl - "+picurl );

        if(bitmap==null && picurl.isEmpty()){
            progressDialog.dismiss();

            Toast.makeText(context,"Please select your picture", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();


        List<String> ll = normalfunc.splitstring(userName.getText().toString());
        ll.addAll(normalfunc.splitchar(typeselected.getEnglish().toLowerCase()));
        final List<String> ll1 = ll;

//        final String s_id = firebaseFirestore.collection(getString(R.string.col_sworker)).document().getId();
//        Log.e("TAG", "upload:S ID = "+s_id );

        doc = new HashMap<>();

        doc.put("s_name", userName.getText().toString());
        if (typeselected.getType_id().equals("none")){
            doc.put("type", swroker.getType());

        }else {
            doc.put("type", typeselected.getType_id());

        }
        doc.put("s_pass", userPin.getText().toString());
        doc.put("s_array", ll);


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
                                    String picurl = uri.toString();
                                    doc.put("s_pic", picurl);
                                    doc.put("thumb_s_pic", picurl);





                                    WriteBatch batch = firebaseFirestore.batch();

                                    Log.e("TAG", "upload: " + doc);

                                    DocumentReference setsworker = firebaseFirestore.collection(getString(R.string.col_sworker))
                                            .document(sID);

                                    batch.set(setsworker, doc, SetOptions.merge());

                                    if (typeselected.getEnglish().equals("guard")) {

                                        final Guards guards = new Guards(buildid, commid, userName.getText().toString()
                                                , normalfunc.getRandomNumberString5(), "", Calendar.getInstance().getTime(), normalfunc.futuredate(), "", picurl, "", picurl,
                                                normalfunc.makephone14(userPhoneTV.getText().toString()), sID, ll1);
                                        Log.e("TAG", "upload: " + guards);
                                        DocumentReference setguard = firebaseFirestore.collection(getString(R.string.col_guards))
                                                .document(sID);

                                        batch.set(setguard, guards, SetOptions.merge());
                                    }

                                    ArrayList<String> stringid = new ArrayList<>();
                                    ArrayList<String> stringno = new ArrayList<>();

                                    for (int i = 0; i < historyflatno.size(); i++) {
                                        stringid.add(allflats.get(i).getFlat_id());
                                        stringno.add(allflats.get(i).getF_no());
                                    }

                                    SLastHistory sLastHistory = new SLastHistory(sID, buildid, stringid, stringno, Calendar.getInstance().getTime());
                                    DocumentReference setflat = firebaseFirestore.collection(getString(R.string.col_sworker))
                                            .document(sID).collection("shistory").document(buildid);

                                    batch.set(setflat, sLastHistory, SetOptions.merge());
                                    Log.e("TAG", "upload: " + sLastHistory);


                                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                progressDialog.dismiss();
                                                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                                                userName.setText("");
                                                userPhoneTV.setText("");
                                                userFlat.setText("");
                                                userWtype.setText("");
                                                startActivity(new Intent(EditWrokerProfileActivity.this, SWorkersActivity.class)
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                    UniversalImageLoader.setImage("", , null, "");
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
        }

        else{
            WriteBatch batch = firebaseFirestore.batch();

            Log.e("TAG", "upload: " + doc);

            DocumentReference setsworker = firebaseFirestore.collection(getString(R.string.col_sworker))
                    .document(sID);

            batch.set(setsworker, doc, SetOptions.merge());

            if (typeselected.getEnglish().equals("guard")) {

                final Guards guards = new Guards(buildid, commid, userName.getText().toString()
                        , normalfunc.getRandomNumberString5(), "", Calendar.getInstance().getTime(), normalfunc.futuredate(), "", picurl, "", picurl,
                        normalfunc.makephone14(userPhoneTV.getText().toString()), sID, ll1);
                Log.e("TAG", "upload: " + guards);
                DocumentReference setguard = firebaseFirestore.collection(getString(R.string.col_guards))
                        .document(sID);

                batch.set(setguard, guards, SetOptions.merge());
            }

            ArrayList<String> stringid = new ArrayList<>();
            ArrayList<String> stringno = new ArrayList<>();

            for (int i = 0; i < historyflatno.size(); i++) {
                stringid.add(allflats.get(i).getFlat_id());
                stringno.add(allflats.get(i).getF_no());
            }

            SLastHistory sLastHistory = new SLastHistory(sID, buildid, stringid, stringno, Calendar.getInstance().getTime());
            DocumentReference setflat = firebaseFirestore.collection(getString(R.string.col_sworker))
                    .document(sID).collection("shistory").document(buildid);

            batch.set(setflat, sLastHistory, SetOptions.merge());
            Log.e("TAG", "upload: " + sLastHistory);


            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {


                        progressDialog.dismiss();
                        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                        userName.setText("");
                        userPhoneTV.setText("");
                        userFlat.setText("");
                        userWtype.setText("");
                        startActivity(new Intent(EditWrokerProfileActivity.this, MainPage.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                    UniversalImageLoader.setImage("", , null, "");
                    }
                }
            });
        }







    }
    //upload End

    private void initView() {
        userPhoneTV = (TextView) findViewById(R.id.user_Phone_TV);
        generatepin = (Button) findViewById(R.id.generatepin);
        userPin = (EditText) findViewById(R.id.user_pin);
        userName = (EditText) findViewById(R.id.user_name);
        userWtype = (EditText) findViewById(R.id.user_wtype);
        userFlat = (EditText) findViewById(R.id.user_flat);
        userPhoto= findViewById(R.id.user_photo_IV);
        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(EditWrokerProfileActivity.this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        allflats = new ArrayList<>();
        historyflatno = new ArrayList<>();
        historyflatId = new ArrayList<>();
        types = new ArrayList<>();
        typeselected = new Types();


        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                /*PickSetup setup = new PickSetup()
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
                        .show(EditWrokerProfileActivity.this);*/
            }
        });
    }

 /*   @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //progressBar.setVisibility(View.VISIBLE);

//            userPhoto.setImageBitmap(null);



            mFileUri = r.getUri().toString();
            bitmap = r.getBitmap();
            Log.d(TAG, "onPickResult: uuu "+ bitmap);
            userPhoto.setImageBitmap(r.getBitmap());

        } else {
            Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_LONG).show();

        }
    }

*/
    public void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Photo Option..");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (options[which].equals("Take Photo")) {
                    cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, TAKE_PICTURE_CODE);
                } else if (options[which].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);


                Glide.with(getApplicationContext()).load(data.getData()).into(userPhoto);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == TAKE_PICTURE_CODE && resultCode == RESULT_OK ) {

//            Toast.makeText(context, ""+data.getExtras().get("data"), Toast.LENGTH_SHORT).show();

            bitmap = (Bitmap) data.getExtras().get("data");

            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            Glide.with(getApplicationContext()).load(bitmap).into(userPhoto);

//            userPhoto.setImageBitmap(bitmap);

        }

    }
}