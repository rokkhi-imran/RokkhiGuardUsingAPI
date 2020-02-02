package com.rokkhi.rokkhiguard.Utils;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.rokkhi.rokkhiguard.Model.Notifications;
import com.rokkhi.rokkhiguard.NoticeBoard;
import com.rokkhi.rokkhiguard.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticeDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    protected CircleImageView imageViewID;
    protected TextView noticeTitleTV;
    protected TextView noticeDetailsTV;
    protected Button okButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_notice_details);
        initView();

       Notifications notification =  getIntent().getParcelableExtra("noticeDetails");
//        Glide.with(context).load(notification.)
        noticeTitleTV.setText(notification.getN_title());
        noticeDetailsTV.setText(notification.getN_body());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.okButton) {

            startActivity(new Intent(this, NoticeBoard.class));
            finish();

        }
    }

    private void initView() {
        context=this;
        imageViewID = (CircleImageView) findViewById(R.id.imageView_ID);
        noticeTitleTV = (TextView) findViewById(R.id.noticeTitleTV);
        noticeDetailsTV = (TextView) findViewById(R.id.noticeDetailsTV);
        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(NoticeDetailsActivity.this);
    }
}
