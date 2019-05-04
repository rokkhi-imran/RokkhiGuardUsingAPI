package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.rokkhiguard.Model.Swroker;

import java.util.ArrayList;
import java.util.List;

import huwi.joldi.abrar.rokkhiguardo.Kotlin.LinePinField;


public class GatePass extends AppCompatActivity implements View.OnClickListener, GateAdapter.MyInterface {

    //after gate pass-----> content add visitor

    LinePinField linePinField;
    TextView one;
    TextView two;
    TextView three;
    TextView four;
    TextView five;
    TextView six;
    TextView seven;
    TextView eight;
    TextView nine;
    TextView zero;
    TextView cross;
    String passtext="";
    Context context;
    AlertDialog alertDialog;
    List<Swroker> list;
    RecyclerView recyclerView;
    TextView nomatch;
    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    GateAdapter gateAdapter;
    ProgressBar progressBar;
    Integer today;
    private static final String TAG = "GatePass";
    String flatid = "", buildid = "", commid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_pass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = GatePass.this;

        firebaseFirestore= FirebaseFirestore.getInstance();

        linePinField= findViewById(R.id.linepin);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        editor=sharedPref.edit();

        one= findViewById(R.id.propic);
        two= findViewById(R.id.two);
        three= findViewById(R.id.three);
        four= findViewById(R.id.four);
        five= findViewById(R.id.five);
        six= findViewById(R.id.six);
        seven= findViewById(R.id.seven);
        eight= findViewById(R.id.eight);
        nine= findViewById(R.id.nine);
        zero= findViewById(R.id.zero);
        cross= findViewById(R.id.cross);


        one.setOnClickListener( this);
        two.setOnClickListener( this);
        three.setOnClickListener( this);
        four.setOnClickListener( this);
        five.setOnClickListener( this);
        six.setOnClickListener( this);
        seven.setOnClickListener( this);
        eight.setOnClickListener( this);
        nine.setOnClickListener( this);
        zero.setOnClickListener( this);
        cross.setOnClickListener( this);

        initdialog();


    }



    @Override
    public void onClick(View v) {


        if(buildid.equals("none")){
            final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            firebaseFirestore.collection(getString(R.string.col_phoneguards)).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot=task.getResult();
                        if(documentSnapshot!=null && documentSnapshot.exists()){
                            buildid=documentSnapshot.getString("build_id");
                        }
                    }
                }
            });
        }

        if(v.getId()==R.id.cross  ){
            if( passtext.length() >0)passtext=passtext.substring(0,passtext.length()-1);
        }
        else{
            TextView tt= (TextView) v;
            passtext=passtext+ tt.getText().toString();
        }
        linePinField.setText(passtext);
        if(passtext.length()==5){
            showdialog();
            return;
        }

    }


    private void showdialog() {
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_showlist, null);
        recyclerView = convertView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar= convertView.findViewById(R.id.progressBar1);
        nomatch= convertView.findViewById(R.id.nomatch);
        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection(getString(R.string.col_sworker))
                .whereEqualTo("s_pass", passtext).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                passtext="";
                linePinField.setText("");
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete:  cjj2 " );
                    list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Swroker swroker = document.toObject(Swroker.class);
                        list.add(swroker);
                    }
                    if(list.isEmpty())nomatch.setVisibility(View.VISIBLE);
                    gateAdapter = new GateAdapter(list,context);
                    gateAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(gateAdapter);
                }
                else Log.d(TAG, "onComplete:  cjj3");

            }
        });



        alertDialog.setView(convertView);
        alertDialog.show();


    }

    Dialog mdialog;

    public void initdialog(){
        mdialog=new Dialog(this);

        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);

    }



    @Override
    public void dissmissdialog(){
        alertDialog.dismiss();
    }



    @Override
    public void showprogressbar(){
        mdialog.show(); }


    @Override
    public void hideprogressbar(){
        mdialog.dismiss();
    }



}
