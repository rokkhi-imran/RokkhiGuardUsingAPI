package com.rokkhi.rokkhiguard.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.rokkhi.rokkhiguard.Model.Types;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class ParcelActivity extends AppCompatActivity implements IPickResult{

    CircleImageView parcelphoto;
    EditText cname, gphone, ptype, flat;
    Button done;
    String mFileUri = "";
    Context context;
    ArrayList<Types> types;
    private Bitmap bitmap = null;
    private static final String TAG = "ParcelActivity";


    ProgressBar progressBar;
    Calendar myCalendar;
    Normalfunc normalfunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = ParcelActivity.this;
        normalfunc= new Normalfunc();

        AndroidNetworking.initialize(getApplicationContext());


        done = findViewById(R.id.done);
        cname = findViewById(R.id.cname);
        gphone = findViewById(R.id.gphone);
        flat = findViewById(R.id.flat);
        ptype = findViewById(R.id.type);
        parcelphoto = findViewById(R.id.parcel_photo);
        progressBar = findViewById(R.id.progressBar1);
        myCalendar = Calendar.getInstance();


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
