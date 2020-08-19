package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

class StaticField {
    public static String url="http://ec2-54-183-244-125.us-west-1.compute.amazonaws.com:8000/upload";



    public static void sendExceptionFunction(@NotNull Exception e, Context context, String activityName) {



        String osVersion = android.os.Build.VERSION.RELEASE; // e.g. myVersion := "1.6"


        FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();


        Map<String, Object> data = new HashMap<>();
        data.put("activityName", activityName);
        data.put("appVersionName", "1.048");
        data.put("OsVersionCode", "Android "+osVersion);
        data.put("appName", "Rokkhi Guard App ");
        data.put("DeviceName", getDeviceName());
        data.put("exception", new Gson().toJson(e));

        firebaseFunctions
                .getHttpsCallable("exceptionRecord")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {

                        if (!task.isSuccessful()) {
                            Log.e("TAG", "then: getException  !success= " + task.getException());

                        } else {
                            Log.e("TAG", "then: getException success= " + task.getException());

                        }

                        return null;

                    }
                });

    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
