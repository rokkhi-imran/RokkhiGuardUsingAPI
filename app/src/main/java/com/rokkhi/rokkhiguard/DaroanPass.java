package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rokkhi.rokkhiguard.CallerApp.MainActivity;
import com.rokkhi.rokkhiguard.Model.Buildings;
import com.rokkhi.rokkhiguard.Model.GuardPhone;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.data.FlatsRepository;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;
import com.rokkhi.rokkhiguard.data.WhiteListRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import huwi.joldi.abrar.rokkhiguardo.Kotlin.CirclePinField;


public class DaroanPass extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 12773;
    private static final String TAG = "DaroanPass";
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
    String passtext = "";
    Context context;
    AuthUI.IdpConfig phoneConfigWithDefaultNumber;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String token = "";
    String tabpass = "";
    String flatid = "", buildid = "", commid = "", userid = "";
   static TextView homename;
    ArrayAdapter<String> adapter;
    int flag = 0;
    //roomdatabase
    FlatsRepository flatsRepository;
    WhiteListRepository whiteListRepository;
    VehiclesRepository vehiclesRepository;
    String thismobileuid;
    List<String> buildingsName;
    private View mRootView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public  static boolean checked=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daroan_pass);
        context = DaroanPass.this;
        Log.d(TAG, "onCreate: xxx ");
        buildingsName = new ArrayList<>();


        circlePinField = findViewById(R.id.circleField);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mRootView = findViewById(R.id.root);
        homename = findViewById(R.id.buildingname);
        flatid = sharedPref.getString("flatid", "none");
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");

        firebaseFirestore = FirebaseFirestore.getInstance();
        editor = sharedPref.edit();


        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        cross = findViewById(R.id.cross);
        clear = findViewById(R.id.clear);


        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        cross.setOnClickListener(this);
        clear.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    gosignpage();
                } else {

                    final String phoneno = firebaseUser.getPhoneNumber();

                    thismobileuid = FirebaseAuth.getInstance().getUid();

                    flatsRepository = new FlatsRepository(DaroanPass.this);
                    whiteListRepository = new WhiteListRepository(DaroanPass.this);
                    vehiclesRepository = new VehiclesRepository(DaroanPass.this);


                    Log.d(TAG, "onAuthStateChanged: ccc10 " + phoneno);
                    firebaseFirestore.collection(getString(R.string.col_phoneguards)).document(phoneno).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                final DocumentSnapshot documentSnapshot = task.getResult();

                                Log.d(TAG, "onComplete: ck1 ");
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    Log.d(TAG, "onComplete: ck2");

                                    GuardPhone guardPhone = documentSnapshot.toObject(GuardPhone.class);
                                    ArrayList<String> arr = guardPhone.getBuild_array();

                                    if (arr.size()==1){
                                        checked=true;
                                    }

                                    buildid = arr.get(0);
                                    commid = documentSnapshot.getString("comm_id");
                                    editor.putString("buildid", buildid);
                                    editor.putString("commid", commid);
                                    editor.apply();
                                    token = documentSnapshot.getString("g_token");
                                    tabpass = documentSnapshot.getString("mobilepass");

                                    editor.putString("pass", tabpass);
                                    editor.apply();

                                    if (arr.size()==1){

                                        firebaseFirestore.collection(getString(R.string.col_build)).document(buildid).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot documentSnapshot1 = task.getResult();
                                                            if (documentSnapshot1.exists()) {
                                                                String hname = documentSnapshot1.getString("b_name");
                                                                homename.setText(hname+" (Selected)");


                                                            }
                                                        }
                                                    }
                                                });

                                    }



                                    firebaseFirestore.collection(getString(R.string.col_community))
                                            .document(commid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot1 = task.getResult();
                                                if (documentSnapshot1.exists()) {

                                                    Boolean status = documentSnapshot1.getBoolean("c_status");
                                                    if (status == null || !status) {
                                                        Log.d(TAG, "onComplete: ck4");
                                                        mAuth.signOut();
                                                    } else {
                                                        //homename.setText(hname);

                                                    }
                                                }
                                            }
                                        }
                                    });


                                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(DaroanPass.this, new OnSuccessListener<InstanceIdResult>() {
                                        @Override
                                        public void onSuccess(InstanceIdResult instanceIdResult) {

                                            String tempToken = instanceIdResult.getToken();

                                            if (!tempToken.equals(token)) {

                                                if (token.isEmpty() || token.equals("none")) {
                                                    String phone = firebaseUser.getPhoneNumber();
                                                    String uid = firebaseUser.getUid();

                                                    Normalfunc normalfunc = new Normalfunc();
                                                    normalfunc.addUser(tempToken, phone, uid, "guard", "", "Guard");


                                                    String topic = buildid;
                                                    topic = topic + "guard" + "android";
                                                    FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });

                                                    String topic1 = buildid;
                                                    topic1 = topic1 + "all" + "android";

                                                    FirebaseMessaging.getInstance().subscribeToTopic(topic1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });


                                                    String topic2 = commid.replace("@", "_");
                                                    topic2 = topic2 + "guard" + "android";
                                                    FirebaseMessaging.getInstance().subscribeToTopic(topic2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });

                                                    String topic3 = commid.replace("@", "_");
                                                    topic3 = topic3 + "all" + "android";

                                                    FirebaseMessaging.getInstance().subscribeToTopic(topic3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });
                                                }


                                                firebaseFirestore.collection(getString(R.string.col_phoneguards)).document(phoneno).update("g_token", instanceIdResult.getToken()
                                                        , "activated", true)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }


                                        }
                                    });


                                } else {
                                    Log.d(TAG, "onComplete: ck5");
                                    mAuth.signOut();
                                }
                            } else {
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


//set on click

        homename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBuildingsNameList(context);
            }
        });


    }

    private void callBuildingsNameList(final Context context) {
        String phoneNumebr;
        if (firebaseUser != null) {
            phoneNumebr = firebaseUser.getPhoneNumber();


            firebaseFirestore.collection(getString(R.string.col_phoneguards)).document(phoneNumebr).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                    if (task.isSuccessful()) {
                        final DocumentSnapshot documentSnapshot = task.getResult();

                        Log.d(TAG, "onComplete: ck1 ");
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Log.d(TAG, "onComplete: ck2");

                            GuardPhone guardPhone = documentSnapshot.toObject(GuardPhone.class);
                            ArrayList<String> arr = guardPhone.getBuild_array();


                            if (arr.size()>1){

                                showAddressAlert(arr, context);

                            }


                        }
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "failed to laod data", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void showAddressAlert(List<String> list, final Context context) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.alert_dialog_adress, null);


        RecyclerView recyclerList = rowList.findViewById(R.id.buildingnaemList);

        ProgressBar progressBar=rowList.findViewById(R.id.progressBar);

        recyclerList.setLayoutManager(new LinearLayoutManager(context));



        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();


        BuildingNameAdapter adapter = new BuildingNameAdapter(context, list,dialog);

        recyclerList.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

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
    protected void onRestart() {
        checked=false;
        super.onRestart();
    }

    @Override
    protected void onPause() {
        checked=false;
        super.onPause();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.cross) {
            if (passtext.length() > 0) passtext = passtext.substring(0, passtext.length() - 1);
        } else if (view.getId() == R.id.clear) {
            passtext = "";
        } else {
            TextView tt = (TextView) view;
            passtext = passtext + tt.getText().toString();
        }
        circlePinField.setText(passtext);

        if (passtext.length() == 5) {
            //circlePinField.setText(passtext);
            if (tabpass != null && tabpass.equals(passtext)) {
                // passtext="";


                if (DaroanPass.checked){

                    Intent intent = new Intent(DaroanPass.this, MainPage.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Select building", Toast.LENGTH_SHORT).show();
                }
            } else {
                tabpass = sharedPref.getString("pass", "none");
                if (tabpass != null && tabpass.equals(passtext)) {
                    //passtext="";

                    Intent intent = new Intent(DaroanPass.this, MainPage.class);
                    startActivity(intent);
                    finish();
                } else {

                    passtext = "";
                    Toast.makeText(context, "Wrong passcode!!", Toast.LENGTH_SHORT).show();
                }
            }

            new CountDownTimer(100, 100) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    circlePinField.setText("");
                    passtext = "";
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


    private static class BuildingNameAdapter extends RecyclerView.Adapter<BuildingNameAdapter.ListViewHolder>{

            Context context;
            List<String> list;
            FirebaseFirestore firebaseFirestore;
            AlertDialog alertDialog;


            //for onClick from java class (Second ....)
            private ClickListener clickListener;
            SharedPreferences sharedPreferences;
            SharedPreferences.Editor editor;


            public BuildingNameAdapter(Context context, List<String> list, AlertDialog dialog) {
                firebaseFirestore=FirebaseFirestore.getInstance();
                this.context = context;
                this.list = list;
                this.alertDialog=dialog;

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                editor = sharedPreferences.edit();
            }

            @NonNull
            @Override
            public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_building_name, parent, false);

                ListViewHolder listViewHolder=new ListViewHolder(view);

                return listViewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {

                Log.e("TAG", "onBindViewHolder: ID =  "+list.get(position) );

                Log.e(TAG, "onBindViewHolder: build id  =  "+list.get(position) );

                firebaseFirestore.collection(context.getString(R.string.col_build))
                        .whereEqualTo("build_id",list.get(position))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){
                            for (DocumentSnapshot snapshot:task.getResult()){

                                Buildings buildings = (Buildings) snapshot.toObject(Buildings.class);
                                holder.buildingName.setText(buildings.getB_name());
                                holder.buildingIDTV.setText(buildings.getBuild_id());
//                                holder.progressBar.setVisibility(View.GONE);
                            }
                        }else {
//                            holder.progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "Data not loaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

                TextView buildingName ;
                TextView buildingIDTV ;
//                ProgressBar progressBar;
                public View view;

                ListViewHolder(View itemView) {
                    super(itemView);
                    view = itemView;
                    buildingName = view.findViewById(R.id.buildingNameTV);
                    buildingIDTV =view.findViewById(R.id.buildingIDTV);
//                    progressBar =view.findViewById(R.id.progressBar);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                            DaroanPass.homename.setText(buildingName.getText().toString());

                            String buildID=buildingIDTV.getText().toString();
                            Log.e(TAG, "onClick: buildID = "+buildID);

                            //save buil ID in sharedpreferance

                            editor.putString("buildid", buildID);
                            editor.apply();
                            checked=true;

                        }
                    });

                }

                @Override
                public void onClick(View v) {

                    clickListener.onItemClick(getAdapterPosition(), v, buildingName.getText().toString(),buildingIDTV.getText().toString());

                }

                @Override
                public boolean onLongClick(View v) {

                    clickListener.onItemLongClick(getAdapterPosition(), v);

                    return false;
                }
            }

            public interface ClickListener {
                void onItemClick(int position, View v, String s, String text);
                void onItemLongClick(int position, View v);
            }

    }
}
