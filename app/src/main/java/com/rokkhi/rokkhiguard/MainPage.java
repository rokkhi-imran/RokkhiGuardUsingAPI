package com.rokkhi.rokkhiguard;

import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Activebuilding;
import com.rokkhi.rokkhiguard.Model.BuildingChanges;
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.Model.Whitelist;
import com.rokkhi.rokkhiguard.data.FlatsRepository;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;
import com.rokkhi.rokkhiguard.data.WhiteListRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage extends AppCompatActivity {

    CircleImageView gatepass, logout, addvis, vislist, notice, parcel, create, vehicle, child,callLogs;
    private static final String TAG = "MainPage";
    Context context;
    ImageButton settings;
    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    AlertDialog alertDialog;
    String buildid = "", commid = "";

    ArrayList<ActiveFlats> allActiveFlats;
    ArrayList<Whitelist> allWhiteLists;
    ArrayList<Vehicle> allVehicles;
    FlatsRepository flatsRepository;
    WhiteListRepository whiteListRepository;
    VehiclesRepository vehiclesRepository;
    String thismobileuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        Log.d(TAG, "onCreate: " + "xxx");

        Intent intent = getIntent();
        context = MainPage.this;


        gatepass = findViewById(R.id.gatepass);
        logout = findViewById(R.id.logout);
        addvis = findViewById(R.id.addvis);
        vislist = findViewById(R.id.vislist);
        notice = findViewById(R.id.notice);
        parcel = findViewById(R.id.parcel);
        create = findViewById(R.id.profile);
        settings = findViewById(R.id.settings);
        vehicle = findViewById(R.id.vehicle);
        child = findViewById(R.id.child);
        callLogs=findViewById(R.id.callLogs);

        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");


        firebaseFirestore.collection(getString(R.string.col_activebuild)).document(buildid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Activebuilding activebuilding = documentSnapshot.toObject(Activebuilding.class);
                        int floorno = activebuilding.getB_tfloor();
                        int flatno = activebuilding.getB_tflat();
                        editor.putInt("floorno", floorno);
                        editor.putInt("flatno", flatno);
                        editor.apply();
                    }
                }
            }
        });

        thismobileuid = FirebaseAuth.getInstance().getUid();

        flatsRepository = new FlatsRepository(this);
        whiteListRepository = new WhiteListRepository(this);
        vehiclesRepository = new VehiclesRepository(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DaroanPass.class);
                startActivity(intent);
                finish();
            }
        });

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParkingActivity.class);
                startActivity(intent);
            }
        });

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChildrenList.class);
                startActivity(intent);
            }
        });

        gatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, GatePass.class);
                startActivity(intent);
            }
        });
        addvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, AddVisitor.class);
                startActivity(intent);
            }
        });
        vislist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, VisitorsList.class);
                startActivity(intent);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, NoticeBoard.class);
                startActivity(intent);

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {

                    showposititivedialog();


                } else {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainPage.this, DaroanPass.class);
                    startActivity(intent);
                }


            }
        });
        parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, ParcelActivity.class);
                startActivity(intent);

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        callLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, CallLogsActivity.class);
                startActivity(intent);
            }
        });

    }


    public void showposititivedialog() {

        Intent intent = new Intent(MainPage.this, SWorkersActivity.class);
        startActivity(intent);

       /* Intent intent = new Intent(MainPage.this, CreateProfile.class);
        startActivity(intent);*/
    }


    public void getAllActiveFlatsAndSaveToLocalDatabase(final BuildingChanges buildingChanges) {
        // final FlatsRepository flatsRepository = new FlatsRepository(this);

        firebaseFirestore.collection(getString(R.string.col_activeflat))
                .whereEqualTo("build_id", buildid).orderBy("f_no", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: pppp");
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        ActiveFlats activeFlat = documentSnapshot.toObject(ActiveFlats.class);
                        flatsRepository.deleteActiveFlat(activeFlat);
                        flatsRepository.insertActiveFlat(activeFlat);
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

                    flatsRepository.deleteTask(buildid);

                } else {
                    Log.d(TAG, "onComplete: pppp1");
                }
            }
        });
    }


    public void getAllWhiteListAndSaveToLocalDatabase(final BuildingChanges buildingChanges) {
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

    public void matchanddelete(ArrayList<Vehicle>check, Vehicle vehicle){
        boolean flag=false;

        for(int i=0;i<check.size();i++){
            if(check.get(i).getVehicle_id().equals(vehicle.getVehicle_id())){
                //vehiclesRepository.deleteVehicle(vehicle);
                Log.d(TAG, "matchanddelete: hhhh " );
                flag=true;
            }
        }
        if(!flag)vehiclesRepository.deleteVehicle(vehicle);

    }

    public void getVehiclesAndSaveToLocalDatabase(final BuildingChanges buildingChanges) {
        //final FlatsRepository flatsRepository = new FlatsRepository(this);


        Log.d("room", "getting new vehicle success " + buildid);
        firebaseFirestore.collection(getString(R.string.col_vehicle))
                .whereEqualTo("build_id", buildid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    final ArrayList<Vehicle> check = new ArrayList<>();
                    Log.d("room", "getting new vehicle success " + "adsf");
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Vehicle vehicle = documentSnapshot.toObject(Vehicle.class);

                        Log.d("room", "getting new vehicle data found " + "adsf");
                        vehiclesRepository.deleteVehicle(vehicle);
                        vehiclesRepository.insert(vehicle);
                        check.add(vehicle);
                    }

                    Log.d(TAG, "onComplete: kkk "+ check );

                    vehiclesRepository.getAllVehicle().observe(MainPage.this, new Observer<List<Vehicle>>() {
                        @Override
                        public void onChanged(@Nullable List<Vehicle> allVehicles) {
                            for (Vehicle vehicle : allVehicles) {
                                matchanddelete(check,vehicle);
                                Log.d(TAG, "onChanged: yyyyy "+check.size());
                                Log.d("room yyyyy", "found a new Vehicle   " + vehicle.getF_no() + "  -- > " + vehicle.getFlat_id());
                            }
                        }
                    });


                    Map<String, Object> data = new HashMap<>();
                    ArrayList<String> vdata = new ArrayList<>();
                    vdata = buildingChanges.getVehicles();
                    vdata.add(thismobileuid);
                    data.put("vehicles", vdata);


                    firebaseFirestore.collection("buildingChanges").document(buildid)
                            .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "vehicle data changed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    vehiclesRepository.deleteTask(buildid);


                } else {
                    Log.d(TAG, "onComplete: xxx5");
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

                    if (!vehicles.contains(thismobileuid)) {
                        Log.d("firebase", "Getting new Vehicles data because data is changed or updated");
                        getVehiclesAndSaveToLocalDatabase(buildingChanges);
                    }

                }
                else{
                    BuildingChanges buildingChanges= new BuildingChanges(new ArrayList<String>(),new ArrayList<String>()
                    ,new ArrayList<String>());
                    getAllActiveFlatsAndSaveToLocalDatabase(buildingChanges);
                    getAllWhiteListAndSaveToLocalDatabase(buildingChanges);
                    getVehiclesAndSaveToLocalDatabase(buildingChanges);
                }
            }
        });


        //getting the data from repository example

//        flatsRepository.getAllActiveFlats().observe(this, new Observer<List<ActiveFlats>>() {
//            @Override
//            public void onChanged(@Nullable List<ActiveFlats> allFlats) {
//                for(ActiveFlats flat : allFlats) {
//                    Log.d("room" , "found a new Flat   " + flat.getF_no()+"  -- > " + flat.getFlat_id());
//                }
//            }
//        });


        //getting the data from repository example
//        final WhiteListRepository whiteListRepository = new WhiteListRepository(this);
//        whiteListRepository.getAllWhiteList().observe(this, new Observer<List<Whitelist>>() {
//            @Override
//            public void onChanged(@Nullable List<Whitelist> allWhiteLists) {
//                for(Whitelist whiteList : allWhiteLists) {
//                    Log.d("room" , "found a new WhiteList   " + whiteList.getF_no()+"  -- > " + whiteList.getFlat_id());
//                }
//            }
//        });
//
//
//
//        final VehiclesRepository vehiclesRepository = new VehiclesRepository(this);
//        vehiclesRepository.getAllVehicle().observe(this, new Observer<List<Vehicle>>() {
//            @Override
//            public void onChanged(@Nullable List<Vehicle> allVehicles) {
//                for(Vehicle vehicle : allVehicles) {
//                    Log.d("room" , "found a new Vehicle   " + vehicle.getF_no()+"  -- > " + vehicle.getFlat_id());
//                }
//            }
//        });
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
