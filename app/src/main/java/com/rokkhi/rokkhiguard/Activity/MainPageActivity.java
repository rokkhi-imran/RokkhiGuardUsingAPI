package com.rokkhi.rokkhiguard.Activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Model.api.AllFlatsModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainPageActivity extends AppCompatActivity {

    private static final String TAG = "MainPageActivity";
    CircleImageView logout, addvis, vislist, notice, parcel, create, vehicle,
            child;
    Context context;
    ImageButton settings;



    ArrayList<Visitors> visitorsArrayList;

    SharedPrefHelper sharedPrefHelper;
    FullScreenAlertDialog fullScreenAlertDialog;

    TextView buildNameTV;
    TextView buildAddressTV;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        context = MainPageActivity.this;
        fullScreenAlertDialog = new FullScreenAlertDialog(context);

        sharedPrefHelper = new SharedPrefHelper(context);

        visitorsArrayList = new ArrayList<>();
        logout = findViewById(R.id.logout);
        addvis = findViewById(R.id.addvis);
        vislist = findViewById(R.id.vislist);
        notice = findViewById(R.id.notice);
        parcel = findViewById(R.id.parcel);
        create = findViewById(R.id.profile);
        settings = findViewById(R.id.settings);
        vehicle = findViewById(R.id.vehicleET);
        child = findViewById(R.id.child);

        buildNameTV=findViewById(R.id.buildingNameTV);
        buildAddressTV=findViewById(R.id.buildAddressV);

        buildNameTV.setText(sharedPrefHelper.getString(StaticData.BUILD_NAME));
        buildAddressTV.setText(sharedPrefHelper.getString(StaticData.BUILD_ADDRESS));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);


        //set default caller

        offerReplacingDefaultDialer(context);

        //check the call permission
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
            requestPermissions(permissions, 1);
        }




        callFlatList();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, GuardListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

                Intent intent = new Intent(context, ChildrenListActivity.class);
                startActivity(intent);
            }
        });


        addvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, AddVisitorActivity.class);
                startActivity(intent);
            }
        });
        vislist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, VisitorsListActivity.class);
                startActivity(intent);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, NoticeBoardActivity.class);
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
                    Intent intent = new Intent(MainPageActivity.this, GuardListActivity.class);
                    startActivity(intent);
                }


            }
        });
        parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, ParcelActivity.class);
                startActivity(intent);

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void callFlatList() {
        fullScreenAlertDialog.showdialog();

        Map<String, Object> dataPost = new HashMap<>();
        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole", Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ROLE)));
        dataPost.put("buildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        JSONObject jsonDataPost = new JSONObject(dataPost);

        String getFlatsUrl = StaticData.baseURL + "" + StaticData.getFlats;

        Log.e(TAG, "onCreate: " + jsonDataPost);
        Log.e(TAG, "onCreate: " + getFlatsUrl);
        Log.e(TAG, "onCreate: JWT Token =- " + sharedPrefHelper.getString(StaticData.JWT_TOKEN));

        Log.e(TAG, "onCreate: " + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        Log.e(TAG, "onCreate: ---------------------- ");


        AndroidNetworking.post(getFlatsUrl)
                .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
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
                        AllFlatsModelClass allFlatsModelClass = gson.fromJson(String.valueOf(response), AllFlatsModelClass.class);

                        String jsonFlats = gson.toJson(allFlatsModelClass);

                        sharedPrefHelper.putString(StaticData.ALL_FLATS, jsonFlats);

                        AndroidNetworking.cancelAll();
                    }
                    @Override
                    public void onError(ANError anError) {
                        fullScreenAlertDialog.dismissdialog();

                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
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

        Intent intent = new Intent(MainPageActivity.this, SWorkersListActivity.class);
        startActivity(intent);

    }




    @Override
    protected void onStart() {
        super.onStart();

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
