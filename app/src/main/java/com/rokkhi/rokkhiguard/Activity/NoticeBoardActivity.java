package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout noDataLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        context = this;
        sharedPrefHelper = new SharedPrefHelper(context);

        Map<String, Object> dataPost = new HashMap<>();
        dataPost.put("limit","" );
        dataPost.put("pageId","" );
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole", Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ROLE)));
        dataPost.put("noticeFor", StaticData.NOTICE_GUARDS);
        dataPost.put("buildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("communityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("fromDate", "");
        dataPost.put("toDate", "");


        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getNotice;

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);

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

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);


                        Timber.e("onResponse:  get Notice =   " + response);

                        Gson gson = new Gson();
                        noticeModelClass = gson.fromJson(String.valueOf(response), NoticeModelClass.class);

                        NoticeAdapter noticeAdapter =new NoticeAdapter(noticeModelClass,context);

                        mRecyclerview.setAdapter(noticeAdapter);

                        if (noticeModelClass.getData().size()<1){
                            noDataLinearLayout.setVisibility(View.VISIBLE);
                        }
                        AndroidNetworking.cancelAll();

                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);


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
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container_notice);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        noDataLinearLayout=findViewById(R.id.noDataLayout);

    }
}
