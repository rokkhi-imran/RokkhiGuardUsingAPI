package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;


public class StaticData {

    public static final String KEY_FIREBASE_ID_TOKEN = "FIREBASE_ID_TOKEN";

    public static void getIdToken(final Context context) {
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

    public  static String baseURL= "http://home.api.rokkhi.com:3000";
    public  static String getNotice= baseURL+"/api/v1/notice/getNotices";

}
