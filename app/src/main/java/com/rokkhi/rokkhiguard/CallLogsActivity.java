package com.rokkhi.rokkhiguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.CallLogClass;

import java.util.ArrayList;
import java.util.List;

public class CallLogsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerViewID;
    CallLogsAdapter callLogsAdapter;
    String thismobile;
    List<CallLogClass>callLogClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs);

        callLogClassList=new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerViewID = findViewById(R.id.callLogsRecyclerViewID);
        thismobile = FirebaseAuth.getInstance().getUid();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewID.setLayoutManager(mLayoutManager);

        loadDataToRecyclerView(thismobile);


    }

    private void loadDataToRecyclerView(String thismobile) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Execution Action...");
        progressDialog.setCancelable(false);
        progressDialog.show();



        firebaseFirestore.collection(getString(R.string.col_callLog))
                .whereEqualTo("mobileUID",thismobile)
                .orderBy("callStart", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    callLogClassList.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        CallLogClass callLogClass= documentSnapshot.toObject(CallLogClass.class);
                        callLogClassList.add(callLogClass);
                        Log.e("TAG", "onComplete: "+callLogClass.getDocID());
                        progressDialog.dismiss();
                    }

                    callLogsAdapter = new CallLogsAdapter(callLogClassList);
                    recyclerViewID.setAdapter(callLogsAdapter);

                }else {
                    progressDialog.dismiss();
                }

            }
        });

    }
}
