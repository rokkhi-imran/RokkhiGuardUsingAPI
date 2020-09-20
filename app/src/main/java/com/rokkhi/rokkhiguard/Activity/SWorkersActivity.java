package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.SWorkerAdapter;
import com.rokkhi.rokkhiguard.Model.api.SWorkerModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SWorkersActivity extends AppCompatActivity implements View.OnClickListener {


    Context context;
    SharedPrefHelper sharedPrefHelper;

    private EditText mSearch;
    private RecyclerView mSWorkerRecyclerViewID;
    private ProgressBar mProgressBar;

    private Button mCreateprofile;

    SWorkerModelClass sWorkerModelClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.activity_sworkers);
        setContentView(R.layout.activity_sworkers);
        context=this;
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCreateprofile = findViewById(R.id.createprofile);
        sharedPrefHelper=new SharedPrefHelper(context);

        AndroidNetworking.initialize(getApplicationContext());


        mCreateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateProfileActivity.class);
                startActivity(intent);
            }
        });


        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("buildingId", "2");
        dataPost.put("communityId", "2");
        dataPost.put("flatId", "");
        dataPost.put("userRoleCode", StaticData.SERVICE_WORKER.toString());

        JSONObject jsonDataPost = new JSONObject(dataPost);

//        JSONArray jsonDataPost=new JSONArray();
//        jsonDataPost.put(dataPost);


        String url = StaticData.baseURL + "" + StaticData.getUsersList;
        String token = sharedPrefHelper.getString(StaticData.KEY_FIREBASE_ID_TOKEN);

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);
        Log.e("TAG", "onCreate: " + token);
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

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG ","onResponse: =   " + response);

                        Gson gson = new Gson();
                        sWorkerModelClass = gson.fromJson(String.valueOf(response), SWorkerModelClass.class);

                        SWorkerAdapter sWorkerAdapter = new SWorkerAdapter(sWorkerModelClass, context);

                        mSWorkerRecyclerViewID.setAdapter(sWorkerAdapter);

                    }

                    @Override
                    public void onError(ANError anError) {

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
        mSearch.setOnClickListener(this);
        mSWorkerRecyclerViewID = (RecyclerView) findViewById(R.id.sWorkerRecyclerViewID);
        mSWorkerRecyclerViewID.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mCreateprofile = (Button) findViewById(R.id.createprofile);
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
