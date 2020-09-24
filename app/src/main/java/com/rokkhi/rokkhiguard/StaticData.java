package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;


public class StaticData {

    public static final String KEY_FIREBASE_ID_TOKEN = "FIREBASE_ID_TOKEN";
    public static final String FLAT_ID = "FLAT_ID";
    public static final String BUILD_ID = "BUILDING_ID";
    public static final String COMM_ID = "COMM_ID";
    public static final String USER_ID = "USER_ID";
    public static String imageUploadURL = "http://ec2-54-183-244-125.us-west-1.compute.amazonaws.com:8000/upload";

    public static final void getIdToken(final Context context) {
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                Log.e("TAG", "onSuccess: " + getTokenResult.getToken());

                SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);
                sharedPrefHelper.putString(StaticData.KEY_FIREBASE_ID_TOKEN, getTokenResult.getToken());
                Log.e("TAG", "onSuccess: " + getTokenResult.getToken());

            }
        });

    }

    public static final void showErrorAlertDialog(Context context, String alertTitle, String alertBody) {

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

    public static final String baseURL = "http://home.api.rokkhi.com:3000";
    public static final String getNotice = "/api/v1/notice/getNotices";
    public static final String getUsersList = "/api/v1/user/getUsersList";
    public static final String addParcel = "/api/v1/entrance/addParcel";
    public static final String registerOrUpdate = "/api/v1/user/registerOrUpdate";
    public static final String getVisitors = "/api/v1/entrance/getVisitors";
    public static final String letTheVisitorOut = "/api/v1/entrance/letTheVisitorOut";
    public static final String getVehicles = "/api/v1/vehicle/getVehicles";
    public static final String recordVehicleEntry = "/api/v1/entrance/recordVehicleEntry";
    public static final String getUserDetails = "/api/v1/user/getUserDetails";
    public static final String getByPhoneNumber = "/api/v1/user/getByPhoneNumber";


    //user role
    public static final Integer SUPER_ADMIN = 1001;
    public static final Integer GENERAL_REGISTERED_USER = 1002;
    public static final Integer SERVICE_WORKER = 1003;
    public static final Integer GENERAL_FLAT_MEMBER = 1004;
    public static final Integer CHILD = 1005;
    public static final Integer CARETAKER = 1006;
    public static final Integer COMMITTEE_MEMBER = 1007;
    public static final Integer GUARD = 1008;


    public static final String PARCEL_RECEIVED = "PARCEL_RECEIVED";
    public static final String PARCEL_DELIVERED = "PARCEL_DELIVERED";
    public static final String INSIDE_COMPOUND = "INSIDE_COMPOUND";
    public static final String OUTSIDE_COMPOUND = "OUTSIDE_COMPOUND";

    //    NoticeForConstants
    public static final String ALL = "ALL";
    public static final String FLAT_MEMBERS = "FLAT_MEMBERS";
    public static final String BUILDING_OWNERS = "BUILDING_OWNERS";
    public static final String COMMITTEE_MEMBERS = "COMMITTEE_MEMBERS";
    public static final String GUARDS = "GUARDS";

    public static void selectImage(FragmentActivity createProfileActivity) {
        PickSetup setup = new PickSetup()
                .setTitle("Choose Photo")
                .setBackgroundColor(Color.WHITE)
                .setButtonOrientation(LinearLayout.HORIZONTAL)
                .setGalleryButtonText("Gallery")
                .setCameraIcon(R.mipmap.camera_colored)
                .setGalleryIcon(R.mipmap.gallery_colored)
                .setCameraToPictures(false)
                .setMaxSize(300);

        PickImageDialog.build(setup)
                //.setOnClick(this)
                .show(createProfileActivity);
    }

    public static void showSuccessDialog(FragmentActivity context, String title, String body) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        context.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
