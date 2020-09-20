package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.rokkhi.rokkhiguard.Adapter.NoticeAdapter;
import com.rokkhi.rokkhiguard.Model.api.NoticeModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class NoticeBoardActivity extends AppCompatActivity {

    Context context;

    NoticeModelClass noticeModelClass;

    SharedPrefHelper sharedPrefHelper;
    private RecyclerView mRecyclerview;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        sharedPrefHelper = new SharedPrefHelper(context);

        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("noticeFor", "");
        dataPost.put("buildingId", "2");
        dataPost.put("communityId", "2");
        dataPost.put("fromDate", "");
        dataPost.put("toDate", "");

        JSONObject jsonDataPost = new JSONObject(dataPost);

//        JSONArray jsonDataPost=new JSONArray();
//        jsonDataPost.put(dataPost);


        String url = StaticData.baseURL + "" + StaticData.getNotice;
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

                        Timber.e("onResponse: =   " + response);

                        Gson gson = new Gson();
                        noticeModelClass = gson.fromJson(String.valueOf(response), NoticeModelClass.class);

                        NoticeAdapter noticeAdapter =new NoticeAdapter(noticeModelClass,context);

                        mRecyclerview.setAdapter(noticeAdapter);

                    }

                    @Override
                    public void onError(ANError anError) {

                        mProgressBar.setVisibility(View.GONE);

                        StaticData.showErrorAlertDialog(context,"Alert !","আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });

    }


    private void initView() {
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

    }
}
