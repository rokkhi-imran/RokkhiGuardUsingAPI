package com.rokkhi.rokkhiguard.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Model.api.UserDetailsModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import huwi.joldi.abrar.rokkhiguardo.Kotlin.CirclePinField;


public class DaroanPassActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 12773;
    private static final String TAG = "DaroanPass";
    public static boolean checked = false;
    static TextView homename;
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
    SharedPreferences.Editor editor;
    String tabpass = "";
    String flatid = "", buildid = "", commid = "", userid = "";
    ArrayAdapter<String> adapter;

    List<String> buildingsName;
    private View mRootView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPrefHelper sharedPrefHelper;
    FullScreenAlertDialog fullScreenAlertDialog;

    UserDetailsModelClass userDetailsData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daroan_pass);
        context = DaroanPassActivity.this;
        sharedPrefHelper=new SharedPrefHelper(context);


        sharedPrefHelper.putString(StaticData.BUILD_ID,"2");
        sharedPrefHelper.putString(StaticData.COMM_ID,"2");
        sharedPrefHelper.putString(StaticData.USER_ID,"1");

        //check Stroage permission Start
        if (!checkPermissionForWriteExtertalStorage(this)) {
            try {
                requestPermissionForWriteExtertalStorage(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!checkPermissionForReadExtertalStorage(this)) {
            try {
                requestPermissionForReadeExtertalStorage(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        Log.d(TAG, "onCreate: xxx ");
        buildingsName = new ArrayList<>();


        circlePinField = findViewById(R.id.circleField);
        mRootView = findViewById(R.id.root);
        homename = findViewById(R.id.buildingname);


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
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    gosignpage();
                } else {

                    callTokenID();

                }
            }
        };


    }

    private void callTokenID() {
        fullScreenAlertDialog=new FullScreenAlertDialog(context);

        fullScreenAlertDialog.showdialog();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                Log.e("TAG", "onSuccess: " + getTokenResult.getToken());

                SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);
                sharedPrefHelper.putString(StaticData.KEY_FIREBASE_ID_TOKEN, getTokenResult.getToken());

                getUserDetailsFunction(getTokenResult.getToken());

            }
        });
    }

    private void getUserDetailsFunction(String token) {




        String url = StaticData.baseURL + "" + StaticData.getUserDetails;

        Log.e("TAG", "onCreate: " + url);
        Log.e("TAG", "onCreate: " + token);
        Log.e("TAG", "onCreate: ---------------------- ");


        AndroidNetworking.post(url)
                .addHeaders("authtoken", token)
                .setContentType("application/json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fullScreenAlertDialog.dismissdialog();


                        Log.e(TAG, "onResponse: =  =----------- " + response);

                        Gson gson = new Gson();
                        userDetailsData = gson.fromJson(String.valueOf(response), UserDetailsModelClass.class);

                        if (!userDetailsData.getData().getPrimaryRoleCode().equals(StaticData.GUARD)){

                            StaticData.showSuccessDialog((FragmentActivity) context,"Alert !"," আপনার এই অ্যাপ টি ব্যাবহার করার অনুমতি নাই। ");

                        }



                    }

                    @Override
                    public void onError(ANError anError) {

                        fullScreenAlertDialog.dismissdialog();

                        StaticData.showErrorAlertDialog(context,"Alert !","আবার চেষ্টা করুন ।");

                        Log.e(TAG, "onResponse: error message =  " + anError.getMessage());
                        Log.e(TAG, "onResponse: error code =  " + anError.getErrorCode());
                        Log.e(TAG, "onResponse: error body =  " + anError.getErrorBody());
                        Log.e(TAG, "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });




    }

    //check stroage Permission Start

    public boolean checkPermissionForReadExtertalStorage(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public boolean checkPermissionForWriteExtertalStorage(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForWriteExtertalStorage(Context context) throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void requestPermissionForReadeExtertalStorage(Context context) throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //check stroage Permission End


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
        checked = false;
        super.onRestart();
    }

    @Override
    protected void onPause() {
        checked = false;
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


                if (DaroanPassActivity.checked) {

                    Intent intent = new Intent(DaroanPassActivity.this, MainPageActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Select building", Toast.LENGTH_SHORT).show();
                }
            } else {
//                tabpass = sharedPref.getString("pass", "none");
                if (tabpass != null && tabpass.equals(passtext)) {
                    //passtext="";

                    Intent intent = new Intent(DaroanPassActivity.this, MainPageActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    passtext = "";
                    Toast.makeText(context, "Wrong passcode!!", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(DaroanPassActivity.this, MainPageActivity.class);
                    startActivity(intent);
                    finish();
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



}
