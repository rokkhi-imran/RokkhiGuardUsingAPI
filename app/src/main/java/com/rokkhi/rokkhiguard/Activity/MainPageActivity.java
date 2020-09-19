package com.rokkhi.rokkhiguard.Activity;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.telecom.TelecomManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.data.BlackListRepository;
import com.rokkhi.rokkhiguard.data.FlatsRepository;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;
import com.rokkhi.rokkhiguard.data.WhiteListRepository;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.rokkhi.rokkhiguard.StaticData.getIdToken;

public class MainPageActivity extends AppCompatActivity {

    private static final String TAG = "MainPage";
    CircleImageView logout, addvis, vislist, notice, parcel, create, vehicle,
            child;
    Context context;
    ImageButton settings;

    FlatsRepository flatsRepository;
    WhiteListRepository whiteListRepository;
    BlackListRepository blackListRepository;
    VehiclesRepository vehiclesRepository;
    String thismobileuid;
    String appVersion;

    RecyclerView recyclerViewVisitorAdapter;

    ArrayList<Visitors> visitorsArrayList;

    Button buildingName;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        context = MainPageActivity.this;

        sharedPrefHelper=new SharedPrefHelper(context);

        visitorsArrayList = new ArrayList<>();
        logout = findViewById(R.id.logout);
        addvis = findViewById(R.id.addvis);
        vislist = findViewById(R.id.vislist);
        notice = findViewById(R.id.notice);
        parcel = findViewById(R.id.parcel);
        create = findViewById(R.id.profile);
        settings = findViewById(R.id.settings);
        vehicle = findViewById(R.id.vehicle);
        child = findViewById(R.id.child);
        buildingName = findViewById(R.id.buildingNameTV);

        recyclerViewVisitorAdapter = findViewById(R.id.recyclerviewVisitorWaitingListID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        recyclerViewVisitorAdapter.setLayoutManager(linearLayoutManager);


        //get Token And Save
        getIdToken(context);

        //set default caller

        offerReplacingDefaultDialer(context);

        //check the call permission
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
            requestPermissions(permissions, 1);
        }


//check new app start

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//check new app End


        thismobileuid = FirebaseAuth.getInstance().getUid();

        flatsRepository = new FlatsRepository(this);
        whiteListRepository = new WhiteListRepository(this);
        blackListRepository = new BlackListRepository(this);
        vehiclesRepository = new VehiclesRepository(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DaroanPassActivity.class);
                startActivity(intent);
                finish();
            }
        });

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParkingActivity.class);
                startActivity(intent);
            }
        });

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String token = instanceIdResult.getToken();
                        // send it to server
                    }
                });


                Intent intent = new Intent(context, ChildrenListActivity.class);
                startActivity(intent);
            }
        });


        addvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, AddVisitorActivity.class);
                startActivity(intent);
            }
        });
        vislist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, VisitorsListActivity.class);
                startActivity(intent);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, NoticeBoardActivity.class);
                startActivity(intent);

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {

                    sWorkerListActivity();


                } else {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainPageActivity.this, DaroanPassActivity.class);
                    startActivity(intent);
                }


            }
        });
        parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, ParcelActivity.class);
                startActivity(intent);

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showDialog(final Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);


        dialog.getWindow().getAttributes().height =
                (int) (getDeviceMetrics(context).heightPixels * 0.8);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_emergency);

        final TextView text = (TextView) dialog.findViewById(R.id.alertDetailsTV);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.okButtonAlert);

        text.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (text.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(activity, DaroanPassActivity.class));
                finish();
            }
        });

        dialog.show();


    }

    public static DisplayMetrics getDeviceMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }


    private void offerReplacingDefaultDialer(Context context) {

        if (context.getSystemService(TelecomManager.class).getDefaultDialerPackage() != getPackageName()) {
            new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        }
    }


    public void sWorkerListActivity() {

        Intent intent = new Intent(MainPageActivity.this, SWorkersActivity.class);
        startActivity(intent);

    }


    public void matchanddelete(ArrayList<Vehicle> check, Vehicle vehicle) {
        boolean flag = false;

        for (int i = 0; i < check.size(); i++) {
            if (check.get(i).getVehicle_id().equals(vehicle.getVehicle_id())) {
                //vehiclesRepository.deleteVehicle(vehicle);
                Log.e(TAG, "matchanddelete: hhhh ");
                flag = true;
            }
        }
        if (!flag) vehiclesRepository.deleteVehicle(vehicle);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static class CheckForSDCard {

        //Method to Check If SD Card is mounted or not
        public static boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(

                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }
}
