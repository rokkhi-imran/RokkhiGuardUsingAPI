package com.rokkhi.rokkhiguard;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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
    VehiclesRepository vehiclesRepository ;
    ArrayList<Vehicle> allVehicles;

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
        vehiclesRepository = new VehiclesRepository(this);

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


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseFirestore.getInstance().collection("b_flags").document(buildid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    if(documentSnapshot.contains("v_changed") && documentSnapshot.getBoolean("v_changed")){
                        Log.d("firebase" , "Getting new Vehicles data because data is changed or updated" );
                        getVehiclesAndSaveToLocalDatabase();
                    }

                }
            }
        });

    }

    public void getVehiclesAndSaveToLocalDatabase(){
        allVehicles = new ArrayList<>();
        //final FlatsRepository flatsRepository = new FlatsRepository(this);


        Log.d("room" , "getting new vehicle success " + buildid);
        firebaseFirestore.collection(getString(R.string.col_vehicle))
                .whereEqualTo("build_id",buildid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("room" , "getting new vehicle success " + "adsf");
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Vehicle vehicle = documentSnapshot.toObject(Vehicle.class);
                        Log.d("room" , "getting new vehicle data found " + "adsf");
                        vehiclesRepository.deleteVehicle(vehicle);
                        vehiclesRepository.insert(vehicle);
                    }

                }
                else{
                    Log.d(TAG, "onComplete: xxx5");
                }
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
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

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            gridAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });





                }
                else {
                    Log.d(TAG, "onComplete: kotoboro1");
                }
            }
        });

    }





}
