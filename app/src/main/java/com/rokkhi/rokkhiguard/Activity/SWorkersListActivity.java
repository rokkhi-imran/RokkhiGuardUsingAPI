package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.SWorkerAdapter;
import com.rokkhi.rokkhiguard.Model.api.SWorkerModelClass;
import com.rokkhi.rokkhiguard.Model.api.SworkerData;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SWorkersListActivity extends AppCompatActivity  {


    Context context;
    SharedPrefHelper sharedPrefHelper;

    private EditText mSearch;
    private RecyclerView mSWorkerRecyclerViewID;
    private ProgressBar mProgressBar;

    private Button mCreateprofile;

    SWorkerModelClass sWorkerModelClass;
    SWorkerAdapter sWorkerAdapter;
    ShimmerFrameLayout shimmerFrameLayout;

    LinearLayout noDataLinearLayout;


    @Override
    protected void onPostResume() {
        super.onPostResume();

        //call inside this method because when add user and return ,  that time call this function
        callWorkerList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sworkers);
        context=this;
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCreateprofile = findViewById(R.id.createprofile);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container_service_Worker);
        shimmerFrameLayout.setVisibility(View.VISIBLE);



        mCreateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateProfileActivity.class);
                startActivity(intent);
            }
        });


        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sWorkerAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void callWorkerList() {

        sharedPrefHelper=new SharedPrefHelper(context);


        AndroidNetworking.initialize(getApplicationContext());
        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("buildingId", sharedPrefHelper.getString(StaticData.BUILD_ID));
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("flatId", "");
        dataPost.put("userRoleCode", StaticData.SERVICE_WORKER.toString());

        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getUsersList;

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

                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG ","onResponse: =   " + response);

                        Gson gson = new Gson();
                        sWorkerModelClass = gson.fromJson(String.valueOf(response), SWorkerModelClass.class);

                        sWorkerAdapter = new SWorkerAdapter((ArrayList<SworkerData>) sWorkerModelClass.getData(), context);

                        mSWorkerRecyclerViewID.setAdapter(sWorkerAdapter);

                        if (sWorkerModelClass.getData().size()<1){
                            noDataLinearLayout.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();



                        mProgressBar.setVisibility(View.GONE);

                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });


    }


    private void initView() {
        mSearch = (EditText) findViewById(R.id.search);
        mSWorkerRecyclerViewID = (RecyclerView) findViewById(R.id.sWorkerRecyclerViewID);
        mSWorkerRecyclerViewID.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mCreateprofile = (Button) findViewById(R.id.createprofile);


        mSWorkerRecyclerViewID.setHasFixedSize(true);
        mSWorkerRecyclerViewID.setItemViewCacheSize(20);
        mSWorkerRecyclerViewID.setDrawingCacheEnabled(true);
        mSWorkerRecyclerViewID.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        noDataLinearLayout=findViewById(R.id.noDataLayout);

    }

}
