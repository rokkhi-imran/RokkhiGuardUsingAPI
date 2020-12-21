package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.ChildListAdapter;
import com.rokkhi.rokkhiguard.Model.api.ChildData;
import com.rokkhi.rokkhiguard.Model.api.ChildModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChildrenListActivity extends AppCompatActivity  {
    private static final String TAG = "ChildrenList";


    RecyclerView recyclerView;
    ChildListAdapter childAdapter;

    SharedPrefHelper sharedPrefHelper;
    Context context;
    ChildModelClass childModelClass;
    ProgressBar mProgressBar;
    EditText searchET;

    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        context=this;
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mProgressBar=findViewById(R.id.progressBar2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        sharedPrefHelper=new SharedPrefHelper(getApplicationContext());
        searchET=findViewById(R.id.search);


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recyclerView = findViewById(R.id.recyclerview);

                recyclerView.setNestedScrollingEnabled(false);

                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                recyclerView.setLayoutManager(new LinearLayoutManager(context));


                childAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        Map<String, String> dataPost = new HashMap<>();

        dataPost.put("buildingId", sharedPrefHelper.getString(StaticData.BUILD_ID));
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("flatId", "");
        dataPost.put("userRoleCode", StaticData.CHILD.toString());

        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getUsersList;

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

                        mProgressBar.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        Log.e(TAG, "onResponse: =   " + response);

                        Gson gson = new Gson();
                        childModelClass = gson.fromJson(String.valueOf(response), ChildModelClass.class);


                        childAdapter = new ChildListAdapter((ArrayList<ChildData>) childModelClass.getData(), context);
                        childAdapter.setHasStableIds(true);
                        recyclerView.setAdapter(childAdapter);


                    }

                    @Override
                    public void onError(ANError anError) {

                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();

                        mProgressBar.setVisibility(View.GONE);

                        StaticData.showErrorAlertDialog(context,"Alert !","আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });




    }


}
