package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitorAcceptedActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView nameTV;
    TextView phoneTV;
    TextView flatTV;
    TextView purposeTV;

    Button insideBtn;
    Button outsideBtn;
    Button backBtn;
    Context context;

    String visitorName;
    String status;
    String visitorAddress;
    String visitorImage;
    String id;
    String visitorContact;
    String notificationType;
    String flatName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_accepted);
        context = VisitorAcceptedActivity.this;

        nameTV = findViewById(R.id.nameET);
        circleImageView = findViewById(R.id.imageView_id);
        phoneTV = findViewById(R.id.phoneET);
        flatTV = findViewById(R.id.flatNumberET);
        purposeTV = findViewById(R.id.visitET_ID);
        insideBtn = findViewById(R.id.insideBtn);
        outsideBtn = findViewById(R.id.cancelUserInfoBtn);
        backBtn = findViewById(R.id.backbuttonInfoBtn);
        Log.e("TAG", "onCreate: status "+status);
        Intent intent = getIntent();

        visitorName = intent.getStringExtra("visitorName")+"";
        status = intent.getStringExtra("status")+"";
        visitorAddress = intent.getStringExtra("visitorAddress")+"";
        visitorImage = intent.getStringExtra("visitorImage")+"";
        id = intent.getStringExtra("id")+"";
        visitorContact = intent.getStringExtra("visitorContact")+"";
        notificationType = intent.getStringExtra("notificationType")+"";
        flatName = intent.getStringExtra("flatName")+"";



        if (status.equals(StaticData.INSIDE_COMPOUND)) {
            outsideBtn.setVisibility(View.GONE);
            insideBtn.setVisibility(View.VISIBLE);
        }else  {
            outsideBtn.setVisibility(View.VISIBLE);
            insideBtn.setVisibility(View.GONE);
        }


        nameTV.setText(visitorName);
        phoneTV.setText(visitorContact);
        flatTV.setText(flatName);
        purposeTV.setText(notificationType);

        if (!visitorImage.isEmpty()) {

            Picasso.get().load(visitorImage).placeholder(R.drawable.progress_animation).error(R.drawable.male1).into(circleImageView);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


}