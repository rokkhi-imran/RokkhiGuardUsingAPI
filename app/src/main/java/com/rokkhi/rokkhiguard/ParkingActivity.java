package com.rokkhi.rokkhiguard;

import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rokkhi.rokkhiguard.Model.BuildingChanges;
import com.rokkhi.rokkhiguard.Model.Parkings;
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParkingActivity extends AppCompatActivity implements GridAdapter.MyInterface  {
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

    String thismobileuid;


    Query getFirstQuery;
    Date d;
    String flatid = "", buildid = "", commid = "";
    int floorno,flatno;
    VehiclesRepository vehiclesRepository ;
    ArrayList<Vehicle> vehicles;

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
        thismobileuid= FirebaseAuth.getInstance().getUid();

        parkingRef=firebaseFirestore.
                collection(getString(R.string.col_parkings));

        getFirstQuery= parkingRef.whereEqualTo("build_id",buildid).orderBy("f_no",Query.Direction.ASCENDING);

        getfirstdata();






    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseFirestore.getInstance().collection("buildingChanges").document(buildid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    BuildingChanges buildingChanges=documentSnapshot.toObject(BuildingChanges.class);
                    ArrayList<String> flats=buildingChanges.getFlats();
                    ArrayList<String> whitelists= buildingChanges.getWhitelists();
                    ArrayList<String> vehicles= buildingChanges.getVehicles();


                    if(!vehicles.contains(thismobileuid)){
                        Log.d("firebase" , "Getting new Vehicles data because data is changed or updated" );
                        getVehiclesAndSaveToLocalDatabase(buildingChanges);
                    }

                }
            }
        });


        vehiclesRepository.getAllVehicle().observe(this, new Observer<List<Vehicle>>() {
            @Override
            public void onChanged(@Nullable List<Vehicle> allVehicles) {
                vehicles=new ArrayList<>();
                for(Vehicle vehicle : allVehicles) {
                    vehicles.add(vehicle);

                    Log.d("room" , "found a new Vehicle   " + vehicle.getF_no()+"  -- > " + vehicle.getFlat_id());
                }
            }
        });
    }



    public void getVehiclesAndSaveToLocalDatabase(final BuildingChanges buildingChanges){


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


                    Map<String, Object> data = new HashMap<>();
                    ArrayList<String> vdata= new ArrayList<>();
                    vdata= buildingChanges.getVehicles();
                    vdata.add(thismobileuid);
                    data.put("vehicles",vdata);


                    firebaseFirestore.collection("buildingChanges").document(buildid)
                            .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context,"Whitelists data changed!",Toast.LENGTH_SHORT).show();
                        }
                    });


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


    @Override
    public ArrayList<Vehicle> fetchFlatVehicle(String flatid) {
        ArrayList<Vehicle> tempvv;
        tempvv=new ArrayList<>();
        for(Vehicle vv: vehicles){
            if(vv.getFlat_id().equals(flatid)){
                tempvv.add(vv);
            }
        }
        return tempvv;
    }
}
