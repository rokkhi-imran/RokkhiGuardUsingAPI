package com.rokkhi.rokkhiguard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Child;
import com.rokkhi.rokkhiguard.Model.Notifications;
import com.rokkhi.rokkhiguard.Model.Parkings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ParkingActivity extends AppCompatActivity  {
    FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot lastVisible=null;
    private boolean isLastItemReached = false;
    private static final String TAG = "ParkingActivity";
    ArrayList<Parkings> list;
    RecyclerView recyclerView;
    FirebaseUser user;
    View mrootView;
    Toolbar toolbar;
    ProgressBar progressBar;
    Context context;
    SharedPreferences sharedPref;
    CollectionReference parkingRef;

    EditText search;
    SharedPreferences.Editor editor;
    GridAdapter gridAdapter;

    NestedScrollView myNestedScroll;
    boolean shouldscrol=true;


    Query getFirstQuery;
    Date d;
    String flatid = "", buildid = "", commid = "";
    int floorno,flatno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseFirestore= FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        context= ParkingActivity.this;
        myNestedScroll= (NestedScrollView) findViewById(R.id.nested);
        progressBar= findViewById(R.id.progressBar1);

        mrootView=findViewById(R.id.root);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor= sharedPref.edit();
        search = findViewById(R.id.search);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        floorno = sharedPref.getInt("floorno", 0);
        flatno= sharedPref.getInt("flatno",0);

        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this,flatno));

        parkingRef=firebaseFirestore.
                collection(getString(R.string.col_parkings));

        getFirstQuery= parkingRef.whereEqualTo("build_id",buildid).orderBy("f_no",Query.Direction.ASCENDING);

//        firebaseFirestore.collection(getString(R.string.col_parkings)).document("0KL4jRqdvqRyA1u3EnZL")
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    Parkings parkings= task.getResult().toObject(Parkings.class);
//                    Log.d(TAG, "onComplete: iii "+ parkings.isVacant());
//
//                }
//            }
//        });

        getfirstdata();




    }


    public void getfirstdata(){
        getFirstQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: kotoboro "+task.getResult().size());
                    list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Parkings parkings = document.toObject(Parkings.class);
                        Log.d(TAG, "onComplete: uuu "+ parkings.isVacant()+" "+ parkings.getF_no());
                        list.add(parkings);
                    }
                    progressBar.setVisibility(View.GONE);
                    gridAdapter = new GridAdapter(list,context);
                    gridAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(gridAdapter);


                }
                else {
                    Log.d(TAG, "onComplete: kotoboro1");
                }
            }
        });

    }





}
