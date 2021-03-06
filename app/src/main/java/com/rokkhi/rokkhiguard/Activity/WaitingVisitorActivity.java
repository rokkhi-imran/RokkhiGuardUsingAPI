package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Model.api.VisitorOutModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class WaitingVisitorActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nameET;
    TextView phoneNumberET;
    TextView flatET;
    TextView visitET;
    Button cancleBTn;
    Button entyBtn;
    Button goBack;
    CircleImageView imageView;


    Integer visitorID;
    String name;
    String phone;
    String image;
    String flat;
    Integer flatID;
    String purpose;
    String address;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_visitor);
        context = this;
        phoneNumberET = findViewById(R.id.phoneET);
        flatET = findViewById(R.id.flatNumberET);
        visitET = findViewById(R.id.visitET_ID);
        cancleBTn = findViewById(R.id.cancelUserInfoBtn);
        imageView = findViewById(R.id.imageView_id);
        nameET = findViewById(R.id.nameET);
        entyBtn = findViewById(R.id.insideBtn);
        goBack = findViewById(R.id.backbuttonInfoBtn);

        cancleBTn.setOnClickListener(this);
        entyBtn.setOnClickListener(this);
        goBack.setOnClickListener(this);

        visitorID = getIntent().getIntExtra("visitorID", 0);
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        image = getIntent().getStringExtra("image");
        flat = getIntent().getStringExtra("flat");
        flatID = getIntent().getIntExtra("flatID", 0);
        purpose = getIntent().getStringExtra("purpose");
        address = getIntent().getStringExtra("address");

        if (image != null && !image.isEmpty()) {

            Picasso.get().load(image).fit().placeholder(R.drawable.progress_animation).error(R.drawable.male1).into(imageView);
        }


        nameET.setText(name);
        phoneNumberET.setText(phone);
        flatET.setText(flat);
        visitET.setText(purpose);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelUserInfoBtn:
                callVisitorInFunction(context, visitorID, StaticData.CANCEL_COMPOUND, flatID);

                break;
            case R.id.insideBtn:
                callVisitorInFunction(context, visitorID, StaticData.INSIDE_COMPOUND, flatID);
                break;
            case R.id.backbuttonInfoBtn:
                finish();
                break;
        }
    }


    private void callVisitorInFunction(Context context, int id, String status, Integer flatID) {
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);


        FullScreenAlertDialog fullScreenAlertDialog = new FullScreenAlertDialog(context);

        Map<String, Object> dataPost = new HashMap<>();
        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterUserRole", Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ROLE)));
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("visitorId", id);
        dataPost.put("newStatus", status);
        dataPost.put("visitorName", "");
        dataPost.put("flatId", flatID);


        JSONObject jsonDataPost = new JSONObject(dataPost);
        Log.e("TAG", "callVisitorInFunction: json data visitor in by guard =  " + jsonDataPost);

        String changeVisitorStatusUrl = StaticData.baseURL + "" + StaticData.changeVisitorStatus;

        AndroidNetworking.post(changeVisitorStatusUrl)
                .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fullScreenAlertDialog.dismissdialog();


                        Log.e("TAG", "onResponse: =  =----------- " + response);

                        Gson gson = new Gson();
                        VisitorOutModelClass visitorOutModelClass = gson.fromJson(String.valueOf(response), VisitorOutModelClass.class);
                        if (status.equals(StaticData.INSIDE_COMPOUND)) {

                            StaticData.showSuccessDialog((FragmentActivity) context, "Entry Alert !", "Entry Successfully Done ");
                        } else if (status.equals(StaticData.CANCEL_COMPOUND)) {
                            StaticData.showSuccessDialog((FragmentActivity) context, "Cancel Alert !", "Cancel Successfully Done ");

                        }
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

}