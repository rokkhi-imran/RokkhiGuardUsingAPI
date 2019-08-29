package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rokkhi.rokkhiguard.Model.GuardPhone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import huwi.joldi.abrar.rokkhiguardo.Kotlin.CirclePinField;


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

//        firebaseFirestore.collection(getString(R.string.col_activeflat)).whereEqualTo("build_id","c7niuI2ozGcNAJLXpA1b")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    WriteBatch batch=firebaseFirestore.batch();
//                    for(DocumentSnapshot documentSnapshot: task.getResult()){
//                        ActiveFlats activeFlats= documentSnapshot.toObject(ActiveFlats.class);
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("flat_id", activeFlats.getFlat_id());
//                        data.put("build_id", activeFlats.getBuild_id());
//                        data.put("f_no", activeFlats.getF_no());
//                        data.put("vacant",activeFlats.isVacant());
//                        data.put("lastTime", FieldValue.serverTimestamp());
//                        data.put("beforeLastTime", FieldValue.serverTimestamp());
//                        data.put("vehicle_array", new ArrayList<>());
//                        DocumentReference parking= firebaseFirestore.collection(getString(R.string.col_parkings)).document(activeFlats.getFlat_id());
//                        batch.set(parking, data);
//                    }
//
//                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            }
//        });

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


                                                            String topic= buildid;
                                                            topic=topic+"guard";
                                                            FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                }
                                                            });

                                                            String topic1= buildid;
                                                            topic1=topic1+"all";

                                                            FirebaseMessaging.getInstance().subscribeToTopic(topic1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                }
                                                            });
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
                                                        String topic= commid.replace("@","_");
                                                        topic=topic+"guard";
                                                        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        });

                                                        String topic1= commid.replace("@","_");
                                                        topic1=topic1+"all";

                                                        FirebaseMessaging.getInstance().subscribeToTopic(topic1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    });






                                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( DaroanPass.this,  new OnSuccessListener<InstanceIdResult>() {
                                        @Override
                                        public void onSuccess(InstanceIdResult instanceIdResult) {

                                            String tempToken=instanceIdResult.getToken();

                                            if(!tempToken.equals(token))firebaseFirestore.collection(getString(R.string.col_phoneguards)).document(phoneno).update("g_token",instanceIdResult.getToken()
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

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == RESULT_OK) {
            //startActivity(openstartingpoint);
            // populateProfile();
           // checkUser();
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
