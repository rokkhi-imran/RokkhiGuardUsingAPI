package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.rokkhi.rokkhiguard.Model.api.NoticeModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.retrofit.NoticeViewModel;


public class NoticeBoardActivity extends AppCompatActivity {

    Context context;

    NoticeViewModel noticeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;


        noticeViewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);
        noticeViewModel.init("", 2, 2, "",
                "");

        noticeViewModel.getNewsRepository().observe(this, newsResponse -> {
            NoticeModelClass noticeModelClass = newsResponse;

            Toast.makeText(context, ""+noticeModelClass, Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onChanged:  = "+newsResponse );

        });

    }




}
