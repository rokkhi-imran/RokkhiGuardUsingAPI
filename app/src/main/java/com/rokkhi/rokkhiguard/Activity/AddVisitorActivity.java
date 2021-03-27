package com.rokkhi.rokkhiguard.Activity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.ActiveFlatAdapter;
import com.rokkhi.rokkhiguard.Adapter.TypesAdapter;
import com.rokkhi.rokkhiguard.Adapter.VisitorWaitingListAdapter;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatData;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatsModelClass;
import com.rokkhi.rokkhiguard.Model.api.AddVisitorResponse;
import com.rokkhi.rokkhiguard.Model.api.GetInsideVisitorData;
import com.rokkhi.rokkhiguard.Model.api.GetRecordedUserByPhoneNumber;
import com.rokkhi.rokkhiguard.Model.api.GetVisitorInsideModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddVisitorActivity extends AppCompatActivity implements View.OnClickListener, IPickResult {

    private static final String TAG = "AddVisitorActivity";
    Context context;
    private RecyclerView mVisitorPendingListRecycelrViewID;
    private CircleImageView mUserPhotoIV;

    private TextInputEditText mPhoneNoET;

    private TextInputEditText mUserNameET;

    private TextInputEditText mFlatNumberET;

    private TextInputEditText mPuposeET;

    private TextInputEditText mAddressET;
    private TextView imageUploadTV;


    private Button mSubmitUserInfoBtn;
    private ProgressBar mProgressBar1;

    String mFileUri = "";
    private Bitmap bitmap = null;


    ArrayList<ActiveFlatData> historyFlats;
    String totaltext = "";
    FullScreenAlertDialog fullScreenAlertDialog;

    SharedPrefHelper sharedPrefHelper;

    Normalfunc normalfunc;
    String imageLinkPre="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = AddVisitorActivity.this;
        historyFlats = new ArrayList<>();
        AndroidNetworking.initialize(getApplicationContext());
        sharedPrefHelper = new SharedPrefHelper(context);
        fullScreenAlertDialog = new FullScreenAlertDialog(context);
        normalfunc=new Normalfunc();

        mPhoneNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    callUserInformationByPhoneNumber(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void callUserInformationByPhoneNumber(CharSequence s) {

        Map<String, Object> dataPost = new HashMap<>();
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));

        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole",1);
        dataPost.put("phoneNumber", s.toString());




        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getRecordedUserByPhoneNumber;

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);
        Log.e("TAG", "onCreate: ---------------------- ");


        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);
        AndroidNetworking.post(url)
                .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, "onResponse: =   " + response);

                        Gson gson = new Gson();
                        GetRecordedUserByPhoneNumber getRecordedUserByPhoneNumber = gson.fromJson(String.valueOf(response), GetRecordedUserByPhoneNumber.class);

                        if (!getRecordedUserByPhoneNumber.getData().getType().equals("NOT_FOUND")) {
                            showUserInformationDialog(getRecordedUserByPhoneNumber, context);
                        }
                        AndroidNetworking.cancelAll();

                    }

                    @Override
                    public void onError(ANError anError) {


//                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });


    }

    private void showUserInformationDialog(GetRecordedUserByPhoneNumber getUserByPhoneNumberModelClass, Context context) {

        final AlertDialog alertCompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_dialo_user_information, null);


        TextView name = convertView.findViewById(R.id.nameTV);
        TextView phone = convertView.findViewById(R.id.phoneTV);
        TextView address = convertView.findViewById(R.id.addressTV);
        CircleImageView imageViewAlert = convertView.findViewById(R.id.imageView);
        Button cancelBtn = convertView.findViewById(R.id.cancelBtn);
        Button addedBtn = convertView.findViewById(R.id.addedBtn);

        alertCompany.setView(convertView);

        cancelBtn.setOnClickListener(v -> {
            alertCompany.dismiss();
        });

        if (getUserByPhoneNumberModelClass.getData().getImage() != null && !getUserByPhoneNumberModelClass.getData().getImage().isEmpty()) {

            Picasso.get().load(getUserByPhoneNumberModelClass.getData().getImage()).fit().placeholder(R.drawable.progress_animation).error(R.drawable.male1).into(imageViewAlert);

        }
        name.setText(getUserByPhoneNumberModelClass.getData().getName());
        phone.setText(getUserByPhoneNumberModelClass.getData().getContact());
        address.setText(getUserByPhoneNumberModelClass.getData().getAddress());

        addedBtn.setOnClickListener(v -> {
            alertCompany.dismiss();

            mUserNameET.setText(getUserByPhoneNumberModelClass.getData().getName());
            mAddressET.setText(getUserByPhoneNumberModelClass.getData().getAddress());
            if (getUserByPhoneNumberModelClass.getData().getImage() != null && !getUserByPhoneNumberModelClass.getData().getImage().isEmpty()) {

                imageLinkPre=getUserByPhoneNumberModelClass.getData().getImage();
                bitmap=null;

                Picasso.get().load(getUserByPhoneNumberModelClass.getData().getImage()).fit().placeholder(R.drawable.progress_animation).error(R.drawable.male1).into(mUserPhotoIV);
            }


        });

        alertCompany.show();

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        getVisitorWaitingList();
    }

    private void getVisitorWaitingList() {


        Map<String, Object> dataPost = new HashMap<>();

        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole",1);
        dataPost.put("buildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("communityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("flatId", 0);
        dataPost.put("status", StaticData.PENDING_PERMISSION);
        dataPost.put("fromDate", "");
        dataPost.put("toDate", "");



        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.getVisitors;

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);
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

                        Log.e(TAG, "onResponse: =   " + response);

                        Gson gson = new Gson();
                        GetVisitorInsideModelClass getVisitorInsideModelClass = gson.fromJson(String.valueOf(response), GetVisitorInsideModelClass.class);

                        VisitorWaitingListAdapter visitorWaitingListAdapter = new VisitorWaitingListAdapter((ArrayList<GetInsideVisitorData>) getVisitorInsideModelClass.getData(), context);
                        visitorWaitingListAdapter.setHasStableIds(true);

                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                        mVisitorPendingListRecycelrViewID.setLayoutManager(layoutManager);

                        mVisitorPendingListRecycelrViewID.setVisibility(View.VISIBLE);
                        mVisitorPendingListRecycelrViewID.setAdapter(visitorWaitingListAdapter);

                        AndroidNetworking.cancelAll();

                    }

                    @Override
                    public void onError(ANError anError) {


                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });


    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void initView() {
        mVisitorPendingListRecycelrViewID = (RecyclerView) findViewById(R.id.visitorPendingListRecycelrViewID);
        mUserPhotoIV = (CircleImageView) findViewById(R.id.user_photoIV);
        mUserPhotoIV.setOnClickListener(this);
        mPhoneNoET = (TextInputEditText) findViewById(R.id.phone_noET);
        mUserNameET = (TextInputEditText) findViewById(R.id.user_nameET);
        mFlatNumberET = findViewById(R.id.flatNumberET);
        mFlatNumberET.setOnClickListener(this);
        mPuposeET = findViewById(R.id.puposeET);
        mPuposeET.setOnClickListener(this);
        mAddressET = findViewById(R.id.addressET);
        mSubmitUserInfoBtn = (Button) findViewById(R.id.SubmitUserInfoBtn);
        mSubmitUserInfoBtn.setOnClickListener(this);
        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);

        imageUploadTV = findViewById(R.id.imageUploadTV);
        imageUploadTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.user_photoIV:
            case R.id.imageUploadTV:
                StaticData.selectImage(AddVisitorActivity.this);
                break;
            case R.id.flatNumberET:
                showAllFlats();
                break;
            case R.id.puposeET:
                showAllParcelTypes();
                break;
            case R.id.SubmitUserInfoBtn:

                checkValidation();

                break;
        }
    }

    private void checkValidation() {


        if (mPhoneNoET.getText().toString().isEmpty()) {
            mPhoneNoET.requestFocus();
            mPhoneNoET.setError("Add phone Number");
            return;
        }
        if (!normalfunc.isvalidphone(mPhoneNoET.getText().toString())) {
            mPhoneNoET.requestFocus();
            mPhoneNoET.setError("Add a valid phone Number");
            return;
        }
        if (mUserNameET.getText().toString().isEmpty()) {
            mUserNameET.requestFocus();
            mUserNameET.setError("Add User Name");
            return;
        }
        if (mFlatNumberET.getText().toString().isEmpty()) {
            mFlatNumberET.requestFocus();
            mFlatNumberET.setError("Add Flat Number");
            return;
        }
        if (mPuposeET.getText().toString().isEmpty()) {
            mPuposeET.requestFocus();
            mPuposeET.setError("Add Purpose");
            return;
        }

        if (bitmap == null) {
            uploadDataData("");

        } else {

            fullScreenAlertDialog.showdialog();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            Uri uri = Uri.parse(path);

            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            File file = new File(cursor.getString(idx));

            Log.e(TAG, "uploadImage: File Path =  " + file);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            Log.e(TAG, "onClick: currentDateandTime =  " + currentDateandTime);
            Log.e(TAG, "onClick: currentDateandTime =  " + sharedPrefHelper.getString(StaticData.JWT_TOKEN));

            String imageUploadUrl = StaticData.baseURL+StaticData.imageUploadURL;

            AndroidNetworking.upload(imageUploadUrl)
                    .addHeaders("jwtTokenHeader",sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                    .addMultipartFile("image", file)// posting any type of file
                    .addMultipartParameter("folderName", "visitors")
                    .addMultipartParameter("subFolderName", sharedPrefHelper.getString(StaticData.BUILD_ID))
                    .addMultipartParameter("fileName", currentDateandTime)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String imageDownloadLink = response.getString("data");


                                Log.e(TAG, "onResponse: imageDownloadLink " + imageDownloadLink);
                                uploadDataData(imageDownloadLink);

                            } catch (JSONException e) {
                                fullScreenAlertDialog.dismissdialog();
                                StaticData.showErrorAlertDialog(context, "Error !", "আবার চেষ্টা করুন । ");
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            fullScreenAlertDialog.dismissdialog();

                            Toast.makeText(context, "Image Upload Problem wait some Time Later", Toast.LENGTH_SHORT).show();

                            Log.e("TAG = ", "onError: " + error.getErrorBody());
                            Log.e("TAG = ", "onError: " + error.getMessage());
                            Log.e("TAG = ", "onError: " + error);
                        }
                    });


        }

    }

    private void uploadDataData(String imageLink) {

        if (!imageLinkPre.isEmpty() && bitmap==null){
            imageLink=imageLinkPre;
        }

        fullScreenAlertDialog.showdialog();


        try {

            JSONObject dataPost = new JSONObject();
            dataPost.put("limit", "");
            dataPost.put("pageId", "");
            dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
            dataPost.put("requesterFlatId", 0);
            dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
            dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
            dataPost.put("requesterUserRole", 1);
            dataPost.put("name", mUserNameET.getText().toString());
            dataPost.put("address", mAddressET.getText().toString());
            dataPost.put("contact", mPhoneNoET.getText().toString());
            dataPost.put("email", "");
            dataPost.put("purpose", mPuposeET.getText().toString());
            dataPost.put("image", imageLink);
            dataPost.put("thumbImage", imageLink);
            dataPost.put("communityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
            dataPost.put("buildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
            dataPost.put("flatId", historyFlats.get(0).getId());
            dataPost.put("guardId", Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ID)));
            dataPost.put("responderId", 0);


            String addVisitorUrl = StaticData.baseURL + "" + StaticData.addVisitor;

            Log.e("TAG", "onCreate: " + dataPost);
            Log.e("TAG", "onCreate: " + addVisitorUrl);
            Log.e("TAG", "onCreate: ---------------------- ");

            AndroidNetworking.post(addVisitorUrl)
                    .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                    .setContentType("application/json")
                    .addJSONObjectBody(dataPost)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            fullScreenAlertDialog.dismissdialog();

                            Log.e(TAG, "onResponse: =  =----------- " + response);

                            Gson gson = new Gson();
                            AddVisitorResponse addVisitorResponse = gson.fromJson(String.valueOf(response), AddVisitorResponse.class);

                            if (addVisitorResponse.getData().getMessage().equals(StaticData.WHITE_LISTED)){

                                showAlertDialog(StaticData.WHITE_LISTED,context,addVisitorResponse);

                            }else if (addVisitorResponse.getData().getMessage().equals(StaticData.BLACK_LISTED)){
                                showAlertDialog(StaticData.BLACK_LISTED,context,addVisitorResponse);


                            }else if (addVisitorResponse.getData().getMessage().equals(StaticData.NO_SPECIALITY)){

                                try {
                                    StaticData.showSuccessDialog((FragmentActivity) context, "Alert !", "অতিথি কে অ্যাড করা হয়েছে । নিশ্চিত হওয়ার জন্য অপেক্ষা করুন ");

                                }catch (Exception e){

                                }
                            }else {
                                //No FLat User Found
                                showAlertDialog(StaticData.NO_FLAT_MEMBER,context,addVisitorResponse);


                            }

                            AndroidNetworking.cancelAll();


                        }

                        @Override
                        public void onError(ANError anError) {

                            fullScreenAlertDialog.dismissdialog();

                            StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                            Log.e(TAG, "onResponse: error message =  " + anError.getMessage());
                            Log.e(TAG, "onResponse: error code =  " + anError.getErrorCode());
                            Log.e(TAG, "onResponse: error body =  " + anError.getErrorBody());
                            Log.e(TAG, "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                        }
                    });


        } catch (Exception e) {
            fullScreenAlertDialog.dismissdialog();

            Log.e(TAG, "uploadDataData: Visitor add failed for jsonObject = " + e.getMessage());

        }


    }

    private void showAlertDialog(String visitorStatus, Context context, AddVisitorResponse addVisitorResponse) {



        if (visitorStatus.equals(StaticData.WHITE_LISTED)){

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom_white_list_dialog, null);
            Button okWhiteButton=convertView.findViewById(R.id.okWhitelist);
            alertDialog.setView(convertView);
            alertDialog.setCancelable(false);

            okWhiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDialog.dismiss();
                    startActivity(new Intent(context,AddVisitorActivity.class));
                    finish();

                }
            });

            alertDialog.show();


        }else if (visitorStatus.equals(StaticData.BLACK_LISTED)){

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom_black_list_dialog, null);
            Button okBlackButton=convertView.findViewById(R.id.okBlacklist);
            alertDialog.setView(convertView);
            alertDialog.setCancelable(false);

            okBlackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    alertDialog.dismiss();
                    startActivity(new Intent(context,AddVisitorActivity.class));
                    finish();

                }
            });

            alertDialog.show();

        }else if (visitorStatus.equals(StaticData.NO_FLAT_MEMBER)){

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom_no_flat_user_dialog, null);
            Button okNoFlatUserBtn=convertView.findViewById(R.id.okNoFlatUserBtn);
            alertDialog.setView(convertView);
            alertDialog.setCancelable(false);

            okNoFlatUserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    alertDialog.dismiss();
                    startActivity(new Intent(context,AddVisitorActivity.class));
                    finish();

                }
            });

            alertDialog.show();

        }

    }

    public void showAllParcelTypes() {

        ArrayList<String> parcelTypes = new ArrayList<>();

        parcelTypes.add("Visit");
        parcelTypes.add("other");


        final TypesAdapter valueAdapter = new TypesAdapter(parcelTypes, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_percel_list, null);
        final TextInputEditText editText = convertView.findViewById(R.id.sear);


        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.SubmitUserInfoBtn);
        alertcompany.setView(convertView);
        alertcompany.setCancelable(true);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(valueAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPuposeET.setText(editText.getText().toString());
                alertcompany.dismiss();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                valueAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String typeselected = (String) lv.getItemAtPosition(position);
                mPuposeET.setText(typeselected);
                alertcompany.dismiss();
            }
        });

    }

    public void showAllFlats() {


        Gson gson = new Gson();
        String json = sharedPrefHelper.getString(StaticData.ALL_FLATS);

        ActiveFlatsModelClass activeFlat = gson.fromJson(json, ActiveFlatsModelClass.class);

        if (activeFlat != null) {
            final ActiveFlatAdapter activeFlatAdapter = new ActiveFlatAdapter(activeFlat, context);
            final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom_list_multiple, null);
            final TextInputEditText editText = convertView.findViewById(R.id.sear);
            //change Listview to Gridview
            final GridView lv = (GridView) convertView.findViewById(R.id.listView1);
            final Button done = convertView.findViewById(R.id.SubmitUserInfoBtn);
            final Button selectbutton = convertView.findViewById(R.id.select);
            final Button unselectbutton = convertView.findViewById(R.id.deselect);
            final TextView tt = convertView.findViewById(R.id.selected);
            tt.setMovementMethod(new ScrollingMovementMethod());
            totaltext = "";


            alertcompany.setView(convertView);
            alertcompany.setCancelable(true);
            //valueAdapter.notifyDataSetChanged();

            lv.setAdapter(activeFlatAdapter);
            alertcompany.show();

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFlatNumberET.setText(totaltext);
                    alertcompany.dismiss();
                }
            });

            selectbutton.setVisibility(View.GONE);
            unselectbutton.setVisibility(View.GONE);
            /*
            selectbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyFlats.clear();
                    totaltext = "";
                    tt.setText(totaltext);

                    //add again
                    for (int i = 0; i < activeFlat.getData().size(); i++) {

                        activeFlatAdapter.changedata(activeFlat.getData().get(i).getNumber(), true);
                        activeFlatAdapter.notifyDataSetChanged();
                        historyFlats.add(activeFlat.getData().get(i));
                        totaltext = totaltext + " " + activeFlat.getData().get(i).getNumber() + " ";
                        unselectbutton.setVisibility(View.VISIBLE);
                        selectbutton.setVisibility(View.GONE);
                    }
                    tt.setText(totaltext);

                }
            });

            unselectbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < activeFlat.getData().size(); i++) {

                        activeFlatAdapter.changedata(activeFlat.getData().get(i).getNumber(), false);
                        activeFlatAdapter.notifyDataSetChanged();
                        historyFlats.remove(activeFlat.getData().get(i));
                        totaltext = totaltext.replace(" " + activeFlat.getData().get(i).getNumber(), " ");
                        unselectbutton.setVisibility(View.GONE);
                        selectbutton.setVisibility(View.VISIBLE);
                    }
                    totaltext = "";
                    tt.setText(totaltext);
                    historyFlats.clear();
                }
            });
*/
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    activeFlatAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    ActiveFlatData ss = (ActiveFlatData) lv.getItemAtPosition(position);

                  /*  if (!historyFlats.contains(ss)) {
                        activeFlatAdapter.changedata(ss.getNumber(), true);
                        historyFlats.add(ss);
                        activeFlatAdapter.notifyDataSetChanged();
                        totaltext = totaltext + "  " + ss.getNumber();

                    } else {
                        activeFlatAdapter.changedata(ss.getNumber(), false);
                        historyFlats.remove(ss);


                        totaltext = "";
                        //add again
                        for (int i = 0; i < historyFlats.size(); i++) {

                            totaltext = totaltext + " " + historyFlats.get(i).getNumber() + " ";

                        }

                        activeFlatAdapter.notifyDataSetChanged();

                    }*/

                    historyFlats.clear();
                    historyFlats.add(ss);
                    totaltext = totaltext + "  " + ss.getNumber();
                    mFlatNumberET.setText(totaltext);

                    alertcompany.dismiss();


                }
            });
        } else {
            Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            mUserPhotoIV.setImageURI(null);

            mFileUri = r.getUri().toString();
            bitmap = r.getBitmap();

            mUserPhotoIV.setImageURI(r.getUri());

        } else {

            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
