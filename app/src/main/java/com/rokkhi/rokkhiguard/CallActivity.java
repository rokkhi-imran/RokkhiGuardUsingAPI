package com.rokkhi.rokkhiguard;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.CountDownTimer;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

public class CallActivity extends AppCompatActivity  {



    Context context;
    Button endcall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        context= CallActivity.this;

        setContentView(R.layout.activity_call2);
        endcall= findViewById(R.id.endcall);


        IntentFilter filter = new IntentFilter();

        filter.addAction("finish");
        registerReceiver(receiver, filter);


        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        endcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endcall();
            }
        });







    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();

        }
    };




    private void endcall(){
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {

            endcall1();
        }else {
            final String[] PERMISSIONS_STORAGE = {Manifest.permission.ANSWER_PHONE_CALLS};
            //Asking request Permissions
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            endcall1();
        }else {
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    String phoneno="";

    private void endcall1(){
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
            TelecomManager tm = (TelecomManager)getSystemService(Context.TELECOM_SERVICE);

            if (tm != null) {
                boolean success = tm.endCall();
                // success == true if call was terminated.
            }
        }else{
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        boolean finish= intent.getBooleanExtra("finish",false);
        if( !finish)finish();
    }

    @Override
    public void onBackPressed() {

    }







    private static final String TAG = "CallActivity";
}
