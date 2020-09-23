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
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.ActiveFlatAdapter;
import com.rokkhi.rokkhiguard.Adapter.TypesAdapter;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatData;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatsModelClass;
import com.rokkhi.rokkhiguard.Model.api.RegisterUserModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
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
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity implements IPickResult, View.OnClickListener {


    private CircleImageView userPhotoImageView;
    Context context;
    /**
     * নাম
     */
    private EditText mUserName;
    /**
     * মোবাইল নাম্বার
     */
    private EditText mUserPhoneET;
    /**
     * কি ধরনের প্রোফাইল
     */
    private EditText mUserWtype;
    /**
     * ফ্ল্যাট
     */
    private EditText UserFlatET;
    /**
     * Done ( শেষ )
     */
    private Button submitUserDataBtn;
    private ProgressBar mProgressBar;

    String mFileUri = "";
    private Bitmap bitmap = null;

    SharedPrefHelper sharedPrefHelper;
    FullScreenAlertDialog fullScreenAlertDialog;

    RegisterUserModelClass registerUserModelClass;


    String totaltext="";

    ArrayList<ActiveFlatData>historyFlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        historyFlats=new ArrayList<>();

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
        mUserName = (EditText) findViewById(R.id.user_nameET);
        mUserPhoneET = (EditText) findViewById(R.id.user_Phone_ET);
        mUserWtype = (EditText) findViewById(R.id.user_wtype);
        mUserWtype.setOnClickListener(this);
        UserFlatET = (EditText) findViewById(R.id.user_flat);
        UserFlatET.setOnClickListener(this);
        submitUserDataBtn = (Button) findViewById(R.id.SubmitUserInfoBtn);
        submitUserDataBtn.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        context = CreateProfileActivity.this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.user_photoIV:

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

        ActiveFlatData activeFlatData=new ActiveFlatData(
                "01",
                "",
                "",
                "",
                "",
                0,
                true,false,"Flat 1",
                "6",
                1202,
                2,
                3,
                2,
                ""
        );
        ArrayList<ActiveFlatData> activeFlatDataArrayList=new ArrayList<>();

        activeFlatDataArrayList.add(activeFlatData);

        ActiveFlatsModelClass activeFlat=new ActiveFlatsModelClass(activeFlatDataArrayList,activeFlatDataArrayList,"Success",200);




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


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFlatET.setText(totaltext);
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

                        totaltext = totaltext + " " + historyFlats.get(i).getNumber() + " ";

                    }

                    activeFlatAdapter.notifyDataSetChanged();
                    tt.setText(totaltext);

                }


            }
        });

    }


    private void sWorkerType() {


        ArrayList<String> parcelTypes=new ArrayList<>();

        parcelTypes.add("গার্ড");
        parcelTypes.add("হোম");
        parcelTypes.add("অন্যন্য");


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


            AndroidNetworking.upload(StaticData.imageUploadURL)
                    .addMultipartFile("image", file)// posting any type of file
                    .addMultipartParameter("folder", "sworkers")
                    .addMultipartParameter("subfolder", sharedPrefHelper.getString(StaticData.BUILD_ID))
                    .addMultipartParameter("filename", currentDateandTime)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String imageDownloadLink = response.getString("url");

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

        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("userId", "");
        dataPost.put("name", mUserName.getText().toString());
        dataPost.put("userRoleCode", StaticData.SERVICE_WORKER.toString());
        dataPost.put("address", "");
        dataPost.put("email", "");
        dataPost.put("phone", mUserPhoneET.getText().toString());
        dataPost.put("gender", "");
        dataPost.put("organization", "");
        dataPost.put("nid", "");
        dataPost.put("password", "");
        dataPost.put("image", imageDownloadLink);
        dataPost.put("thumbImage", imageDownloadLink);
        dataPost.put("flatId", sharedPrefHelper.getString(StaticData.FLAT_ID));
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("buildingId", sharedPrefHelper.getString(StaticData.BUILD_ID));

        JSONObject jsonDataPost = new JSONObject(dataPost);


        String url = StaticData.baseURL + "" + StaticData.registerOrUpdate;
        String token = sharedPrefHelper.getString(StaticData.KEY_FIREBASE_ID_TOKEN);

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);
        Log.e("TAG", "onCreate: " + token);
        Log.e("TAG", "onCreate: ---------------------- ");


        AndroidNetworking.post(url)
                .addHeaders("authtoken", token)
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fullScreenAlertDialog.dismissdialog();


                        Log.e("TAG", "onResponse: =  =----------- " + response);

                        Gson gson = new Gson();
                        registerUserModelClass = gson.fromJson(String.valueOf(response), RegisterUserModelClass.class);

                        StaticData.showSuccessDialog(CreateProfileActivity.this, "ইনফর্মেশন !", "প্রফাইল্টি তৈরি করা সম্পূর্ণ হয়েছে । ");

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

    }


}
