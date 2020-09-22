package com.rokkhi.rokkhiguard.Activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Types;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditWrokerProfileActivity extends AppCompatActivity {


    protected TextView userPhoneTV;
    protected Button generatepin;
    protected EditText userPin;
    protected EditText userName;
    protected EditText userWtype;
    protected EditText userFlat;
    protected Button done;
    CircleImageView userPhoto;
    Intent intent;
    Context context;
    String sID;
    String buildid = "";
    String commid = "";
    String total = "";
    Normalfunc normalfunc;
    private Bitmap bitmap = null;

    ArrayList<ActiveFlats> allflats;
    ArrayList<String> historyflatno;
    ArrayList<String> historyflatId;
    ArrayList<Types> types;

    Types typeselected;
    String flatName = "";

    private static final int TAKE_PICTURE_CODE = 101;
    private static final int PICK_IMAGE_REQUEST = 10;
    Intent cameraIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_edit_worker_profile);
        initView();
        context = EditWrokerProfileActivity.this;
        intent = getIntent();
        sID = intent.getStringExtra("s_id");
        normalfunc = new Normalfunc();
        Log.e("TAG", "onCreate: " + sID);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");


    }

    //upload End

    private void initView() {
        userPhoneTV = (TextView) findViewById(R.id.user_Phone_TV);
        generatepin = (Button) findViewById(R.id.generatepin);
        userPin = (EditText) findViewById(R.id.user_pin);
        userName = (EditText) findViewById(R.id.user_name);
        userWtype = (EditText) findViewById(R.id.user_wtype);
        userFlat = (EditText) findViewById(R.id.user_flat);
        userPhoto= findViewById(R.id.user_photo_IV);
        done = (Button) findViewById(R.id.SubmitUserInfoBtn);

        allflats = new ArrayList<>();
        historyflatno = new ArrayList<>();
        historyflatId = new ArrayList<>();
        types = new ArrayList<>();
        typeselected = new Types();


        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    public void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Photo Option..");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (options[which].equals("Take Photo")) {
                    cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, TAKE_PICTURE_CODE);
                } else if (options[which].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);


                Glide.with(getApplicationContext()).load(data.getData()).into(userPhoto);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == TAKE_PICTURE_CODE && resultCode == RESULT_OK ) {


            bitmap = (Bitmap) data.getExtras().get("data");

            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            Glide.with(getApplicationContext()).load(bitmap).into(userPhoto);


        }

    }
}