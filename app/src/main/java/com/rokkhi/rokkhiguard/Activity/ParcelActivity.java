package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.content.DialogInterface;
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
import com.rokkhi.rokkhiguard.Model.api.ParcelResponseModelClass;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ParcelActivity extends AppCompatActivity implements IPickResult {


    String totaltext = "";

    CircleImageView parcelphoto;
    EditText companyNameET, parcelTypeET, flatNumberET;
    Button parcelSubmitButton;
    String mFileUri = "";
    private Bitmap bitmap = null;
    Context context;
    private static final String TAG = "ParcelActivity";


    ProgressBar progressBar;
    Calendar myCalendar;
    Normalfunc normalfunc;
    FullScreenAlertDialog fullScreenAlertDialog;
    SharedPrefHelper sharedPrefHelper;
    private TextView imageUploadTV;

    ParcelResponseModelClass parcelResponseModelClass;

    ArrayList<ActiveFlatData> historyFlats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = ParcelActivity.this;
        normalfunc = new Normalfunc();
        fullScreenAlertDialog = new FullScreenAlertDialog(context);
        sharedPrefHelper = new SharedPrefHelper(context);
        historyFlats = new ArrayList<>();

        AndroidNetworking.initialize(getApplicationContext());


        parcelSubmitButton = findViewById(R.id.parcelSubmitButton);
        companyNameET = findViewById(R.id.companyNameET);
        flatNumberET = findViewById(R.id.flatNumberET);
        parcelTypeET = findViewById(R.id.parcelTypeET);
        parcelphoto = findViewById(R.id.parcel_photo);
        progressBar = findViewById(R.id.progressBar1);
        imageUploadTV=findViewById(R.id.imageUploadTV);
        myCalendar = Calendar.getInstance();

        parcelphoto.setOnClickListener(v -> StaticData.selectImage(ParcelActivity.this));

        imageUploadTV.setOnClickListener(v -> StaticData.selectImage(ParcelActivity.this));

        parcelSubmitButton.setOnClickListener(v -> {

            if (companyNameET.getText().toString().isEmpty()) {
                companyNameET.requestFocus();
                companyNameET.setError("Company Name ?");
                return;
            }

            if (flatNumberET.getText().toString().isEmpty()) {
                flatNumberET.requestFocus();
                flatNumberET.setError("Select flat name");
                return;
            }
            if (parcelTypeET.getText().toString().isEmpty()) {
                parcelTypeET.requestFocus();
                parcelTypeET.setError("Select parcel type");
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

                Log.e(TAG, "uploadImage: File Path =  " + file);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                Log.e(TAG, "onClick: currentDateandTime =  " + currentDateandTime);

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
        });

        parcelTypeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllParcelTypes();
            }
        });

        flatNumberET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllflats();
            }
        });

    }

    private void uploadDataData(String imageDownloadLink) {


        Map<String, Object> dataPost = new HashMap<>();
        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId",  Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole", Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ROLE)));
        dataPost.put("name", parcelTypeET.getText().toString());
        dataPost.put("company", companyNameET.getText().toString());
        dataPost.put("image", imageDownloadLink);
        dataPost.put("thumbImage", imageDownloadLink);
        dataPost.put("acknowledgedBy", "");
        dataPost.put("guardId", Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ID)));
        dataPost.put("buildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("flatId", historyFlats.get(0).getId());
        dataPost.put("communityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));




        JSONObject jsonDataPost = new JSONObject(dataPost);

        String url = StaticData.baseURL + "" + StaticData.addParcel;

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);
//        Log.e("TAG", "onCreate: " + token);
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

                        fullScreenAlertDialog.dismissdialog();


                        Log.e(TAG, "onResponse: =  =----------- " + response);

                        Gson gson = new Gson();
                        parcelResponseModelClass = gson.fromJson(String.valueOf(response), ParcelResponseModelClass.class);

                        showSuccessDialog();
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





    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Success !")
                .setMessage("Parcel Received Successfully.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void showAllParcelTypes() {

        ArrayList<String> parcelTypes = new ArrayList<>();

        parcelTypes.add("পার্সেল");
        parcelTypes.add("খাবার");
        parcelTypes.add("অন্যান্য");


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
                parcelTypeET.setText(editText.getText().toString());
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
                parcelTypeET.setText(typeselected);
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
        totaltext = "";


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flatNumberET.setText(totaltext);
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

                //selected na hoile selected er moto kaj korbe.. selection er subidhar jnno

                historyFlats.clear();
                historyFlats.add(ss);
                totaltext = totaltext + "  " + ss.getNumber();
                flatNumberET.setText(totaltext);
                alertcompany.dismiss();
/*

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
*/


            }
        });

    }


    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            parcelphoto.setImageURI(null);

            mFileUri = r.getUri().toString();
            bitmap = r.getBitmap();

            parcelphoto.setImageURI(r.getUri());

        } else {

            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
