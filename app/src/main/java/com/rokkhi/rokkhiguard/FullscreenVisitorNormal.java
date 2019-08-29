package com.rokkhi.rokkhiguard;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.rokkhi.rokkhiguard.Model.Users;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;


import de.hdodenhof.circleimageview.CircleImageView;

public class FullscreenVisitorNormal extends AppCompatActivity {

    CircleImageView propic;
    private static final String TAG = "FullscreenVisitorNormal";
    ProgressBar progressBar;
    String whoadd="", subject="",body="",time="";
    int nid;
    String response;
    TextView nameview,bodytext,subjecttext,timetext;
    FirebaseFirestore firebaseFirestore;
    Vibrator v;
    Button okay;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_noti_forguards);
        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        context= FullscreenVisitorNormal.this;

        Log.d(TAG, "onCreate: hhhh ");
        propic= findViewById(R.id.user_photo);
        nameview= findViewById(R.id.name);
        okay= findViewById(R.id.okay);
        progressBar= findViewById(R.id.progressBar1);
        bodytext= findViewById(R.id.body);
        subjecttext= findViewById(R.id.subject);
        timetext= findViewById(R.id.time);


        if(firebaseAuth.getCurrentUser()==null)finish();

        firebaseFirestore= FirebaseFirestore.getInstance();

        onNewIntent(getIntent());
        setvibrator();


        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sesh();
                finish();
                //goParcelActivity();
            }
        });






    }



    public void setvibrator(){

        long[] pattern = {0, 500, 1000};

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createWaveform(pattern,-1));



    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressBar.setVisibility(View.GONE);
        sesh();
        if(v!=null)v.cancel();

    }



    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras != null){
            whoadd=intent.getStringExtra("who_add");
            subject=intent.getStringExtra("subject");
            //org=intent.getStringExtra("org");
            body= intent.getStringExtra("body");
            //omail= intent.getStringExtra("omail");
            time= intent.getStringExtra("time");
            nid= intent.getIntExtra("nid",0);


            firebaseFirestore.collection(getString(R.string.col_users)).document(whoadd)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot= task.getResult();
                        if(documentSnapshot.exists()){
                            Users users= documentSnapshot.toObject(Users.class);
                            nameview.setText(users.getName());
                            UniversalImageLoader.setImage(users.getThumb(), propic, null, "");
                        }
                    }
                }
            });

            subjecttext.setText(subject);
            bodytext.setText(body);
            timetext.setText(time);
        }


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON

        );
    }

    public void sesh(){
        Log.d(TAG, "sesh: "+ nid);
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(nid);
    }

}
