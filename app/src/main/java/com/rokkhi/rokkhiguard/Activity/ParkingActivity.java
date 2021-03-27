package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.CarListAdapter;
import com.rokkhi.rokkhiguard.Model.api.VehicleData;
import com.rokkhi.rokkhiguard.Model.api.VehicleListModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ParkingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ParkingActivity";


    private EditText mSearch;
    private RecyclerView mRecyclerview;
    private ProgressBar mProgressBar1;
    SharedPrefHelper sharedPrefHelper;

    CarListAdapter carListAdapter;
    Context context;
    LinearLayout noDataLinearLayout;

    VehicleListModelClass vehicleListModelClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Map<String, Object> dataPost = new HashMap<>();
        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole", 1);
        dataPost.put("vehicleType", "");
        dataPost.put("buildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("flatId", 0);
        dataPost.put("communityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        JSONObject jsonDataPost = new JSONObject(dataPost);



        String url = StaticData.baseURL + "" + StaticData.getVehicles;

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);
//        Log.e("TAG", "onCreate: " + token);
        Log.e("TAG", "onCreate: ---------------------- ");


        AndroidNetworking.post(url)
                .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        Log.e(TAG, "onResponse: =  =----------- " + response);

                        Gson gson = new Gson();
                        vehicleListModelClass = gson.fromJson(String.valueOf(response), VehicleListModelClass.class);

                        mProgressBar1.setVisibility(View.GONE);


                        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);

                        mRecyclerview.setLayoutManager(layoutManager);


                        carListAdapter = new CarListAdapter((ArrayList<VehicleData>) vehicleListModelClass.getData(),context);
                        carListAdapter.setHasStableIds(true);
                        mRecyclerview.setAdapter(carListAdapter);
                        if (vehicleListModelClass.getData().size()<1){
                            noDataLinearLayout.setVisibility(View.VISIBLE);
                        }
                        AndroidNetworking.cancelAll();

                    }

                    @Override
                    public void onError(ANError anError) {


                        StaticData.showErrorAlertDialog(context,"Alert !","আবার চেষ্টা করুন ।");

                        Log.e(TAG, "onResponse: error message =  " + anError.getMessage());
                        Log.e(TAG, "onResponse: error code =  " + anError.getErrorCode());
                        Log.e(TAG, "onResponse: error body =  " + anError.getErrorBody());
                        Log.e(TAG, "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });




    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initView() {
        mSearch = (EditText) findViewById(R.id.search);
        mSearch.setOnClickListener(this);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        sharedPrefHelper=new SharedPrefHelper(getApplicationContext());

        context=ParkingActivity.this;
        noDataLinearLayout=findViewById(R.id.noDataLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.search:
                break;
        }
    }
}
