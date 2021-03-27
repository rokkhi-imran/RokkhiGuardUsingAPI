package com.rokkhi.rokkhiguard.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Model.api.GetUserByPhoneData;
import com.rokkhi.rokkhiguard.Model.api.GetUserByPhoneNumberModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huwi.joldi.abrar.rokkhiguardo.Kotlin.CirclePinField;


public class DaroanPassActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DaroanPassActivity";
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

    String tabpass = "";

    List<String> buildingsName;

    SharedPrefHelper sharedPrefHelper;
    FullScreenAlertDialog fullScreenAlertDialog;

    GetUserByPhoneData getUserByPhoneData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daroan_pass);
        context = DaroanPassActivity.this;
        sharedPrefHelper = new SharedPrefHelper(context);

        Gson gson = new Gson();
        getUserByPhoneData = gson.fromJson(getIntent().getStringExtra("guardinfo"), GetUserByPhoneData.class);

        Log.e(TAG, "onCreate: getUserByPhoneData = " + getUserByPhoneData);


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


        fullScreenAlertDialog = new FullScreenAlertDialog(context);

        fullScreenAlertDialog.showdialog();
        callUserInformation(fullScreenAlertDialog, sharedPrefHelper.getString(StaticData.JWT_TOKEN));


    }

    private void callUserInformation(FullScreenAlertDialog fullScreenAlertDialog, String jWTToken) {


        Map<String, Object> dataPost = new HashMap<>();
        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole", 1);
        dataPost.put("phoneNumber", getUserByPhoneData.getPhone());

        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getByPhoneNumber;

        Log.e(TAG, "onCreate: callUserInformation jsonDataPost 1 = " + jsonDataPost);
        Log.e(TAG, "onCreate:  callUserInformation 1 = " + url);
        Log.e(TAG, "onCreate: callUserInformation jWTToken 1 =" + jWTToken);
        Log.e(TAG, "onCreate: callUserInformation 1 =" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        Log.e(TAG, "onCreate: ---------------------- ");


        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);

        AndroidNetworking.post(url)
                .addHeaders("jwtTokenHeader", jWTToken)
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fullScreenAlertDialog.dismissdialog();


                        Log.e("TAG ", "onResponse: =   " + response);

                        Gson gson = new Gson();
                        GetUserByPhoneNumberModelClass userDetailsModelClassUserByPhoneNumberModelClass = gson.fromJson(String.valueOf(response), GetUserByPhoneNumberModelClass.class);

                        sharedPrefHelper.putString(StaticData.BUILD_ID, String.valueOf(userDetailsModelClassUserByPhoneNumberModelClass.getData().getBuilding().getId()));
                        sharedPrefHelper.putString(StaticData.COMM_ID, String.valueOf(userDetailsModelClassUserByPhoneNumberModelClass.getData().getCommunity().getId()));
                        sharedPrefHelper.putString(StaticData.USER_ID, String.valueOf(userDetailsModelClassUserByPhoneNumberModelClass.getData().getId()));

                        homename.setText(userDetailsModelClassUserByPhoneNumberModelClass.getData().getBuilding().getName());

                        sharedPrefHelper.putString(StaticData.BUILD_NAME, userDetailsModelClassUserByPhoneNumberModelClass.getData().getBuilding().getName());
                        sharedPrefHelper.putString(StaticData.BUILD_ADDRESS, userDetailsModelClassUserByPhoneNumberModelClass.getData().getBuilding().getAddress());


                        Log.e(TAG, "onResponse: getPrimaryRoleCode  = ==  " + userDetailsModelClassUserByPhoneNumberModelClass.getData().getPrimaryRoleCode());


                        AndroidNetworking.cancelAll();
                    }

                    @Override
                    public void onError(ANError anError) {
                        fullScreenAlertDialog.dismissdialog();


                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

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
    //check storage Permission End

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

            tabpass = getUserByPhoneData.getPassword();
            if (tabpass != null && tabpass.equals(passtext)) {
                Intent intent = new Intent(DaroanPassActivity.this, MainPageActivity.class);
                startActivity(intent);
                finish();
            } else {

                passtext = "";
                Toast.makeText(context, "Wrong pass code!!", Toast.LENGTH_SHORT).show();

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


}
