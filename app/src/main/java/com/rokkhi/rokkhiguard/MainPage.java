package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rokkhi.rokkhiguard.Model.Activebuilding;
import com.rokkhi.rokkhiguard.Model.Settings;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage extends AppCompatActivity {

    CircleImageView gatepass,logout,addvis,vislist,notice,parcel,create,invitee,vehicle,child;
    private static final String TAG = "MainPage";
    Context context;
    ImageButton settings;
    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    AlertDialog alertDialog;
    String  buildid = "", commid = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        Log.d(TAG, "onCreate: " + "xxx");

        Intent intent=getIntent();
        context= MainPage.this;


        gatepass= findViewById(R.id.gatepass);
        logout= findViewById(R.id.logout);
        addvis= findViewById(R.id.addvis);
        vislist= findViewById(R.id.vislist);
        notice= findViewById(R.id.notice);
        parcel= findViewById(R.id.parcel);
        create= findViewById(R.id.profile);
        invitee= findViewById(R.id.invitee);
        settings = findViewById(R.id.settings);
        vehicle = findViewById(R.id.vehicle);
        child = findViewById(R.id.child);

        firebaseFirestore= FirebaseFirestore.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor= sharedPref.edit();
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");


        firebaseFirestore.collection(getString(R.string.col_activebuild)).document(buildid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot= task.getResult();
                    if(documentSnapshot.exists()){
                        Activebuilding activebuilding= documentSnapshot.toObject(Activebuilding.class);
                        int floorno=activebuilding.getB_tfloor();
                        int flatno=activebuilding.getB_tflat();
                        editor.putInt("floorno",floorno);
                        editor.putInt("flatno",flatno);
                        editor.apply();
                    }
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DaroanPass.class);
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
                Intent intent = new Intent(context, ChildrenList.class);
                startActivity(intent);
            }
        });

        gatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainPage.this,GatePass.class);
                startActivity(intent);
            }
        });
        addvis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainPage.this,AddVisitor.class);
                startActivity(intent);
            }
        });
        vislist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainPage.this,VisitorsList.class);
                startActivity(intent);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainPage.this,NoticeBoard.class);
                startActivity(intent);

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser!=null){

                    showposititivedialog(4);
//                    firebaseFirestore.collection(getString(R.string.col_setting)).document(buildid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if(task.isSuccessful()){
//                                Log.d(TAG, "onComplete:  ggg2");
//                                DocumentSnapshot documentSnapshot= task.getResult();
//                                if(documentSnapshot!=null && documentSnapshot.exists()){
//                                    Settings settings= documentSnapshot.toObject(Settings.class);
//                                    boolean status= settings.isH_status();
//                                    Log.d(TAG, "onComplete: nasif "+ status);
//                                    if(status){
//
//
//                                        int isguardedit= settings.getIsgedit();
//                                        Log.d(TAG, "onComplete: nasif "+ isguardedit);
//                                        if(isguardedit==1){
//                                            shownegativedialog();
//                                        }
//                                        else{
//                                            if(isguardedit>1){
//                                                showposititivedialog(isguardedit);
//                                            }
//
//                                            else{
//                                                shownegativedialog();
//                                            }
//
//                                        }
//                                    }
//                                    else{
//                                        FirebaseAuth.getInstance().signOut();
//                                        Intent intent = new Intent(context, DaroanPass.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                }
//                            }
//                            else Log.d(TAG, "onComplete: ggg1 ");
//                        }
//                    });


                }
                else{
                    FirebaseAuth.getInstance().signOut();
                    Intent intent= new Intent(MainPage.this,DaroanPass.class);
                    startActivity(intent);
                }



            }
        });
        parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainPage.this,ParcelActivity.class);
                startActivity(intent);

            }
        });
        invitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainPage.this,InvitedsList.class);
                startActivity(intent);

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainPage.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    public void shownegativedialog(){
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View convertView = (View) inflater.inflate(R.layout.done, null);

        final Button ok = convertView.findViewById(R.id.okay);
        final ImageView tik = convertView.findViewById(R.id.tik);
        final TextView text = convertView.findViewById(R.id.text);

        tik.setImageDrawable(getResources().getDrawable(R.drawable.cross,null));
        text.setText("You are not allowed to use this feature.");


        alertDialog.setView(convertView);
        alertDialog.setCancelable(true);
        alertDialog.show();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });
    }

    public void showposititivedialog(int x){
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View convertView = (View) inflater.inflate(R.layout.dialog_select, null);

        final ConstraintLayout emp= convertView.findViewById(R.id.one);
        final ConstraintLayout grd= convertView.findViewById(R.id.two);

        if(x==2){
            emp.setVisibility(View.GONE);
        }
        if(x==3){
            grd.setVisibility(View.GONE);
        }

        alertDialog.setView(convertView);
        alertDialog.setCancelable(true);
        alertDialog.show();


        emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent= new Intent(MainPage.this,CreateProfile.class);
                startActivity(intent);
            }
        });
        grd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent= new Intent(MainPage.this,CreateProfileforGuards.class);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: "+ "xxx");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: "+"xxx");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+"xxx");
    }
}
