package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;


public class StaticData {

    public static final String FLAT_ID = "FLAT_ID";
    public static final String BUILD_ID = "BUILDING_ID";
    public static final String COMM_ID = "COMM_ID";
    public static final String USER_ID = "USER_ID";
    public static final String ALL_FLATS = "ALL_FLATS";
    public static final String BUILD_NAME = "BUILD_NAME";
    public static final String BUILD_ADDRESS = "BUILD_ADDRESS";
    public static final int REQUEST_FOR_APPEAR_ON_TOP_CODE = 565;
    public static final String JWT_TOKEN = "JWT_TOKEN";
    public static final String KRY_DEVICE_TOKEN = "DEVICE_TOKEN";
    public static final String CALL_FLAT_NAME = "CALL_FLAT_NAME";
    public static final String GET_CALL_NUMBER = "GET_CALL_NUMBER";
    public static final String TIME_ZONE ="TIME_ZONE" ;

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

//    public static final String baseURL = "https://home.api.rokkhi.com";
//    public static final String baseURL = "http://dev.rokkhi.com:3200";
    public static final String baseURL = "http://api.rokkhi.com:3000";
    public static String imageUploadURL = "/api/v1/image/uploadSingle";

    public static final String getNotice = "/api/v1/notice/getNotices";
    public static final String getUsersList = "/api/v1/user/getUsersList";
    public static final String addParcel = "/api/v1/entrance/addParcel";
    public static final String registerOrUpdate = "/api/v1/user/registerOrUpdate";
    public static final String getVisitors = "/api/v1/entrance/getVisitors";
    public static final String letTheVisitorOut = "/api/v1/entrance/changeVisitorStatus";
    public static final String getVehicles = "/api/v1/vehicle/getVehicles";
    public static final String recordVehicleEntry = "/api/v1/entrance/recordVehicleEntry";
    public static final String recordVehicleExit = "/api/v1/entrance/recordVehicleExit";
    public static final String getUserDetails = "/api/v1/user/getUserDetails";
    public static final String getByPhoneNumber = "/api/v1/user/getByPhoneNumber";
    public static final String recordChildrenExitFromCompound = "/api/v1/entrance/recordChildrenExitFromCompound";
    public static final String getFlats = "/api/v1/flat/getFlats";
    public static final String addVisitor = "/api/v1/entrance/addVisitor";
    public static final String changeVisitorStatus = "/api/v1/entrance/changeVisitorStatus";
    public static final String recordServiceWorkerEntry = "/api/v1/entrance/recordServiceWorkerEntry";
    public static final String recordServiceWorkerExit = "/api/v1/entrance/recordServiceWorkerExit";
    public static final String changeServiceWorkerStatus = "/api/v1/entrance/changeServiceWorkerStatus";
    public static final String getRecordedUserByPhoneNumber = "/api/v1/entrance/getRecordedUserByPhoneNumber";
    public static final String removeDeviceToken = "/api/v1/device/removeDevice";
    public static final String assignRoleToUserBeta = "/api/v1/privilege/assignRoleToUserBeta";


    public static final String getVisitorById = "/api/v1/entrance/getVisitorById";
    public static final String getServiceWorkers = "/api/v1/user-relations/getServiceWorkers";



    //user role
    public static final Integer SUPER_ADMIN = 1001;
    public static final Integer GENERAL_REGISTERED_USER = 1002;
    public static final Integer SERVICE_WORKER = 1003;
    public static final Integer GENERAL_FLAT_MEMBER = 1004;
    public static final Integer CHILD = 1005;
    public static final Integer CARETAKER = 1006;
    public static final Integer COMMITTEE_MEMBER = 1007;
    public static final Integer GUARD = 1008;
    public static final Integer GUARD_PHONE = 1009;


    public static final String PARCEL_RECEIVED = "PARCEL_RECEIVED";
    public static final String PARCEL_DELIVERED = "PARCEL_DELIVERED";
    public static final String INSIDE_COMPOUND = "INSIDE_COMPOUND";
    public static final String OUTSIDE_COMPOUND = "OUTSIDE_COMPOUND";
    public static final String CANCEL_COMPOUND = "CANCEL";
    public static final String PENDING_PERMISSION = "PENDING_PERMISSION";

    public static final String  WHITE_LISTED = "WHITE_LISTED";
    public static final String  BLACK_LISTED = "BLACK_LISTED";
    public static final String  NO_FLAT_MEMBER = "NO_FLAT_MEMBER";
    public static final String  NO_SPECIALITY = "NO_SPECIALITY";

    //    NoticeForConstants
    public static final String ALL = "ALL";
    public static final String FLAT_MEMBERS = "FLAT_MEMBERS";
    public static final String BUILDING_OWNERS = "BUILDING_OWNERS";
    public static final String COMMITTEE_MEMBERS = "COMMITTEE_MEMBERS";
    public static final String GUARDS = "GUARDS";

    public static void selectImage(FragmentActivity createProfileActivity) {
        PickSetup setup = new PickSetup()
                .setTitle("Choose Photo")
                .setCameraIcon(R.mipmap.camera_colored)
                .setBackgroundColor(Color.RED)
                .setCancelTextColor(Color.RED)
                .setSystemDialog(true);



        PickImageDialog.build(setup)
                .show(createProfileActivity);
    }

    public static void showSuccessDialog(FragmentActivity context, String title, String body) {

        try {

            new AlertDialog.Builder(context)
                    .setIcon(R.drawable.tik)
                    .setTitle("\n\n"+title)
                    .setMessage(body)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            context.finish();
                        }
                    })
                    .show();

        }catch (Exception e){

        }
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.finish();
            }
        }, 3000);

    }
}
