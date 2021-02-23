package com.rokkhi.rokkhiguard.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.GuardListAdapter;
import com.rokkhi.rokkhiguard.Model.api.GuardListData;
import com.rokkhi.rokkhiguard.Model.api.GuardListModelClass;
import com.rokkhi.rokkhiguard.Model.api.UserDetailsModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GuardListActivity extends AppCompatActivity {

    Context context;
    ProgressBar mProgressbar;
    RecyclerView mGuardListRecyclerView;
    SharedPrefHelper sharedPrefHelper;

    GuardListModelClass guardListModelClass;
    GuardListAdapter guardListAdapter;

    private static final int RC_SIGN_IN = 12773;
    static int REQUEST_CODE_SET_DEFAULT_DIALER = 200;

    AuthUI.IdpConfig phoneConfigWithDefaultNumber;
    FirebaseUser firebaseUser;

    private View mRootView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Normalfunc normalfunc;
    LinearLayout noDataImageLayout;
    boolean called = false;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private static final String TAG = "GuardListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_list);
        context = this;
        mRootView = findViewById(R.id.root);
        sharedPrefHelper = new SharedPrefHelper(context);
        mProgressbar = findViewById(R.id.progressbar);
        mGuardListRecyclerView = findViewById(R.id.guardListRecyclerView);
        noDataImageLayout = findViewById(R.id.noDataImageLayout);
        normalfunc = new Normalfunc();
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        getlatLon();

        //get system alert window permission
        if (!Settings.canDrawOverlays(context)) {

            appearOnTheTopAlert();

        } else {
        }
        if (called){
            mProgressbar.setVisibility(View.GONE);
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {

                    sharedPrefHelper.clearAllData();
                    goSignInPage();
                } else {
//                    mProgressbar.setVisibility(View.VISIBLE);

                    FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        @Override
                        public void onSuccess(GetTokenResult getTokenResult) {

                            if (!called) {

                                callUserDetails(getTokenResult.getToken());
                            }

                        }
                    });


                }
            }
        };


    }

    private void getlatLon() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                GuardListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                GuardListActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    List<Address>addressList=geocoder.getFromLocation(lat,longi,1);
                    String cityName = addressList.get(0).getLocality();
                    String countryCode = addressList.get(0).getCountryCode();

                    String json_str = "{\"AD\":\"Europe\",\"AE\":\"Asia\",\"AF\":\"Asia\",\"AG\":\"North America\",\"AI\":\"North America\",\"AL\":\"Europe\",\"AM\":\"Asia\",\"AN\":\"North America\",\"AO\":\"Africa\",\"AQ\":\"Antarctica\",\"AR\":\"South America\",\"AS\":\"Australia\",\"AT\":\"Europe\",\"AU\":\"Australia\",\"AW\":\"North America\",\"AZ\":\"Asia\",\"BA\":\"Europe\",\"BB\":\"North America\",\"BD\":\"Asia\",\"BE\":\"Europe\",\"BF\":\"Africa\",\"BG\":\"Europe\",\"BH\":\"Asia\",\"BI\":\"Africa\",\"BJ\":\"Africa\",\"BM\":\"North America\",\"BN\":\"Asia\",\"BO\":\"South America\",\"BR\":\"South America\",\"BS\":\"North America\",\"BT\":\"Asia\",\"BW\":\"Africa\",\"BY\":\"Europe\",\"BZ\":\"North America\",\"CA\":\"North America\",\"CC\":\"Asia\",\"CD\":\"Africa\",\"CF\":\"Africa\",\"CG\":\"Africa\",\"CH\":\"Europe\",\"CI\":\"Africa\",\"CK\":\"Australia\",\"CL\":\"South America\",\"CM\":\"Africa\",\"CN\":\"Asia\",\"CO\":\"South America\",\"CR\":\"North America\",\"CU\":\"North America\",\"CV\":\"Africa\",\"CX\":\"Asia\",\"CY\":\"Asia\",\"CZ\":\"Europe\",\"DE\":\"Europe\",\"DJ\":\"Africa\",\"DK\":\"Europe\",\"DM\":\"North America\",\"DO\":\"North America\",\"DZ\":\"Africa\",\"EC\":\"South America\",\"EE\":\"Europe\",\"EG\":\"Africa\",\"EH\":\"Africa\",\"ER\":\"Africa\",\"ES\":\"Europe\",\"ET\":\"Africa\",\"FI\":\"Europe\",\"FJ\":\"Australia\",\"FK\":\"South America\",\"FM\":\"Australia\",\"FO\":\"Europe\",\"FR\":\"Europe\",\"GA\":\"Africa\",\"GB\":\"Europe\",\"GD\":\"North America\",\"GE\":\"Asia\",\"GF\":\"South America\",\"GG\":\"Europe\",\"GH\":\"Africa\",\"GI\":\"Europe\",\"GL\":\"North America\",\"GM\":\"Africa\",\"GN\":\"Africa\",\"GP\":\"North America\",\"GQ\":\"Africa\",\"GR\":\"Europe\",\"GS\":\"Antarctica\",\"GT\":\"North America\",\"GU\":\"Australia\",\"GW\":\"Africa\",\"GY\":\"South America\",\"HK\":\"Asia\",\"HN\":\"North America\",\"HR\":\"Europe\",\"HT\":\"North America\",\"HU\":\"Europe\",\"ID\":\"Asia\",\"IE\":\"Europe\",\"IL\":\"Asia\",\"IM\":\"Europe\",\"IN\":\"Asia\",\"IO\":\"Asia\",\"IQ\":\"Asia\",\"IR\":\"Asia\",\"IS\":\"Europe\",\"IT\":\"Europe\",\"JE\":\"Europe\",\"JM\":\"North America\",\"JO\":\"Asia\",\"JP\":\"Asia\",\"KE\":\"Africa\",\"KG\":\"Asia\",\"KH\":\"Asia\",\"KI\":\"Australia\",\"KM\":\"Africa\",\"KN\":\"North America\",\"KP\":\"Asia\",\"KR\":\"Asia\",\"KW\":\"Asia\",\"KY\":\"North America\",\"KZ\":\"Asia\",\"LA\":\"Asia\",\"LB\":\"Asia\",\"LC\":\"North America\",\"LI\":\"Europe\",\"LK\":\"Asia\",\"LR\":\"Africa\",\"LS\":\"Africa\",\"LT\":\"Europe\",\"LU\":\"Europe\",\"LV\":\"Europe\",\"LY\":\"Africa\",\"MA\":\"Africa\",\"MC\":\"Europe\",\"MD\":\"Europe\",\"ME\":\"Europe\",\"MG\":\"Africa\",\"MH\":\"Australia\",\"MK\":\"Europe\",\"ML\":\"Africa\",\"MM\":\"Asia\",\"MN\":\"Asia\",\"MO\":\"Asia\",\"MP\":\"Australia\",\"MQ\":\"North America\",\"MR\":\"Africa\",\"MS\":\"North America\",\"MT\":\"Europe\",\"MU\":\"Africa\",\"MV\":\"Asia\",\"MW\":\"Africa\",\"MX\":\"North America\",\"MY\":\"Asia\",\"MZ\":\"Africa\",\"NA\":\"Africa\",\"NC\":\"Australia\",\"NE\":\"Africa\",\"NF\":\"Australia\",\"NG\":\"Africa\",\"NI\":\"North America\",\"NL\":\"Europe\",\"NO\":\"Europe\",\"NP\":\"Asia\",\"NR\":\"Australia\",\"NU\":\"Australia\",\"NZ\":\"Australia\",\"OM\":\"Asia\",\"PA\":\"North America\",\"PE\":\"South America\",\"PF\":\"Australia\",\"PG\":\"Australia\",\"PH\":\"Asia\",\"PK\":\"Asia\",\"PL\":\"Europe\",\"PM\":\"North America\",\"PN\":\"Australia\",\"PR\":\"North America\",\"PS\":\"Asia\",\"PT\":\"Europe\",\"PW\":\"Australia\",\"PY\":\"South America\",\"QA\":\"Asia\",\"RE\":\"Africa\",\"RO\":\"Europe\",\"RS\":\"Europe\",\"RU\":\"Europe\",\"RW\":\"Africa\",\"SA\":\"Asia\",\"SB\":\"Australia\",\"SC\":\"Africa\",\"SD\":\"Africa\",\"SE\":\"Europe\",\"SG\":\"Asia\",\"SH\":\"Africa\",\"SI\":\"Europe\",\"SJ\":\"Europe\",\"SK\":\"Europe\",\"SL\":\"Africa\",\"SM\":\"Europe\",\"SN\":\"Africa\",\"SO\":\"Africa\",\"SR\":\"South America\",\"ST\":\"Africa\",\"SV\":\"North America\",\"SY\":\"Asia\",\"SZ\":\"Africa\",\"TC\":\"North America\",\"TD\":\"Africa\",\"TF\":\"Antarctica\",\"TG\":\"Africa\",\"TH\":\"Asia\",\"TJ\":\"Asia\",\"TK\":\"Australia\",\"TM\":\"Asia\",\"TN\":\"Africa\",\"TO\":\"Australia\",\"TR\":\"Asia\",\"TT\":\"North America\",\"TV\":\"Australia\",\"TW\":\"Asia\",\"TZ\":\"Africa\",\"UA\":\"Europe\",\"UG\":\"Africa\",\"US\":\"North America\",\"UY\":\"South America\",\"UZ\":\"Asia\",\"VC\":\"North America\",\"VE\":\"South America\",\"VG\":\"North America\",\"VI\":\"North America\",\"VN\":\"Asia\",\"VU\":\"Australia\",\"WF\":\"Australia\",\"WS\":\"Australia\",\"YE\":\"Asia\",\"YT\":\"Africa\",\"ZA\":\"Africa\",\"ZM\":\"Africa\",\"ZW\":\"Africa\"}";
                    JSONObject jsonObject = new JSONObject(json_str);
                    String continentName = jsonObject.getString(countryCode);
                    String timeZone=cityName+"/"+continentName;

                    sharedPrefHelper.putString(StaticData.TIME_ZONE,timeZone);

                    Log.e(TAG, "getLocation: timeZon =  "+sharedPrefHelper.getString(StaticData.TIME_ZONE) );
                    Log.e(TAG, "getLocation: = "+cityName+" country = "+countryCode+" country name = "+continentName );


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


                Log.e(TAG, "getLocation: = "+lat+" lon = "+longi );

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void appearOnTheTopAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Alert !");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Allow display overlay to run over other apps");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                StroagePermission.checkDrawOverlayPermission(GuardListActivity.this, StaticData.REQUEST_FOR_APPEAR_ON_TOP_CODE);

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void callGuardList(String jWTToken) {

        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("buildingId", sharedPrefHelper.getString(StaticData.BUILD_ID));
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("flatId", "");
        dataPost.put("userRoleCode", StaticData.GUARD.toString());


        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getUsersList;

        Log.e("TAG", "onCreate: Guard List " + jsonDataPost);
        Log.e("TAG", "onCreate: Guard List " + url);
        Log.e("TAG", "onCreate: Guard List " + sharedPrefHelper.getString(StaticData.JWT_TOKEN));
        Log.e("TAG", "onCreate: ---------------------- ");


        AndroidNetworking.post(url)
                .addHeaders("jwtTokenHeader", jWTToken)
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        mProgressbar.setVisibility(View.GONE);

                        Log.e("TAG ", "onResponse: Guard List =   " + response);

                        Gson gson = new Gson();
                        guardListModelClass = gson.fromJson(String.valueOf(response), GuardListModelClass.class);


                        guardListAdapter = new GuardListAdapter((ArrayList<GuardListData>) guardListModelClass.getData(), context);

                        mGuardListRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                        mGuardListRecyclerView.setAdapter(guardListAdapter);

                        if (guardListModelClass.getData().size() < 1) {
                            mGuardListRecyclerView.setVisibility(View.GONE);
                            noDataImageLayout.setVisibility(View.VISIBLE);

                        } else {
                            mGuardListRecyclerView.setVisibility(View.VISIBLE);

                        }
                        AndroidNetworking.cancelAll();

                    }

                    @Override
                    public void onError(ANError anError) {

                        mProgressbar.setVisibility(View.GONE);

                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });


    }


    private void goSignInPage() {
        List<String> whitelistedCountries = new ArrayList<String>();
        whitelistedCountries.add("in");
        whitelistedCountries.add("bd");
        phoneConfigWithDefaultNumber = new AuthUI.IdpConfig.PhoneBuilder()
                .setDefaultCountryIso("bd")
                .setWhitelistedCountries(whitelistedCountries)
                .build();

        signInPhone(mRootView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }

        if (requestCode == StaticData.REQUEST_FOR_APPEAR_ON_TOP_CODE) {

            if (!Settings.canDrawOverlays(this)) {

                appearOnTheTopAlert();
            } else {

            }

        }
    }


    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == RESULT_OK) {

        } else {
            if (response == null) {
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }

    }

    private void callUserDetails(String firebaseToken) {

        Log.e("TAG", "onSuccess: first time call Function: ");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                called = true;

                String deviceToken = instanceIdResult.getToken();
                sharedPrefHelper.putString(StaticData.KRY_DEVICE_TOKEN, deviceToken);

                Log.e("TAG", "token ID onSuccess first time : Device Token =  " + deviceToken);
                Log.e("TAG", "token ID onSuccess first time : auth JWT Token  =  " + sharedPrefHelper.getString(StaticData.JWT_TOKEN));

                Map<String, String> dataPost = new HashMap<>();
                dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
                dataPost.put("limit", "");
                dataPost.put("pageId", "");
                dataPost.put("communityId", "");
                dataPost.put("firebaseIdToken", firebaseToken);
                dataPost.put("deviceToken", deviceToken);
                dataPost.put("deviceType", "Android");


                JSONObject jsonDataPost = new JSONObject(dataPost);

                String url = StaticData.baseURL + "" + StaticData.getUserDetails;

                Log.e("TAG", "onCreate: Call User Details Json Data : " + jsonDataPost);
                Log.e("TAG", "onCreate: Call user Details Url " + url);
                Log.e("TAG", "onCreate: ---------------------- ");

                AndroidNetworking.post(url)
                        .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                        .setContentType("application/json")
                        .addJSONObjectBody(jsonDataPost)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("TAG", "token ID onSuccess first time : User Details response =  " + response);

                                Gson gson = new Gson();
                                UserDetailsModelClass userDetailsModelClass = gson.fromJson(String.valueOf(response), UserDetailsModelClass.class);

                                if (!userDetailsModelClass.getData().getPrimaryRoleCode().equals(StaticData.GUARD_PHONE.toString())) {
                                    showAlertDialog(context);
                                } else {

                                    sharedPrefHelper.putString(StaticData.BUILD_ID, String.valueOf(userDetailsModelClass.getData().getBuildingId()));
                                    sharedPrefHelper.putString(StaticData.COMM_ID, String.valueOf(userDetailsModelClass.getData().getCommunityId()));
                                    sharedPrefHelper.putString(StaticData.JWT_TOKEN, String.valueOf(userDetailsModelClass.getData().getJwtToken()));

                                    callGuardList(sharedPrefHelper.getString(StaticData.JWT_TOKEN));
                                }


                            }

                            @Override
                            public void onError(ANError anError) {
                                mProgressbar.setVisibility(View.GONE);


                                StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                                Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                                Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                                Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                                Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                            }
                        });

            }
        });


    }


    private void showAlertDialog(Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_dialog_not_use_this_app, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Alert !");
        alertDialog.setCancelable(false);
        Button logoutBtn = convertView.findViewById(R.id.logoutBtn);
        Button knowMoreBtn = convertView.findViewById(R.id.knowMore);

        logoutBtn.setOnClickListener(v -> {

            alertDialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(context, GuardListActivity.class);
            startActivity(intent);
            finish();


        });

        knowMoreBtn.setOnClickListener(view -> {

            alertDialog.dismiss();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.rokkhi.com/"));
            startActivity(browserIntent);
        });

        alertDialog.show();


    }


    public void signInPhone(View view) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(phoneConfigWithDefaultNumber))
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG", "onStart: xxx");
        mAuth.addAuthStateListener(mAuthListener);


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop: xxx");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showSnackbar(int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }


}
