package com.rokkhi.rokkhiguard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.Model.SLastHistory;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Model.Types;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditVisitorProfileActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView userPhoneTV;
    protected Button generatepin;
    protected EditText userPin;
    protected EditText userName;
    protected EditText userWtype;
    protected EditText userFlat;
    protected Button done;
    Intent intent;
    Context context;
    String sID;
    FirebaseFirestore firebaseFirestore;
    Swroker swroker;
    String buildid = "";
    String commid = "";
    String total="";

    ArrayList<ActiveFlats> allflats;
    ArrayList<String> historyflatno;
    ArrayList<String> historyflatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_edit_visitor_profile);
        initView();
        context= EditVisitorProfileActivity.this;
        intent = getIntent();
        sID = intent.getStringExtra("s_id");



        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");

        getUserData(sID);
        getFlatList(sID);

        //load Flat
        loadFlat();
    }

    private void loadFlat() {

        firebaseFirestore.collection(getString(R.string.col_activeflat)).whereEqualTo("build_id",buildid).
                orderBy("f_no", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    allflats.clear();
                    for(DocumentSnapshot documentSnapshot: task.getResult()){

                        ActiveFlats activeFlat= documentSnapshot.toObject(ActiveFlats.class);
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
                flatName="";
                historyflatId.clear();
                historyflatno.clear();
                for(int i=0;i<allflats.size();i++){
                    activeFlatAdapter.changedata(allflats.get(i).getF_no(), true);

                    historyflatId.add(allflats.get(i).getFlat_id());
                    historyflatno.add(allflats.get(i).getF_no());

                    flatName=  flatName+" "+allflats.get(i).getF_no();
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


                flatName="";
                tt.setText("");
                historyflatId.clear();
                historyflatno.clear();
                for(int i=0;i<allflats.size();i++){
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

        for(int i=0;i<historyflatno.size();i++){
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
//                    view.findViewById(R.id.name).setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_textsize_with_bg));
//                    holder.ename.setTextColor(ContextCompat.getColor(context,R.color.white));
                    activeFlatAdapter.changedata(ss.getF_no(), true);
                    activeFlatAdapter.notifyDataSetChanged();
                    historyflatId.add(ss.getFlat_id());
                    historyflatno.add(ss.getF_no());
                    flatName = flatName + " " + ss.getF_no();
                    tt.setText(flatName);
                    //

                } else {
////                    Log.d(TAG, "onItemClick: rrr2");
//                    view.findViewById(R.id.name).setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_textsize));
//                    holder.ename.setTextColor(ContextCompat.getColor(context,R.color.Black));

                    activeFlatAdapter.changedata(ss.getF_no(), false);
                    activeFlatAdapter.notifyDataSetChanged();
                    historyflatId.remove(ss.getFlat_id());
                    historyflatno.remove(ss.getF_no());
                    flatName = flatName.replace(" " + ss.getF_no(), "");
                    tt.setText(flatName);
                    // activeFlatAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    String flatName="";

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
                        flatName="";
                        for (int i = 0; i < historyflatno.size(); i++) {
                            Log.e("TAG", "onComplete: "+historyflatno.get(i));

                            flatName=flatName+" "+historyflatno.get(i);
                        }
                        userFlat.setText(flatName);


                    }
                }
            }
        });
    }

    private void getUserData(final String sID) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Executing Action.....");
        progressDialog.show();
        DocumentReference docRef = firebaseFirestore
                .collection("sworkers")
                .document(sID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                swroker = documentSnapshot.toObject(Swroker.class);

                userPhoneTV.setText(swroker.getS_phone());
                userPin.setText(swroker.getS_pass());
                userName.setText(swroker.getS_name());
                userWtype.setText(swroker.getType());


                progressDialog.dismiss();


            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.done) {


//            upload();


        }
    }

/*

    public void upload() {

        Types typeselected = new Types();
        Normalfunc normalfunc = new Normalfunc();.

        // Store values at the time of the login attempt.
        final String iname = userName.getText().toString();
        final String phoneno = userPhoneTV.getText().toString();
        final String typetext = userWtype.getText().toString();
        final String pintext = userPin.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(iname)) {
            userName.setError(getString(R.string.error_field_required));
            userName.requestFocus();
            return;

        }
        if (TextUtils.isEmpty(typetext)) {
            userWtype.setError(getString(R.string.error_field_required));
            userWtype.requestFocus();

        }


        List<String> ll = normalfunc.splitstring(userName.getText().toString());
        ll.addAll(normalfunc.splitchar(typeselected.getEnglish().toLowerCase()));
        final List<String> ll1 = ll;

        final String s_id = firebaseFirestore.collection(getString(R.string.col_sworker)).document().getId();

        Map<String, Object> doc = new HashMap<>();

        doc.put("s_name", userName.getText().toString());
        doc.put("type", typeselected.getEnglish());
        doc.put("s_pass", userPin.getText().toString());
        doc.put("s_array", ll);

        WriteBatch batch = firebaseFirestore.batch();


        DocumentReference setsworker = firebaseFirestore.collection(getString(R.string.col_sworker))
                .document(s_id);

        batch.set(setsworker, doc);

        if (typeselected.getEnglish().equals("guard")) {

            final Guards guards = new Guards(buildid, commid, userName.getText().toString()
                    , normalfunc.getRandomNumberString5(), "", Calendar.getInstance().getTime(), normalfunc.futuredate(), "", "", "", "",
                    normalfunc.makephone14(userPhoneTV.getText().toString()), s_id, ll1);
            DocumentReference setguard = firebaseFirestore.collection(getString(R.string.col_guards))
                    .document(s_id);

            batch.set(setguard, guards);

        }

        ArrayList<String> stringid = new ArrayList<>();
        ArrayList<String> stringno = new ArrayList<>();

        for (int i = 0; i < historyFlats.size(); i++) {
            stringid.add(historyFlats.get(i).getFlat_id());
            stringno.add(historyFlats.get(i).getF_no());
        }

        SLastHistory sLastHistory = new SLastHistory(s_id, buildid, stringid, stringno, Calendar.getInstance().getTime());
        DocumentReference setflat = firebaseFirestore.collection(getString(R.string.col_sworker))
                .document(s_id).collection("shistory").document(buildid);

        batch.set(setflat, sLastHistory);


        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

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
*/

    private void initView() {
        userPhoneTV = (TextView) findViewById(R.id.user_Phone_TV);
        generatepin = (Button) findViewById(R.id.generatepin);
        userPin = (EditText) findViewById(R.id.user_pin);
        userName = (EditText) findViewById(R.id.user_name);
        userWtype = (EditText) findViewById(R.id.user_wtype);
        userFlat = (EditText) findViewById(R.id.user_flat);
        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(EditVisitorProfileActivity.this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        allflats= new ArrayList<>();
        historyflatno=new ArrayList<>();
        historyflatId=new ArrayList<>();
    }
}
