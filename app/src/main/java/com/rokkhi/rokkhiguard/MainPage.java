package com.rokkhi.rokkhiguard;

import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Activebuilding;
import com.rokkhi.rokkhiguard.Model.BlackList;
import com.rokkhi.rokkhiguard.Model.BuildingChanges;
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Model.Whitelist;
import com.rokkhi.rokkhiguard.data.BlackListRepository;
import com.rokkhi.rokkhiguard.data.FlatsRepository;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;
import com.rokkhi.rokkhiguard.data.WhiteListRepository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage extends AppCompatActivity {

    private static final String TAG = "MainPage";
    CircleImageView gatepass, logout, addvis, vislist, notice, parcel, create, vehicle, child, callLogs, guardList;
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
    BlackListRepository blackListRepository;
    VehiclesRepository vehiclesRepository;
    String thismobileuid;
    String appVersion;

    RecyclerView recyclerViewVisitorAdapter;

    ArrayList<Visitors> visitorsArrayList;

    Button buildingName;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        Log.d(TAG, "onCreate: " + "xxx");

        context = MainPage.this;

        visitorsArrayList = new ArrayList<>();
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
        callLogs = findViewById(R.id.callLogs);
        guardList = findViewById(R.id.guardList);
        buildingName = findViewById(R.id.buildingNameTV);
        recyclerViewVisitorAdapter = findViewById(R.id.recyclerviewVisitorWaitingListID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        recyclerViewVisitorAdapter.setLayoutManager(linearLayoutManager);

        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");

        //set default caller

        offerReplacingDefaultDialer(context);


        //Load visitor Waiting List Start

        //check the call permission
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
            requestPermissions(permissions, 1);
        }

        firebaseFirestore.collection(getString(R.string.col_visitors))
                .whereEqualTo("completed", false)
                .whereEqualTo("build_id", buildid)
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(15)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (queryDocumentSnapshots==null){
                            return;
                        }

                        visitorsArrayList.clear();

                        for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                            Visitors visitors =snapshot.toObject(Visitors.class);
                            visitorsArrayList.add(visitors);
                        }

                        VisitorWaitingAdapter visitorWaitingAdapter = new VisitorWaitingAdapter(visitorsArrayList,MainPage.this);
                        recyclerViewVisitorAdapter.setAdapter(visitorWaitingAdapter);

                        Log.e(TAG, "onComplete: visitors size = " + visitorsArrayList.size());

                    }
                });


        //Load Visitor Waiting List End


//check new app start

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        firebaseFirestore.collection(getString(R.string.col_guardApk)).document(getString(R.string.newApkString))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    String appVersionCodeNew = documentSnapshot.getString("versionCode");
                    boolean mustInstall = documentSnapshot.getBoolean("mustInstall");

                    Log.e(TAG, "onComplete: appVersionCodeNew = database " + appVersionCodeNew);
                    Log.e(TAG, "onComplete: appVersionCodeNew = phone version " + appVersion);

                    if (!appVersion.equalsIgnoreCase(appVersionCodeNew)) {

                        final String downloadLink = documentSnapshot.getString("downloadLink");

                        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setCancelable(false);
                        LayoutInflater inflater = getLayoutInflater();
                        View convertView = (View) inflater.inflate(R.layout.show_update_alert, null);

                        Button cancelBtn = convertView.findViewById(R.id.cancelBtn);
                        Button installBtn = convertView.findViewById(R.id.installBtn);

                        if (mustInstall) {
                            cancelBtn.setVisibility(View.GONE);
                            alertDialog.setCancelable(false);
                        }
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        installBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!downloadLink.isEmpty()) {

                                    if (CheckForSDCard.isSDCardPresent()) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            // only for gingerbread and newer versions


                                        }


                                        Log.e(TAG, "onClick: " + downloadLink);


                                        ProgressDialog progressDialog = new ProgressDialog(MainPage.this);
                                        progressDialog.setMessage("Downloading new Apk...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();

                                        DownloadFile downloadFile = new DownloadFile(downloadLink, progressDialog, context);
                                        downloadFile.execute();


                                    } else {
                                        Toast.makeText(context, "No SD CARD", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }
                        });

                        alertDialog.setView(convertView);
                        alertDialog.show();


                    }
                }
            }
        });


//check new app End


        firebaseFirestore.collection(getString(R.string.col_activebuild)).document(buildid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Activebuilding activebuilding = documentSnapshot.toObject(Activebuilding.class);

                        buildingName.setText(activebuilding.getB_name());


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
        blackListRepository = new BlackListRepository(this);
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

                    sWorkerListActivity();


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

        guardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, GuardListActivity.class);
                startActivity(intent);
            }
        });

    }


    private void offerReplacingDefaultDialer(Context context) {

        if (context.getSystemService(TelecomManager.class).getDefaultDialerPackage() != getPackageName()) {
            new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        }
    }


    public void sWorkerListActivity() {

        Intent intent = new Intent(MainPage.this, SWorkersActivity.class);
        startActivity(intent);

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

//                    flatsRepository.deleteTask(buildid);

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

//                    whiteListRepository.deleteTask(buildid);


                } else {
                    Log.d(TAG, "onComplete: xxx5");
                }
            }
        });

    }

    public void getAllBlackListAndSaveToLocalDatabase(final BuildingChanges buildingChanges) {
        //final FlatsRepository flatsRepository = new FlatsRepository(this);


        firebaseFirestore.collection(getString(R.string.col_blackList))
                .whereEqualTo("build_id", buildid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        BlackList blackList= documentSnapshot.toObject(BlackList.class);
                        blackListRepository.deleteBlackList(blackList);
                        blackListRepository.insert(blackList);
                    }


                    Map<String, Object> data = new HashMap<>();
                    ArrayList<String> wldata = new ArrayList<>();
                    wldata = buildingChanges.getBlacklists();
                    wldata.add(thismobileuid);
                    data.put("blackLists", wldata);


                    firebaseFirestore.collection("buildingChanges").document(buildid)
                            .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Log.e(TAG, "onComplete: Black Lsit updated " );
                            Toast.makeText(context, "black list data changed!", Toast.LENGTH_SHORT).show();
                        }
                    });

//                    whiteListRepository.deleteTask(buildid);


                } else {
                    Log.d(TAG, "onComplete: xxx5");
                }
            }
        });

    }

    public void matchanddelete(ArrayList<Vehicle> check, Vehicle vehicle) {
        boolean flag = false;

        for (int i = 0; i < check.size(); i++) {
            if (check.get(i).getVehicle_id().equals(vehicle.getVehicle_id())) {
                //vehiclesRepository.deleteVehicle(vehicle);
                Log.d(TAG, "matchanddelete: hhhh ");
                flag = true;
            }
        }
        if (!flag) vehiclesRepository.deleteVehicle(vehicle);

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

                    Log.d(TAG, "onComplete: kkk " + check);

                    vehiclesRepository.getAllVehicle().observe(MainPage.this, new Observer<List<Vehicle>>() {
                        @Override
                        public void onChanged(@Nullable List<Vehicle> allVehicles) {
                            for (Vehicle vehicle : allVehicles) {
                                matchanddelete(check, vehicle);
                                Log.d(TAG, "onChanged: yyyyy " + check.size());
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


                } else {
                    Log.d(TAG, "onComplete: xxx5");
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseFirestore.getInstance().collection("buildingChanges")
                .document(buildid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    BuildingChanges buildingChanges = documentSnapshot.toObject(BuildingChanges.class);
                    ArrayList<String> flats = buildingChanges.getFlats();
                    ArrayList<String> whitelists = buildingChanges.getWhitelists();
                    ArrayList<String> blacklists= buildingChanges.getBlacklists();
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

                    Log.e(TAG, "onSuccess: imarn blacklist" +blacklists);
                    if (!blacklists.contains(thismobileuid)) {
                        getAllBlackListAndSaveToLocalDatabase(buildingChanges);
                    }

                    if (!vehicles.contains(thismobileuid)) {
                        Log.d("firebase", "Getting new Vehicles data because data is changed or updated");
                        getVehiclesAndSaveToLocalDatabase(buildingChanges);
                    }

                } else {
                    BuildingChanges buildingChanges = new BuildingChanges(new ArrayList<String>(), new ArrayList<String>()
                            ,new ArrayList<String>(), new ArrayList<String>());
                    getAllActiveFlatsAndSaveToLocalDatabase(buildingChanges);
                    getAllWhiteListAndSaveToLocalDatabase(buildingChanges);
                    getAllBlackListAndSaveToLocalDatabase(buildingChanges);
                    getVehiclesAndSaveToLocalDatabase(buildingChanges);
                }
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static class CheckForSDCard {

        //Method to Check If SD Card is mounted or not
        public static boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(

                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }
}
