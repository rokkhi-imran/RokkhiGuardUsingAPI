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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.VisitorAdapter;
import com.rokkhi.rokkhiguard.Model.api.GetInsideVisitorData;
import com.rokkhi.rokkhiguard.Model.api.GetVisitorInsideModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class VisitorsListActivity extends AppCompatActivity  {

    private static final String TAG = "VisitorsListActivity";
    RecyclerView recyclerView;
    VisitorAdapter visitorAdapter;
    View mrootView;
    ProgressBar progressBar;
    Context context;
    EditText search;

    NestedScrollView myNestedScroll;
    Date low,high;

    SharedPrefHelper sharedPrefHelper;
    GetVisitorInsideModelClass getVisitorInsideModelClass;

    ShimmerFrameLayout shimmerFrameLayout;

    LinearLayout noDataLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context= VisitorsListActivity.this;
        myNestedScroll= (NestedScrollView) findViewById(R.id.nested);
        progressBar= findViewById(R.id.progressBar2);
        shimmerFrameLayout= findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrootView=findViewById(R.id.root);
        search = findViewById(R.id.search);
        sharedPrefHelper=new SharedPrefHelper(context);
        noDataLinearLayout=findViewById(R.id.noDataLayout);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                visitorAdapter.getFilter().filter(s);
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
        dataPost.put("requesterUserRole",1);
        dataPost.put("buildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("communityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("flatId", 0);
        dataPost.put("status", StaticData.INSIDE_COMPOUND);
        dataPost.put("fromDate", "");
        dataPost.put("toDate", "");

        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getVisitors;

        Log.e(TAG, "onCreate: " + jsonDataPost);
        Log.e(TAG, "onCreate: " + url);
        Log.e(TAG, "onCreate: JWT_TOKEN =  " + sharedPrefHelper.getString(StaticData.JWT_TOKEN));
        Log.e(TAG, "onCreate: ---------------------- ");



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

                        progressBar.setVisibility(View.GONE);

                        Log.e("TAG ","onResponse: =   " + response);

                        Gson gson = new Gson();
                        getVisitorInsideModelClass = gson.fromJson(String.valueOf(response), GetVisitorInsideModelClass.class);
                        visitorAdapter=new VisitorAdapter((ArrayList<GetInsideVisitorData>) getVisitorInsideModelClass.getData(),context);
                        recyclerView.setAdapter(visitorAdapter);
                        if (getVisitorInsideModelClass.getData().size()<1){
                            noDataLinearLayout.setVisibility(View.VISIBLE);
                        }
                        AndroidNetworking.cancelAll();

                    }

                    @Override
                    public void onError(ANError anError) {

                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();

                        progressBar.setVisibility(View.GONE);

                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });





    }

    public void getdate(){
        Calendar cal = Calendar.getInstance(); //current date and time
        cal.add(Calendar.DAY_OF_MONTH, 0); //add a day
        cal.set(Calendar.HOUR_OF_DAY, 23); //set hour to last hour
        cal.set(Calendar.MINUTE, 59); //set minutes to last minute
        cal.set(Calendar.SECOND, 59); //set seconds to last second
        cal.set(Calendar.MILLISECOND, 999); //set milliseconds to last millisecond

        high=cal.getTime();
        Calendar cal1 = Calendar.getInstance(); //current date and time
        cal1.add(Calendar.DAY_OF_MONTH, 0); //add a day
        cal1.set(Calendar.HOUR_OF_DAY, 0); //set hour to last hour
        cal1.set(Calendar.MINUTE, 0); //set minutes to last minute
        cal1.set(Calendar.SECOND, 0); //set seconds to last second
        cal1.set(Calendar.MILLISECOND, 0); //set milliseconds to last millisecond
        low= cal1.getTime();

        Log.d(TAG, "getdate: bb "+ high + " "+low);

    }

}
