package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.GuardListAdapter;
import com.rokkhi.rokkhiguard.Model.api.GuardListData;
import com.rokkhi.rokkhiguard.Model.api.GuardListModelClass;
import com.rokkhi.rokkhiguard.Model.api.UserDetailsModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuardListActivity extends AppCompatActivity {

    Context context;
    ProgressBar mProgressbar;
    RecyclerView mGuardListRecyclerView;
    SharedPrefHelper sharedPrefHelper;

    GuardListModelClass guardListModelClass;
    GuardListAdapter guardListAdapter;

    private static final int RC_SIGN_IN = 12773;

    AuthUI.IdpConfig phoneConfigWithDefaultNumber;
    FirebaseUser firebaseUser;

    private View mRootView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Normalfunc normalfunc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_list);
        context = this;
        mRootView = findViewById(R.id.root);
        sharedPrefHelper = new SharedPrefHelper(context);
        mProgressbar = findViewById(R.id.progressbar);
        mGuardListRecyclerView = findViewById(R.id.guardListRecyclerView);
        normalfunc = new Normalfunc();

        //get system alert window permission
        if (!Settings.canDrawOverlays(context)) {

            appearOnTheTopAlert();

        } else {
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {

                    sharedPrefHelper.clearAllData();
                    goSignInPage();
                } else {
                    mProgressbar.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        @Override
                        public void onSuccess(GetTokenResult getTokenResult) {

                            Log.e("TAG", "onSuccess: " + getTokenResult.getToken());



                            callUserDetails(getTokenResult.getToken());

                        }
                    });


                }
            }
        };


    }

    private void appearOnTheTopAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Alert !");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Allow display overlay to run over other apps");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                StroagePermission.checkDrawOverlayPermission(GuardListActivity.this, StaticData.REQUEST_FOR_APPEAR_ON_TOP_CODE);

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void callGuardList(String token) {

        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("buildingId", sharedPrefHelper.getString(StaticData.BUILD_ID));
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("flatId", "");
        dataPost.put("userRoleCode", StaticData.GUARD.toString());

        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getUsersList;

        Log.e("TAG", "onCreate: Guard List " + jsonDataPost);
        Log.e("TAG", "onCreate: Guard List " + url);
        Log.e("TAG", "onCreate: Guard List " + token);
        Log.e("TAG", "onCreate: ---------------------- ");


        AndroidNetworking.post(url)
                .addHeaders("authtoken", token)
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        mProgressbar.setVisibility(View.GONE);

                        Log.e("TAG ", "onResponse: =   " + response);

                        Gson gson = new Gson();
                        guardListModelClass = gson.fromJson(String.valueOf(response), GuardListModelClass.class);


                        guardListAdapter = new GuardListAdapter((ArrayList<GuardListData>) guardListModelClass.getData(), context);

                        mGuardListRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                        mGuardListRecyclerView.setAdapter(guardListAdapter);

                    }

                    @Override
                    public void onError(ANError anError) {

                        mProgressbar.setVisibility(View.GONE);

                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });


    }



    private void goSignInPage() {
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

        if (requestCode == StaticData.REQUEST_FOR_APPEAR_ON_TOP_CODE) {

            if (!Settings.canDrawOverlays(this)) {

                    appearOnTheTopAlert();
                } else {

                }

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

    private void callUserDetails(String token) {

        Log.e("TAG", "onSuccess: first time call Function: ");

                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {

                        String deviceToken = instanceIdResult.getToken();
                        Log.e("TAG", "token ID onSuccess first time : Device Token =  " + deviceToken);
                        Log.e("TAG", "token ID onSuccess first time : auth Token  =  " + token);

                        Map<String, String> dataPost = new HashMap<>();
                        dataPost.put("requesterFirebaseId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataPost.put("requesterProfileId", "");
                        dataPost.put("limit", "");
                        dataPost.put("pageId", "");
                        dataPost.put("communityId", "");
                        dataPost.put("deviceToken", deviceToken);
                        dataPost.put("deviceType", "Android");

                        JSONObject jsonDataPost = new JSONObject(dataPost);

                        String url = StaticData.baseURL + "" + StaticData.getUserDetails;

                        Log.e("TAG", "onCreate: Call User List Json Data : " + jsonDataPost);
                        Log.e("TAG", "onCreate: Call user List Url " + url);
                        Log.e("TAG", "onCreate: ---------------------- ");

                        AndroidNetworking.post(url)
                                .addHeaders("authtoken", token)
                                .setContentType("application/json")
                                .addJSONObjectBody(jsonDataPost)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        Log.e("TAG", "token ID onSuccess first time : response =  " + response);

                                        Gson gson = new Gson();
                                        UserDetailsModelClass userDetailsModelClass = gson.fromJson(String.valueOf(response), UserDetailsModelClass.class);

                                        if (!userDetailsModelClass.getData().getPrimaryRoleCode().equals(StaticData.GUARD_PHONE.toString())){
                                            showAlertDialog(context);
                                        }else {

                                            sharedPrefHelper.putString(StaticData.BUILD_ID, String.valueOf(userDetailsModelClass.getData().getBuildingId()));
                                            sharedPrefHelper.putString(StaticData.COMM_ID, String.valueOf(userDetailsModelClass.getData().getCommunityId()));

                                            callGuardList(token);
                                        }


                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        mProgressbar.setVisibility(View.GONE);


                                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                                    }
                                });

                    }
                });




    }



    private void showAlertDialog(Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_dialog_not_use_this_app, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Alert !");
        alertDialog.setCancelable(false);
        Button logoutBtn = convertView.findViewById(R.id.logoutBtn);
        Button knowMoreBtn = convertView.findViewById(R.id.knowMore);

        logoutBtn.setOnClickListener(v -> {

            alertDialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(context, GuardListActivity.class);
            startActivity(intent);
            finish();


        });

        knowMoreBtn.setOnClickListener(view -> {

            alertDialog.dismiss();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.rokkhi.com/"));
            startActivity(browserIntent);
        });

        alertDialog.show();


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
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG", "onStart: xxx");
        mAuth.addAuthStateListener(mAuthListener);


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop: xxx");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showSnackbar(int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }


}
