package com.rokkhi.rokkhiguard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.rokkhiguard.Model.SLastHistory;
import com.rokkhi.rokkhiguard.Model.Swroker;

import java.util.ArrayList;
import java.util.List;

public class SWorkersActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageButton settings;
    protected RecyclerView sWorkerRecyclerViewID;
    protected FloatingActionButton createProfileFabButton;
    FirebaseFirestore firebaseFirestore;
    String buildid;
    ProgressDialog progressDialog;
    List<Swroker> swrokerList;
    List<SLastHistory> sLastHistoryList;
    SWorkerListAdapter sWorkerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_sworkers);
        initView();

        //get build id

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        Log.e("TAG", "onCreate: SWorker " + buildid);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        sWorkerRecyclerViewID.setLayoutManager(linearLayoutManager);

        sWorkerListAdapter=new SWorkerListAdapter(swrokerList,sLastHistoryList,this);

        getAllServiceWorkerID();


    }

    //get service Worker ID list
    private void getAllServiceWorkerID() {
        progressDialog.show();

        firebaseFirestore.collection("servicebuildings")
                .document(buildid).collection("sworkers")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        String sID = snapshot.getString("s_id");

                        getSworkerInformation(sID);
                        Log.e("TAG", "onComplete: S ID =  " + snapshot.getString("s_id"));


                    }
                    progressDialog.dismiss();



                } else {
                    Toast.makeText(SWorkersActivity.this, "Data Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    //get worker Information
    private void getSworkerInformation(final String sID) {


//get SWorker Profile
        firebaseFirestore.collection(getString(R.string.col_sworker))
                .whereEqualTo("s_id", sID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    swrokerList.clear();
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        Swroker swroker = snapshot.toObject(Swroker.class);
                        swrokerList.add(swroker);
                    }
                    sWorkerListAdapter.notifyDataSetChanged();



                } else {
                    Toast.makeText(SWorkersActivity.this, "Failed to Load worker Information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //get SWorkerFlat

        firebaseFirestore.collection(getString(R.string.col_sworker))
                .document(sID).collection("shistory")
                .whereEqualTo("build_id", buildid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    sLastHistoryList.clear();
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        SLastHistory sLastHistory = snapshot.toObject(SLastHistory.class);
                        sLastHistoryList.add(sLastHistory);
                        Log.e("TAG", "onComplete: "+sLastHistory.getFlatsNo());
                    }

                }else {
                    Toast.makeText(SWorkersActivity.this, "Failed to get flat Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.e("TAG", "getSworkerInformation: "+sLastHistoryList.size());
        Log.e("TAG", "getSworkerInformation: "+swrokerList.size());


    }


    private void initView() {
        sWorkerRecyclerViewID = (RecyclerView) findViewById(R.id.sWorkerRecyclerViewID);
        createProfileFabButton = (FloatingActionButton) findViewById(R.id.createProfileFabButton);
        createProfileFabButton.setOnClickListener(SWorkersActivity.this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        swrokerList = new ArrayList<>();
        sLastHistoryList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Executing Action....");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.createProfileFabButton) {
            startActivity(new Intent(this, CreateProfile.class));
        }
    }
}
