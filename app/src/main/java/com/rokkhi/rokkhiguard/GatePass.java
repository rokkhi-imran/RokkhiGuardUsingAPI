package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;

import java.util.ArrayList;
import java.util.List;


public class GatePass extends AppCompatActivity implements View.OnClickListener, GateAdapter.MyInterface {

    //after gate pass-----> content add visitor

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
    TextView clear;
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
    Button search;
    private static final String TAG = "GatePass";
    String flatid = "", buildid = "", commid = "";
    EditText phnno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_pass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = GatePass.this;

        firebaseFirestore= FirebaseFirestore.getInstance();

        search= findViewById(R.id.search);
        phnno= findViewById(R.id.phnno);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        editor=sharedPref.edit();

        one= findViewById(R.id.one);
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
        clear= findViewById(R.id.clear);


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
        clear.setOnClickListener( this);

        initdialog();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phnno.getText().toString().length()==5){
                    showdialogpin();
                }
                else Toast.makeText(context,"Pin must have 5 characters",Toast.LENGTH_SHORT).show();
            }
        });


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
        else if(v.getId() == R.id.clear){
            passtext="";
        }
        else{
            TextView tt= (TextView) v;
            passtext=passtext+ tt.getText().toString();
        }
        phnno.setText(passtext);
        if(passtext.length()==11){
            showdialog();
            return;
        }

    }


    private void showdialog() {
        Normalfunc normalfunc=new Normalfunc();
        String newtext= normalfunc.makephone14(passtext);
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_showlist, null);
        recyclerView = convertView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar= convertView.findViewById(R.id.progressBar1);
        nomatch= convertView.findViewById(R.id.nomatch);
        final Button profilecreate= convertView.findViewById(R.id.profilecreation);
        final Button cancel= convertView.findViewById(R.id.cancel);
        progressBar.setVisibility(View.VISIBLE);


        firebaseFirestore.collection(getString(R.string.col_sworker))
                .whereEqualTo("s_phone", newtext).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                passtext="";
                phnno.setText("");
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete:  cjj2 " );
                    list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Swroker swroker = document.toObject(Swroker.class);
                        list.add(swroker);
                    }
                    if(list.isEmpty()){
                        nomatch.setVisibility(View.VISIBLE);
                        profilecreate.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        profilecreate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(context, CreateProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
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

    private void showdialogpin() {
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_showlist, null);
        recyclerView = convertView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar= convertView.findViewById(R.id.progressBar1);
        nomatch= convertView.findViewById(R.id.nomatch);
        final Button profilecreate= convertView.findViewById(R.id.profilecreation);
        final Button cancel= convertView.findViewById(R.id.cancel);
        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection(getString(R.string.col_sworker))
                .whereEqualTo("s_pass", passtext).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                passtext="";
                phnno.setText("");
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete:  cjj2 " );
                    list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Swroker swroker = document.toObject(Swroker.class);
                        list.add(swroker);
                    }

                    if(list.isEmpty()){
                        nomatch.setVisibility(View.VISIBLE);
                        profilecreate.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        profilecreate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent= new Intent(context, CreateProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
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
