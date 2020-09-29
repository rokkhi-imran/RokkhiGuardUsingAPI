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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Adapter.ActiveFlatAdapter;
import com.rokkhi.rokkhiguard.Adapter.TypesAdapter;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatData;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatsModelClass;
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


public class AddVisitorActivity extends AppCompatActivity implements View.OnClickListener , IPickResult {

    private static final String TAG = "AddVisitorActivity";
    Context context;
    private RecyclerView mVisitorPendingListRecycelrViewID;
    private CircleImageView mUserPhotoIV;

    private EditText mPhoneNoET;

    private EditText mUserNameET;

    private EditText mFlatNumberET;

    private EditText mPuposeET;

    private EditText mAddressET;


    private Button mSubmitUserInfoBtn;
    private ProgressBar mProgressBar1;

    String mFileUri = "";
    private Bitmap bitmap = null;


    ArrayList<ActiveFlatData>historyFlats;
    String totaltext="";
    FullScreenAlertDialog fullScreenAlertDialog;
    
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = AddVisitorActivity.this;
        historyFlats=new ArrayList<>();
        AndroidNetworking.initialize(getApplicationContext());
        sharedPrefHelper=new SharedPrefHelper(context);
        fullScreenAlertDialog=new FullScreenAlertDialog(context);

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
        mPhoneNoET = (EditText) findViewById(R.id.phone_noET);
        mUserNameET = (EditText) findViewById(R.id.user_nameET);
        mFlatNumberET = (EditText) findViewById(R.id.flatNumberET);
        mFlatNumberET.setOnClickListener(this);
        mPuposeET = (EditText) findViewById(R.id.puposeET);
        mPuposeET.setOnClickListener(this);
        mAddressET = (EditText) findViewById(R.id.addressET);
        mSubmitUserInfoBtn = (Button) findViewById(R.id.SubmitUserInfoBtn);
        mSubmitUserInfoBtn.setOnClickListener(this);
        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.user_photoIV:
                StaticData.selectImage(AddVisitorActivity.this);
                break;
            case R.id.flatNumberET:
                showAllflats();
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

        if (mPhoneNoET.getText().toString().isEmpty()){
            mPhoneNoET.requestFocus();
            mPhoneNoET.setError("Add phone Number");
            return;
        }
        if (mUserNameET.getText().toString().isEmpty()){
            mUserNameET.requestFocus();
            mUserNameET.setError("Add User Name");
            return;
        }
        if (mFlatNumberET.getText().toString().isEmpty()){
            mFlatNumberET.requestFocus();
            mFlatNumberET.setError("Add Flat Number");
            return;
        }
        if (mPuposeET.getText().toString().isEmpty()){
            mPuposeET.requestFocus();
            mPuposeET.setError("Add Purpose");
            return;
        }

        if (bitmap==null){
            uploadDataData("");

        }else {

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
            Log.e(TAG, "onClick: currentDateandTime =  "+currentDateandTime);

            AndroidNetworking.upload(StaticData.imageUploadURL)
                    .addMultipartFile("image", file)// posting any type of file
                    .addMultipartParameter("folder", "visitors")
                    .addMultipartParameter("subfolder", sharedPrefHelper.getString(StaticData.BUILD_ID))
                    .addMultipartParameter("filename", currentDateandTime)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String imageDownloadLink = response.getString("url");


                                Log.e(TAG, "onResponse: imageDownloadLink "+imageDownloadLink );

                                uploadDataData(imageDownloadLink);

                            } catch (JSONException e) {
                                fullScreenAlertDialog.dismissdialog();
                                StaticData.showErrorAlertDialog(context,"Error !","আবার চেষ্টা করুন । ");
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

fullScreenAlertDialog.showdialog();
        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("name",mUserNameET.getText().toString());
        dataPost.put("address",  mAddressET.getText().toString());
        dataPost.put("contact", mPhoneNoET.getText().toString());
        dataPost.put("email", "");
        dataPost.put("purpose", mPuposeET.getText().toString() );

        dataPost.put("image", imageLink);
        dataPost.put("thumbImage", imageLink);
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("buildingId", sharedPrefHelper.getString(StaticData.BUILD_ID));
        dataPost.put("flatId",String.valueOf(historyFlats.get(0).getId()) );
        dataPost.put("guardId", sharedPrefHelper.getString(StaticData.USER_ID));
        dataPost.put("responderId"," " );

        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.addVisitor;
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


                        Log.e(TAG, "onResponse: =  =----------- " + response);


                        StaticData.showSuccessDialog((FragmentActivity) context,"Alert !","Visitor Added ..");

                    }

                    @Override
                    public void onError(ANError anError) {

                        fullScreenAlertDialog.dismissdialog();

                        StaticData.showErrorAlertDialog(context,"Alert !","আবার চেষ্টা করুন ।");

                        Log.e(TAG, "onResponse: error message =  " + anError.getMessage());
                        Log.e(TAG, "onResponse: error code =  " + anError.getErrorCode());
                        Log.e(TAG, "onResponse: error body =  " + anError.getErrorBody());
                        Log.e(TAG, "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });



    }

    public void showAllParcelTypes() {

        ArrayList<String> parcelTypes=new ArrayList<>();

        parcelTypes.add("Visit");
        parcelTypes.add("other");


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
                mPuposeET.setText(editText.getText().toString());
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


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
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

                if(!historyFlats.contains(ss)) {
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

                }
                tt.setText(totaltext);


            }
        });

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
