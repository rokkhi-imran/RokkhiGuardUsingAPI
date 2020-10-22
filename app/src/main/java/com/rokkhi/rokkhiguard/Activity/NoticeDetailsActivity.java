package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        String noticeDataDetails = getIntent().getStringExtra("noticeDetails");
        String noticeDataTitle = getIntent().getStringExtra("noticeTitle");
        noticeTitleTV.setText(noticeDataTitle);
        noticeDetailsTV.setText(noticeDataDetails);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.okButton) {
            finish();

        }
    }

    private void initView() {
        context = this;
        imageViewID = (CircleImageView) findViewById(R.id.imageView_ID);
        noticeTitleTV = (TextView) findViewById(R.id.noticeTitleTV);
        noticeDetailsTV = (TextView) findViewById(R.id.noticeDetailsTV);
        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(NoticeDetailsActivity.this);
    }
}