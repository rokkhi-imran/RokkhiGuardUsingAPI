package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Model.api.VisitorResponseByID;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitorAcceptedActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView nameTV;
    TextView phoneTV;
    TextView flatTV;
    TextView purposeTV;

    TextView insideBtn;
    TextView outsideBtn;
    Button backBtn;
    Context context;

    String id;

    VisitorResponseByID visitorResponseByID;

    FullScreenAlertDialog fullScreenAlertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_accepted);
        context = VisitorAcceptedActivity.this;

        //cancel all notification
        NotificationManagerCompat.from(context).cancelAll();

        nameTV = findViewById(R.id.nameET);
        circleImageView = findViewById(R.id.imageView_id);
        phoneTV = findViewById(R.id.phoneET);
        flatTV = findViewById(R.id.flatNumberET);
        purposeTV = findViewById(R.id.visitET_ID);
        insideBtn = findViewById(R.id.insideBtn);
        outsideBtn = findViewById(R.id.cancelUserInfoBtn);
        backBtn = findViewById(R.id.backbuttonInfoBtn);
        fullScreenAlertDialog=new FullScreenAlertDialog(this);

        Intent intent = getIntent();

        id = intent.getStringExtra("id")+"";
        getVisitorDataByID(id);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void getVisitorDataByID(String visitorID) {

        fullScreenAlertDialog.showdialog();


        String url = StaticData.baseURL+StaticData.getVisitorById;

        AndroidNetworking.post(url)
                .addBodyParameter("visitorId", visitorID)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fullScreenAlertDialog.dismissdialog();

                        Log.e("TAG", "onResponse: =   " + response);

                        Gson gson = new Gson();
                        visitorResponseByID = gson.fromJson(String.valueOf(response), VisitorResponseByID.class);

                        Log.e("TAG", "onResponse: GetID "+visitorResponseByID.getData().getCommunity().getId() );
                        Log.e("TAG", "onResponse: get Status "+visitorResponseByID.getData().getStatus() );

                        if (visitorResponseByID.getData().getStatus().equals(StaticData.INSIDE_COMPOUND)) {
                            insideBtn.setText("অনুমতি দেয়া হয়েছে");
                        }else if (visitorResponseByID.getData().getStatus().equals(StaticData.OUTSIDE_COMPOUND)) {
                            insideBtn.setText("ভিজিটর বের হয়ে গেছে ");
                        }else  {
                            insideBtn.setText("অনুমতি দেয়া হয়নি");
                        }


                        nameTV.setText(visitorResponseByID.getData().getName());
                        phoneTV.setText(visitorResponseByID.getData().getContact());
//                        flatTV.setText(visitorResponseByID.getData().);
//                        purposeTV.setText(visitorResponseByID.getData().no);

                        if (!visitorResponseByID.getData().getImage().isEmpty()) {

                            Picasso.get().load(visitorResponseByID.getData().getImage()).placeholder(R.drawable.progress_animation).error(R.drawable.male1).into(circleImageView);
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        fullScreenAlertDialog.dismissdialog();
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


}