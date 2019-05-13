package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Flats;
import com.rokkhi.rokkhiguard.Model.Invitees;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Model.Vsearch;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;



public class AddVisitor extends AppCompatActivity implements InviteeAdapter.MyInterface {

    CircleImageView userphoto;
    EditText username, phone, purpose, idcardno, email, org, flat;
    Button done;
    Map<String, Object> doc;

    String mFileUri = "";
    Context context;
    FirebaseFirestore firebaseFirestore;
    ArrayList<ActiveFlats> allflats;
    private Bitmap bitmap = null;
    FirebaseUser firebaseUser;
    Date low,high;
    private static final String TAG = "AddVisitor";

    private long mLastClickTime = 0;
    Timestamp out = new Timestamp(0, 0);

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    ProgressBar progressBar;
    StorageReference photoRef;
    String visitorid;
    String flatid = "", buildid = "", commid = "",famid="",userid="";
    ActiveFlats selected;

    Calendar myCalendar;
    InviteeAdapter inviteeAdapter;
    Normalfunc normalfunc;

    private int res;
    ArrayList<Invitees> list;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    boolean flag;
    ImageView cut;

    String linkFromSearch="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = AddVisitor.this;
        normalfunc= new Normalfunc();
        flag=false;



        done = findViewById(R.id.done);
        username = findViewById(R.id.user_name);
        phone = findViewById(R.id.phone_no);
        org = findViewById(R.id.fromwhere);
        purpose = findViewById(R.id.pupose);
        flat = findViewById(R.id.flat);
        email = findViewById(R.id.email);
        idcardno = findViewById(R.id.card_no);
        //changepic = findViewById(R.id.changeProfilePhoto);
        userphoto = findViewById(R.id.user_photo);
        progressBar = findViewById(R.id.progressBar1);
        myCalendar = Calendar.getInstance();
        cut= findViewById(R.id.cut);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        editor= sharedPref.edit();



        initonclick();
        listener();
    }

    public void cleardata(){
        Intent intent= new Intent(AddVisitor.this,AddVisitor.class);
        startActivity(intent);
        finish();
    }

    public void getdate(){
        Calendar cal = Calendar.getInstance(); //current date and time
        cal.add(Calendar.DAY_OF_MONTH, 0); //add a day
        cal.set(Calendar.HOUR_OF_DAY, 23); //set hour to last hour
        cal.set(Calendar.MINUTE, 59); //set minutes to last minute
        cal.set(Calendar.SECOND, 59); //set seconds to last second
        cal.set(Calendar.MILLISECOND, 999); //set milliseconds to last millisecond

        high=cal.getTime();
        Calendar cal1 = Calendar.getInstance(); //current date and time
        cal1.add(Calendar.DAY_OF_MONTH, 0); //add a day
        cal1.set(Calendar.HOUR_OF_DAY, 0); //set hour to last hour
        cal1.set(Calendar.MINUTE, 0); //set minutes to last minute
        cal1.set(Calendar.SECOND, 0); //set seconds to last second
        cal1.set(Calendar.MILLISECOND, 0); //set milliseconds to last millisecond
        low= cal1.getTime();

    }

    public void listener(){
        getdate();
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==11 && !flag){
                    flag=true;

                    Log.d(TAG, "onTextChanged:  nn1");
                    firebaseFirestore.
                            collection(getString(R.string.col_invitees))
                            .whereEqualTo("i_phone",phone.getText().toString())
                            .whereEqualTo("hasdone",false)
                            .whereGreaterThan("i_mtime",low)
                            .whereLessThan("i_mtime",high).
                            orderBy("i_mtime", Query.Direction.ASCENDING).get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        list = new ArrayList<>();
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Invitees invitee = document.toObject(Invitees.class);
                                            list.add(invitee);
                                        }
                                        progressBar.setVisibility(View.GONE);
                                        inviteeAdapter = new InviteeAdapter(list,context);
                                        inviteeAdapter.setHasStableIds(true);

                                        if(!list.isEmpty()){
                                            Log.d(TAG, "onComplete: rrr2 ");
                                            alertDialog = new AlertDialog.Builder(context).create();
                                            alertDialog.setCancelable(false);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View convertView = (View) inflater.inflate(R.layout.dialog_showlist, null);
                                            TextView hide= convertView.findViewById(R.id.text);
                                            hide.setVisibility(View.GONE);
                                            TextView header = convertView.findViewById(R.id.tik);
                                            header.setText("He is invited today!");
                                            recyclerView = convertView.findViewById(R.id.list);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                            recyclerView.setAdapter(inviteeAdapter);
                                            alertDialog.setView(convertView);
                                            alertDialog.show();
                                        }

                                        else{
                                            firebaseFirestore.collection("search")
                                                    .document(phone.getText().toString()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                DocumentSnapshot documentSnapshot= task.getResult();
                                                                if(documentSnapshot!=null && documentSnapshot.exists()){
                                                                    Log.d(TAG, "onComplete: rrr "+ "ashse ");
                                                                    final Vsearch vsearch= documentSnapshot.toObject(Vsearch.class);
                                                                    alertDialog = new AlertDialog.Builder(context).create();
                                                                    alertDialog.setCancelable(false);
                                                                    LayoutInflater inflater = getLayoutInflater();
                                                                    View convertView = (View) inflater.inflate(R.layout.item_person, null);
                                                                    CircleImageView propic= convertView.findViewById(R.id.propic);
                                                                    TextView name= convertView.findViewById(R.id.name);
                                                                    Button cancel= convertView.findViewById(R.id.cancel);
                                                                    RelativeLayout relativeLayout= convertView.findViewById(R.id.relativeLayout);


                                                                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            username.setText(vsearch.getV_name());
                                                                            phone.setText(vsearch.getV_phone());
                                                                            org.setText(vsearch.getV_org());
                                                                            if(!vsearch.getV_purpose().isEmpty())purpose.setText(vsearch.getV_purpose());

                                                                            email.setText(vsearch.getV_mail());
                                                                            UniversalImageLoader.setImage(vsearch.getV_thumb(),userphoto, null, "");

                                                                            if(!vsearch.getV_thumb().isEmpty()){
                                                                                linkFromSearch= vsearch.getV_thumb();
                                                                            }
                                                                            username.requestFocus();
                                                                            alertDialog.dismiss();
                                                                        }
                                                                    });

                                                                    name.setText(vsearch.getV_name());
                                                                    UniversalImageLoader.setImage(vsearch.getV_thumb(),propic, null, "");

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
                                        }
                                    }
                                }
                            }
                    );
                }

                else if(s.length()<11) flag=false;
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    public void upload() {

        Log.d(TAG, "upload: yyyy");

        List<String>ll= normalfunc.splitstring(username.getText().toString());
        if(!email.getText().toString().isEmpty())ll.addAll(normalfunc.splitchar(email.getText().toString().toLowerCase()));
        ll.addAll(normalfunc.splitchar(phone.getText().toString().toLowerCase()));
        ll.add(selected.getF_no());
       // ll.add(selected.getE_login().replace("+88",""));
        if(!org.getText().toString().isEmpty())ll.addAll(normalfunc.splitchar(org.getText().toString().toLowerCase()));





        doc = new HashMap<>();
        doc.put("v_checkin", FieldValue.serverTimestamp());
        doc.put("v_checkout", FieldValue.serverTimestamp());
        doc.put("v_name", username.getText().toString());
        doc.put("v_mail", email.getText().toString());
        doc.put("v_phone", phone.getText().toString());
        doc.put("v_purpose", purpose.getText().toString());
        doc.put("v_where", org.getText().toString());
        doc.put("v_gpass", idcardno.getText().toString());
        doc.put("flat_id", selected.getFlat_id());
        doc.put("comm_id", selected.getComm_id());
        doc.put("build_id", selected.getBuild_id());
        doc.put("family_id", selected.getFamily_id());
        doc.put("v_vehicleno", "");
        doc.put("v_pic", "");
        doc.put("v_thumb", "");
        doc.put("isin",false);
        doc.put("response",1);
        doc.put("v_type","visitor");
        doc.put("v_array",ll);

        if(!linkFromSearch.isEmpty()){
            doc.put("v_pic", linkFromSearch);
            doc.put("v_thumb", linkFromSearch);
        }





        // [END get_child_ref]

        visitorid = firebaseFirestore
                .collection(getString(R.string.col_visitors)).document().getId();
        doc.put("v_uid",visitorid);

        photoRef = FirebaseStorage.getInstance().getReference()
                .child("/visitors/" + visitorid + "/pic");


        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);

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
                                    doc.put("v_thumb", uri.toString());


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


            firebaseFirestore
                    .collection(getString(R.string.col_visitors)).document(visitorid)
                    .set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dialogconfirmation(visitorid);
                        //progressBar.setVisibility(View.GONE);
                        dismissdialog();
                        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void dialogconfirmation(final String uid){


        boolean approve;
        if(selected.getFamily_id()==null || selected.getFamily_id().isEmpty())approve=false;
        else approve=true;

        Log.d(TAG, "dialogconfirmation:  approve "+ approve);

        final AlertDialog alertconfirm = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_confrimation, null);
        final TextView status= convertView.findViewById(R.id.status);
        final Button submit = convertView.findViewById(R.id.submit);
        final CircleImageView enter = convertView.findViewById(R.id.enter);
        final CircleImageView cancel = convertView.findViewById(R.id.cancel);

        final  ProgressBar progressBar= convertView.findViewById(R.id.dialogprogress);
        alertconfirm.setView(convertView);
        alertconfirm.setCancelable(false);



        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection(getString(R.string.col_visitors)).document(uid)
                        .update("isin",true ,"response", 6).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context,"Done!",Toast.LENGTH_SHORT).show();
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
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context,"Done!",Toast.LENGTH_SHORT).show();
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
                if(res==2){
                    firebaseFirestore.collection(getString(R.string.col_visitors)).document(uid)
                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(context,"Done!",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                alertconfirm.dismiss();
                                cleardata();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(context,"Done!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    alertconfirm.dismiss();
                    cleardata();
                }
            }
        });


        if(approve){

            new CountDownTimer(60000, 100) {

                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    if(res!=2 && res!=3){
                        progressBar.setVisibility(View.GONE);
                        status.setText("No response ( সাড়া পাওয়া যায়নি )");
                        status.setTextColor(Color.BLUE);
                    }
                    else{
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

                                Visitors visitors= documentSnapshot.toObject(Visitors.class);
                                res= visitors.getResponse();
                                if(res==2){

                                    enter.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);
                                    submit.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    status.setText("Rejected  ( বাতিল )");
                                    status.setTextColor(Color.RED);
                                }

                                else if(res==3){
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
        }

        else{
            progressBar.setVisibility(View.GONE);
            status.setText("No response ( সাড়া পাওয়া যায়নি )");
            status.setTextColor(Color.BLUE);
        }



        alertconfirm.show();
    }





    public void addallusers(){
        allflats= new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore
                .collection(getString(R.string.col_activeflat)).whereEqualTo("build_id",buildid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    allflats.clear();
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        ActiveFlats activeFlat= documentSnapshot.toObject(ActiveFlats.class);
                        allflats.add(activeFlat);
                    }
                    progressBar.setVisibility(View.GONE);

                    //TODO ekhane flat offline a dekhano jaite pare

                    final ActiveFlatAdapter valueAdapter=new ActiveFlatAdapter(allflats,context);
                    final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom_list, null);
                    final EditText editText=convertView.findViewById(R.id.sear);
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
                            selected=(ActiveFlats) lv.getItemAtPosition(position);
                            //cname.setText(myoffice.getName());
                            flat.setText(selected.getF_no());
                            alertcompany.dismiss();
                        }
                    });
                }
            }
        });
    }



    public void initonclick() {


        flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addallusers();

            }
        });

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
                            //progressBar.setVisibility(View.VISIBLE);

                            mFileUri = r.getUri().toString();
                            bitmap = r.getBitmap();
                            userphoto.setImageBitmap(r.getBitmap());


                        } else {
                            Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }).show(AddVisitor.this);
            }
        });

        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purpose.setText("");
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

                if (!normalfunc.isvalidphone(phoneno)){
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

    @Override
    public void loadagain() {
        Intent intent= new Intent(this,VisitorsList.class);
        startActivity(intent);
        finish();
    }
}
