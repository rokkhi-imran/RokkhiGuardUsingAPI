package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;


public class StaticData {

    public static final String KEY_FIREBASE_ID_TOKEN = "FIREBASE_ID_TOKEN";
    public static final String FLAT_ID = "FLAT_ID";
    public static final String BUILD_ID = "BUILDING_ID";
    public static final String COMM_ID = "COMM_ID";
    public static final String USER_ID = "USER_ID";
    public static String imageUploadURL ="http://ec2-54-183-244-125.us-west-1.compute.amazonaws.com:8000/upload";

    public static final void getIdToken(final Context context) {
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                Log.e("TAG", "onSuccess: "+getTokenResult.getToken() );

                SharedPrefHelper sharedPrefHelper=new SharedPrefHelper(context);
                sharedPrefHelper.putString(StaticData.KEY_FIREBASE_ID_TOKEN,getTokenResult.getToken());
                Log.e("TAG", "onSuccess: "+getTokenResult.getToken() );

            }
        });

    }

    public static final void showErrorAlertDialog(Context context,String alertTitle,String alertBody){

        new AlertDialog.Builder(context)
                .setTitle(alertTitle)
                .setMessage(alertBody)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public static final String baseURL= "http://home.api.rokkhi.com:3000";
    public static final String getNotice= "/api/v1/notice/getNotices";
    public static final String getUsersList= "/api/v1/user/getUsersList";
    public static final String addParcel= "/api/v1/entrance/addParcel";


    //user role
    public static final Integer SUPER_ADMIN = 1001;
    public static final Integer GENERAL_REGISTERED_USER = 1002;
    public static final Integer SERVICE_WORKER = 1003;
    public static final Integer GENERAL_FLAT_MEMBER = 1004;
    public static final Integer CHILD = 1005;
    public static final Integer CARETAKER = 1006;
    public static final Integer COMMITTEE_MEMBER = 1007;
    public static final Integer GUARD = 1008;

}
