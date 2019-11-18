package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.BuildingChanges;
import com.rokkhi.rokkhiguard.Model.GuardPhone;
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.Model.Whitelist;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.data.FlatsRepository;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;
import com.rokkhi.rokkhiguard.data.WhiteListRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huwi.joldi.abrar.rokkhiguardo.Kotlin.CirclePinField;
import io.grpc.internal.LogExceptionRunnable;


public class DaroanPass extends AppCompatActivity implements View.OnClickListener {

    CirclePinField circlePinField;
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
    private View mRootView;
    Context context;
    AuthUI.IdpConfig phoneConfigWithDefaultNumber;
    private static final int RC_SIGN_IN = 12773;

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String token="";
    String tabpass="";
    String flatid = "", buildid = "", commid = "",userid="";
    TextView homename;


    int flag=0;
    private static final String TAG = "DaroanPass";




    //roomdatabase
    FlatsRepository flatsRepository;
    WhiteListRepository whiteListRepository;
    VehiclesRepository vehiclesRepository;
    String thismobileuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daroan_pass);
        context= DaroanPass.this;
        Log.d(TAG, "onCreate: xxx ");


        circlePinField= findViewById( R.id.circleField);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mRootView = findViewById(R.id.root);
        homename= findViewById(R.id.companyname);
        flatid = sharedPref.getString("flatid", "none");
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");

        firebaseFirestore= FirebaseFirestore.getInstance();
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


        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser==null){
                    gosignpage();
                }



                else {

                    final String phoneno=firebaseUser.getPhoneNumber();
                    final String userid=firebaseUser.getUid();

                    thismobileuid = FirebaseAuth.getInstance().getUid();

                    flatsRepository = new FlatsRepository(DaroanPass.this);
                    whiteListRepository = new WhiteListRepository(DaroanPass.this);
                    vehiclesRepository = new VehiclesRepository(DaroanPass.this);



                    Log.d(TAG, "onAuthStateChanged: ccc10 "+ phoneno);
                    firebaseFirestore.collection(getString(R.string.col_phoneguards)).document(phoneno).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                final DocumentSnapshot documentSnapshot= task.getResult();

                                Log.d(TAG, "onComplete: ck1 ");
                                if(documentSnapshot!=null && documentSnapshot.exists()){
                                    Log.d(TAG, "onComplete: ck2");

                                    GuardPhone guardPhone= documentSnapshot.toObject(GuardPhone.class);
                                    ArrayList<String> arr= guardPhone.getBuild_array();
                                    buildid=arr.get(0);
                                    commid= documentSnapshot.getString("comm_id");
                                    editor.putString("buildid",buildid);
                                    editor.putString("commid",commid);
                                    editor.apply();
                                    token=documentSnapshot.getString("g_token");
                                    tabpass= documentSnapshot.getString("mobilepass");

                                    editor.putString("pass",tabpass);
                                    editor.apply();




                                    firebaseFirestore.collection(getString(R.string.col_build)).document(buildid).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot documentSnapshot1= task.getResult();
                                                        if(documentSnapshot1.exists()){
                                                            String hname= documentSnapshot1.getString("b_name");
                                                            homename.setText(hname);



                                                        }
                                                    }
                                                }
                                            });


                                    firebaseFirestore.collection(getString(R.string.col_community))
                                            .document(commid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot documentSnapshot1= task.getResult();
                                                if(documentSnapshot1.exists()){

                                                    Boolean status= documentSnapshot1.getBoolean("c_status");
                                                    if(status==null || !status){
                                                        Log.d(TAG, "onComplete: ck4");
                                                        mAuth.signOut();
                                                    }
                                                    else{
                                                        //homename.setText(hname);

                                                    }
                                                }
                                            }
                                        }
                                    });






                                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( DaroanPass.this,  new OnSuccessListener<InstanceIdResult>() {
                                        @Override
                                        public void onSuccess(InstanceIdResult instanceIdResult) {

                                            String tempToken=instanceIdResult.getToken();

                                            if(!tempToken.equals(token)){

                                                if(token.isEmpty() || token.equals("none")){
                                                    String phone= firebaseUser.getPhoneNumber();
                                                    String uid= firebaseUser.getUid();

                                                    Normalfunc normalfunc= new Normalfunc();
                                                    normalfunc.addUser(tempToken,phone,uid,"guard","","Guard");


                                                    String topic= buildid;
                                                    topic=topic+"guard"+"android";
                                                    FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });

                                                    String topic1= buildid;
                                                    topic1=topic1+"all"+"android";

                                                    FirebaseMessaging.getInstance().subscribeToTopic(topic1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });


                                                    String topic2= commid.replace("@","_");
                                                    topic2=topic2+"guard"+"android";
                                                    FirebaseMessaging.getInstance().subscribeToTopic(topic2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });

                                                    String topic3= commid.replace("@","_");
                                                    topic3=topic3+"all"+"android";

                                                    FirebaseMessaging.getInstance().subscribeToTopic(topic3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });
                                                }


                                                firebaseFirestore.collection(getString(R.string.col_phoneguards)).document(phoneno).update("g_token",instanceIdResult.getToken()
                                                        ,"activated",true)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(context,"Welcome!",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }



                                        }
                                    });






                                }
                                else{
                                    Log.d(TAG, "onComplete: ck5");
                                    mAuth.signOut();
                                }
                            }
                            else {
                                Log.d(TAG, "onComplete: ck6");
                                Log.d(TAG, "onComplete:  task not success");
                               // mAuth.signOut();
                            }
                        }
                    });




                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };






    }

    private void gosignpage() {
        List<String> whitelistedCountries = new ArrayList<String>();
        whitelistedCountries.add("in");
        whitelistedCountries.add("bd");
        phoneConfigWithDefaultNumber = new AuthUI.IdpConfig.PhoneBuilder()
                .setDefaultCountryIso("bd")
                .setWhitelistedCountries(whitelistedCountries)
                .build();

        signInPhone(mRootView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);

        }
    }
/*


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

*/
/*
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

  */
  /*
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
    */
/*

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

                    vehiclesRepository.getAllVehicle().observe(DaroanPass.this, new Observer<List<Vehicle>>() {
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

*/

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == RESULT_OK) {

            ///eikhane Submit data to buldings change
/*
            Log.e(TAG, "handleSignInResponse: "+buildid);
            Log.e(TAG, "handleSignInResponse: "+thismobileuid);

            Map<String,Object> mm=new HashMap<>();
            mm.put("flats", FieldValue.arrayUnion(thismobileuid)); //for insert array union
            mm.put("vehicles", FieldValue.arrayUnion(thismobileuid));
            mm.put("whitelists", FieldValue.arrayUnion(thismobileuid));

            firebaseFirestore.collection("buildingChanges").document(buildid)
                    .update(mm).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                }
            });*/


        } else {
            if (response == null) {
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }

    }



    public void signInPhone(View view) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(phoneConfigWithDefaultNumber))
                        .build(),
                RC_SIGN_IN);

    }




    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.cross  ){
            if( passtext.length() >0)passtext=passtext.substring(0,passtext.length()-1);
        }
        else if(view.getId() == R.id.clear){
            passtext="";
        }
        else{
            TextView tt= (TextView) view;
            passtext=passtext+ tt.getText().toString();
        }
        circlePinField.setText(passtext);

        if(passtext.length()==5){
            //circlePinField.setText(passtext);
            if(tabpass!=null && tabpass.equals(passtext)){
               // passtext="";
                Intent intent=new Intent(DaroanPass.this,MainPage.class);
                startActivity(intent);
                finish();
            }
            else{
                tabpass=sharedPref.getString("pass","none");
                if(tabpass!=null && tabpass.equals(passtext)){
                    //passtext="";

                    Intent intent=new Intent(DaroanPass.this,MainPage.class);
                    startActivity(intent);
                    finish();
                }

                else {

                    passtext="";
                    Toast.makeText(context,"Wrong passcode!!",Toast.LENGTH_SHORT).show();
                }
            }

            new CountDownTimer(100, 100) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    circlePinField.setText("");
                    passtext="";
                }

            }.start();




            return;
        }
    }




    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: xxx");
        mAuth.addAuthStateListener(mAuthListener);



    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: xxx");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showSnackbar(int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }






}
