package com.rokkhi.rokkhiguard.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rokkhi.rokkhiguard.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity implements IPickResult, View.OnClickListener {


    private CircleImageView userPhotoImageView;
    /**
     * নাম
     */
    private EditText mUserName;
    /**
     * মোবাইল নাম্বার
     */
    private EditText mUserPhoneET;
    /**
     * কি ধরনের প্রোফাইল
     */
    private EditText mUserWtype;
    /**
     * ফ্ল্যাট
     */
    private EditText UserFlatET;
    /**
     * Done ( শেষ )
     */
    private Button submitUserDataBtn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {


        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        userPhotoImageView = (CircleImageView) findViewById(R.id.user_photo);
        userPhotoImageView.setOnClickListener(this);
        mUserName = (EditText) findViewById(R.id.user_name);
        mUserPhoneET = (EditText) findViewById(R.id.user_Phone_ET);
        mUserWtype = (EditText) findViewById(R.id.user_wtype);
        mUserWtype.setOnClickListener(this);
        UserFlatET = (EditText) findViewById(R.id.user_flat);
        UserFlatET.setOnClickListener(this);
        submitUserDataBtn = (Button) findViewById(R.id.done);
        submitUserDataBtn.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.user_photo:
                break;
            case R.id.user_wtype:
                break;
            case R.id.user_flat:
                break;
            case R.id.done:
                break;
        }
    }
}
