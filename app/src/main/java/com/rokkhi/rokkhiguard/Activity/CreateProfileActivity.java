package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.ActiveFlatAdapter;
import com.rokkhi.rokkhiguard.Adapter.TypesAdapter;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatData;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatsModelClass;
import com.rokkhi.rokkhiguard.Model.api.AssignRoleToUserDataModelPost;
import com.rokkhi.rokkhiguard.Model.api.AssignRoleToUserServiceWorkerResponse;
import com.rokkhi.rokkhiguard.Model.api.DataFlatInfoPost;
import com.rokkhi.rokkhiguard.Model.api.RegisterUserModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity implements IPickResult, View.OnClickListener {
    private static final String TAG = "CreateProfileActivity";
    private CircleImageView userPhotoImageView;
    Context context;
    /**
     * নাম
     */
    private TextInputEditText mUserName;
    /**
     * মোবাইল নাম্বার
     */
    private TextInputEditText mUserPhoneET;
    /**
     * কি ধরনের প্রোফাইল
     */
    private TextInputEditText mUserWtype;
    /**
     * ফ্ল্যাট
     */
    private TextInputEditText UserFlatET;
    /**
     * Done ( শেষ )
     */
    private TextInputEditText userAddress;
    /**
     * Done ( শেষ )
     */
    private Button submitUserDataBtn;
    private ProgressBar mProgressBar;
    private TextView imageUploadTV;

    String mFileUri = "";
    private Bitmap bitmap = null;

    SharedPrefHelper sharedPrefHelper;
    FullScreenAlertDialog fullScreenAlertDialog;

    RegisterUserModelClass registerUserModelClass;
    AssignRoleToUserServiceWorkerResponse assignRoleToUserServiceWorkerResponse;


    String totaltext = "";

    ArrayList<ActiveFlatData> historyFlats;
    Normalfunc normalfunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        historyFlats = new ArrayList<>();
        normalfunc = new Normalfunc();

        AndroidNetworking.initialize(getApplicationContext());

        sharedPrefHelper = new SharedPrefHelper(context);
        fullScreenAlertDialog = new FullScreenAlertDialog(context);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            userPhotoImageView.setImageURI(null);

            mFileUri = r.getUri().toString();
            bitmap = r.getBitmap();

            userPhotoImageView.setImageURI(r.getUri());

        } else {

            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {

        userPhotoImageView = (CircleImageView) findViewById(R.id.user_photoIV);
        userPhotoImageView.setOnClickListener(this);
        mUserName = findViewById(R.id.user_nameET);
        mUserPhoneET = findViewById(R.id.user_Phone_ET);
        mUserWtype = findViewById(R.id.user_wtype);
        mUserWtype.setOnClickListener(this);
        userAddress = findViewById(R.id.user_address);
        UserFlatET = findViewById(R.id.user_flat);
        UserFlatET.setOnClickListener(this);
        submitUserDataBtn = (Button) findViewById(R.id.SubmitUserInfoBtn);
        submitUserDataBtn.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        context = CreateProfileActivity.this;
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

                StaticData.selectImage(CreateProfileActivity.this);

                break;

            case R.id.user_wtype:

                sWorkerType();

                break;
            case R.id.user_flat:
                showAllflats();
                break;
            case R.id.SubmitUserInfoBtn:
                checkValidation();
                break;
        }
    }

    public void showAllflats() {

        Gson gson = new Gson();
        String json = sharedPrefHelper.getString(StaticData.ALL_FLATS);
        ActiveFlatsModelClass activeFlat = gson.fromJson(json, ActiveFlatsModelClass.class);

        final ActiveFlatAdapter activeFlatAdapter = new ActiveFlatAdapter(activeFlat, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_list_multiple, null);
        final EditText editText = convertView.findViewById(R.id.sear);
        //change Listview to Gridview
        final GridView lv = (GridView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.SubmitUserInfoBtn);
        final Button selectbutton = convertView.findViewById(R.id.select);
        final Button unselectbutton = convertView.findViewById(R.id.deselect);
        final TextView tt = convertView.findViewById(R.id.selected);
        tt.setMovementMethod(new ScrollingMovementMethod());
        tt.setVisibility(View.VISIBLE);
        totaltext = "";
        historyFlats.clear();


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFlatET.setText(totaltext);
                for (ActiveFlatData flatID :historyFlats){
                    Log.e(TAG, "onClick: flat id =  "+flatID.getId() );
                    Log.e(TAG, "onClick: flat name = "+flatID.getName() );
                }
                alertcompany.dismiss();
            }
        });

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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                activeFlatAdapter.getFilter().filter(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ActiveFlatData ss = (ActiveFlatData) lv.getItemAtPosition(position);

                //selected na hoile selected er moto kaj korbe.. selection er subidhar jnno

                if (!historyFlats.contains(ss)) {

                    activeFlatAdapter.changedata(ss.getNumber(), true);
                    historyFlats.add(ss);
                    activeFlatAdapter.notifyDataSetChanged();
                    totaltext = totaltext + "  " + ss.getNumber();
                    tt.setText(totaltext);

                } else {
                    activeFlatAdapter.changedata(ss.getNumber(), false);
                    historyFlats.remove(ss);


                    totaltext = "";
                    //add again
                    for (int i = 0; i < historyFlats.size(); i++) {

//                        activeFlatAdapter.changedata(activeFlats.get(i).getF_no(), true);
//                        activeFlatAdapter.notifyDataSetChanged();
//                        historyFlats.add(activeFlats.get(i));
                        totaltext = totaltext + " " + historyFlats.get(i).getNumber() + " ";

                    }
//                    tt.setText(totaltext);


//                    totaltext = totaltext.replace(ss.getF_no(), "");

                    activeFlatAdapter.notifyDataSetChanged();
                    tt.setText(totaltext);

                }


            }
        });
       /* totaltext = "";


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFlatET.setText(totaltext);
                alertcompany.dismiss();
            }
        });

        selectbutton.setVisibility(View.GONE);
        unselectbutton.setVisibility(View.GONE);


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
                historyFlats.clear();
                historyFlats.add(ss);
                totaltext = totaltext + "  " + ss.getNumber();

                UserFlatET.setText(totaltext);
                alertcompany.dismiss();

              *//*

                if (!historyFlats.contains(ss)) {

                    activeFlatAdapter.changedata(ss.getNumber(), true);
                    historyFlats.add(ss);
                    activeFlatAdapter.notifyDataSetChanged();
                    totaltext = totaltext + "  " + ss.getNumber();
                    tt.setText(totaltext);

                } else {
                    activeFlatAdapter.changedata(ss.getNumber(), false);
                    historyFlats.remove(ss);


                    totaltext = "";
                    //add again
                    for (int i = 0; i < historyFlats.size(); i++) {

                        totaltext = totaltext + " " + historyFlats.get(i).getNumber() + " ";

                    }

                    activeFlatAdapter.notifyDataSetChanged();
                    tt.setText(totaltext);

                }

*//*
            }
        });
*/
    }


    private void sWorkerType() {


        ArrayList<String> parcelTypes = new ArrayList<>();

        parcelTypes.add("গার্ড");
        parcelTypes.add("শিক্ষক");
        parcelTypes.add("সার্ভিস ওয়ার্কার ");
        parcelTypes.add("অন্যান্য ");


        final TypesAdapter valueAdapter = new TypesAdapter(parcelTypes, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_percel_list, null);
        final EditText editText = convertView.findViewById(R.id.sear);

        //change lsitview to gridview

        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.SubmitUserInfoBtn);
        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(valueAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserWtype.setText(editText.getText().toString());
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
                //cname.setText(myoffice.getName());
                mUserWtype.setText(typeselected);
                alertcompany.dismiss();
            }
        });
    }

    private void checkValidation() {

        if (mUserName.getText().toString().isEmpty()) {
            mUserName.requestFocus();
            mUserName.setError("Worker Name ?");
            return;
        }
        if (mUserPhoneET.getText().toString().isEmpty()) {
            mUserPhoneET.requestFocus();
            mUserPhoneET.setError("Worker phone Number ?");
            return;
        }

        if (!normalfunc.isvalidphone(mUserPhoneET.getText().toString())) {
            mUserPhoneET.requestFocus();
            mUserPhoneET.setError("Add a valid phone Number");
            return;
        }
        if (mUserWtype.getText().toString().isEmpty()) {
            mUserWtype.requestFocus();
            mUserWtype.setError("Worker Type ?");
            return;
        }
        if (UserFlatET.getText().toString().isEmpty()) {
            UserFlatET.requestFocus();
            UserFlatET.setError("Worker Flat ?");
            return;
        }
        fullScreenAlertDialog.showdialog();

        if (bitmap == null) {
            uploadDataData("");
        } else {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            Uri uri = Uri.parse(path);
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            File file = new File(cursor.getString(idx));
            Log.e("TAG", "uploadImage: File Path =  " + file);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            Log.e("TAG", "onClick: currentDateandTime =  " + currentDateandTime);

            String imageUploadUrl = StaticData.baseURL + StaticData.imageUploadURL;

            AndroidNetworking.upload(imageUploadUrl)
                    .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                    .addMultipartFile("image", file)// posting any type of file
                    .addMultipartParameter("folderName", "serviceWorkers")
                    .addMultipartParameter("subFolderName", sharedPrefHelper.getString(StaticData.BUILD_ID))
                    .addMultipartParameter("fileName", currentDateandTime)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String imageDownloadLink = response.getString("data");

                                Log.e("TAG", "onResponse: imageDownloadLink " + imageDownloadLink);

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


    private void uploadDataData(String imageDownloadLink) {

        Map<String, Object> dataPost = new HashMap<>();
        dataPost.put("limit","");
        dataPost.put("pageId","");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole", Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ROLE)));
        dataPost.put("userId", "");
        dataPost.put("name", mUserName.getText().toString());
        dataPost.put("userRoleCode", StaticData.SERVICE_WORKER.toString());
        dataPost.put("contactPersonPhone", "");
        dataPost.put("contactPersonName", "");
        dataPost.put("address", userAddress.getText().toString());
        dataPost.put("email", "");

        dataPost.put("phone", mUserPhoneET.getText().toString());
        dataPost.put("gender", "");
        dataPost.put("organization", "");
        dataPost.put("nid", "");
        dataPost.put("password", "");
        dataPost.put("age", 0);
        dataPost.put("image", imageDownloadLink);
        dataPost.put("thumbImage", imageDownloadLink);
        dataPost.put("flatId", 0);
        dataPost.put("communityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("buildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("update", false);
        JSONObject jsonDataPost = new JSONObject(dataPost);




        String registerOrUpdateUrl = StaticData.baseURL + "" + StaticData.registerOrUpdate;

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + registerOrUpdateUrl);
        Log.e("TAG", "onCreate: ---------------------- ");


        AndroidNetworking.post(registerOrUpdateUrl)
                .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("TAG", "onResponse: = service worker added =----------- " + response);

                        Gson gson = new Gson();
                        registerUserModelClass = gson.fromJson(String.valueOf(response), RegisterUserModelClass.class);
                        Log.e(TAG, "onResponse: register id = " + registerUserModelClass.getData().getId());
                        assignRoleToUserBeta(String.valueOf(registerUserModelClass.getData().getId()), historyFlats);


                    }

                    @Override
                    public void onError(ANError anError) {

                        fullScreenAlertDialog.dismissdialog();

                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message registerOrUpdate =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code registerOrUpdate =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body registerOrUpdate =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail registerOrUpdate =  " + anError.getErrorDetail());
                    }
                });


    }

    private void assignRoleToUserBeta(String sWorkerId, ArrayList<ActiveFlatData> historyFlats) {

        List<Integer> flatListID = new ArrayList<>();
        List<DataFlatInfoPost> dataFlatInfoPostList = new ArrayList<>();

        for (ActiveFlatData activeFlatData : historyFlats) {
            flatListID.add(activeFlatData.getId());
        }

        DataFlatInfoPost dataFlatInfoPost = new DataFlatInfoPost(Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)), flatListID, StaticData.SERVICE_WORKER.toString());
        dataFlatInfoPostList.add(dataFlatInfoPost);

        AssignRoleToUserDataModelPost assignRoleToUserDataModelPost = new AssignRoleToUserDataModelPost(
                sharedPrefHelper.getString(StaticData.TIME_ZONE),
                0,
                Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)),
                Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)),
                Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ROLE)),
                dataFlatInfoPostList,
                "",
                "",
                StaticData.SERVICE_WORKER.toString(),
                Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)),
                Integer.parseInt(sWorkerId));


        Gson gson = new Gson();
        String jsonStr = gson.toJson(assignRoleToUserDataModelPost);


        try {
            JSONObject jsonDataPost = new JSONObject(jsonStr);
            Log.e(TAG, "assignRoleToUserBeta: jsonStr = " + jsonDataPost);

            String assignRoleToUserBetaUrl = StaticData.baseURL + "" + StaticData.assignRoleToUserBeta;

            Log.e("TAG", "onCreate: " + assignRoleToUserBetaUrl);


            AndroidNetworking.post(assignRoleToUserBetaUrl)
                    .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                    .setContentType("application/json")
                    .addJSONObjectBody(jsonDataPost)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            fullScreenAlertDialog.dismissdialog();


                            Log.e("TAG", "onResponse: = service worker added 2nd call  =----------- " + response);

                           /* Gson gson = new Gson();
                            assignRoleToUserServiceWorkerResponse = gson.fromJson(String.valueOf(response), AssignRoleToUserServiceWorkerResponse.class);*/

                            Log.e(TAG, "onResponse: register id = " + registerUserModelClass.getData().getId());

                            StaticData.showSuccessDialog(CreateProfileActivity.this, "ইনফর্মেশন !", "প্রোফাইলটি তৈরি করা সম্পূর্ণ হয়েছে । ");

                            AndroidNetworking.cancelAll();
                        }

                        @Override
                        public void onError(ANError anError) {

                            fullScreenAlertDialog.dismissdialog();

                            StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                            Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                            Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                            Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                            Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                        }
                    });

        } catch (JSONException e) {
            Log.e(TAG, "assignRoleToUserBeta: jsonStr = " + e.getMessage());

            e.printStackTrace();
        }


    }


}
