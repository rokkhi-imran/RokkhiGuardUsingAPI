package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.os.Build;

import org.jetbrains.annotations.NotNull;

class StaticField {
    public static String url="http://ec2-54-183-244-125.us-west-1.compute.amazonaws.com:8000/upload";



    public static void sendExceptionFunction(@NotNull Exception e, Context context, String activityName) {



        String osVersion = android.os.Build.VERSION.RELEASE; // e.g. myVersion := "1.6"


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
