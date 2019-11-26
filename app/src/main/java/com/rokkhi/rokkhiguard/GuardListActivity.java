package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.rokkhiguard.Model.Guards;

import java.util.ArrayList;

public class GuardListActivity extends AppCompatActivity {
    protected ProgressBar progressbar;
    protected RecyclerView guardListRecyclerView;
    String buildid = "", commid = "";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Context context;
    ArrayList<Guards> guardsArrayList;


    FirebaseFirestore firebaseFirestore;
    String TAG = "GuardListActivity  ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_guard_list);
        context = this;
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        guardListRecyclerView = (RecyclerView) findViewById(R.id.guardListRecyclerView);
        guardsArrayList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPref.edit();
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        Log.e(TAG, "onCreate: build id =  " + buildid);
        Log.e(TAG, "onCreate: comm id =  " + commid);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        guardListRecyclerView.setLayoutManager(linearLayoutManager);


        firebaseFirestore.collection(getString(R.string.col_guards))
                .whereEqualTo("build_id", buildid).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                Guards guards = snapshot.toObject(Guards.class);
                                guardsArrayList.add(guards);
                                Log.e(TAG, "onComplete:  size = " + guardsArrayList.size());
                            }

                            GuardListAdapter guardListAdapter = new GuardListAdapter(guardsArrayList, context);
                            guardListRecyclerView.setAdapter(guardListAdapter);
                            progressbar.setVisibility(View.GONE);


                        } else {
                            Log.e(TAG, "onComplete: Task Failed");
                            Toast.makeText(context, "task Failed", Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressbar.setVisibility(View.GONE);
                Log.e(TAG, "Failed to Load data");

                Toast.makeText(context, "failed to load Data", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
