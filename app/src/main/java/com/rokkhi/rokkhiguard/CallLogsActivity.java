package com.rokkhi.rokkhiguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.CallLogClass;
import com.rokkhi.rokkhiguard.Model.Visitors;
import java.util.ArrayList;
import java.util.List;


public class CallLogsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerViewID;
    CallLogsAdapter callLogsAdapter;
    private static final String TAG = "CallLogsActivity";
    String thismobile;
    List<CallLogClass>callLogClassList;
    ProgressBar progressBar;

    private int limit = 10;
    NestedScrollView myNestedScroll;
    boolean shouldscrol=true;
    private DocumentSnapshot lastVisible=null;
    private boolean isLastItemReached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs);

        callLogClassList=new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerViewID = findViewById(R.id.callLogsRecyclerViewID);
        thismobile = FirebaseAuth.getInstance().getUid();
        progressBar= findViewById(R.id.progressBar2);
        myNestedScroll= (NestedScrollView) findViewById(R.id.nested);
        recyclerViewID.setNestedScrollingEnabled(false);
        recyclerViewID.setLayoutManager(new LinearLayoutManager(this));

        loadDataToRecyclerView(thismobile);
    }

    private void loadDataToRecyclerView(String thismobile) {



        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection(getString(R.string.col_callLog))
                .whereEqualTo("mobileUID",thismobile)
                .orderBy("callStart", Query.Direction.DESCENDING)
                .limit(limit)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    callLogClassList.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                        CallLogClass callLogClass= documentSnapshot.toObject(CallLogClass.class);
                        callLogClassList.add(callLogClass);
                        Log.e("TAG", " rrrr: "+callLogClass.getDocID());

                    }
                    progressBar.setVisibility(View.GONE);
                    callLogsAdapter = new CallLogsAdapter(callLogClassList);
                    callLogsAdapter.setHasStableIds(true);
                    recyclerViewID.setAdapter(callLogsAdapter);
                    int xx=task.getResult().size();
                    if(xx>0)lastVisible = task.getResult().getDocuments().get(xx - 1);
                    loadmoredata();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CallLogsActivity.this, "DataBase Error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void loadmoredata(){

        myNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY && shouldscrol) {

                        shouldscrol=false;

                        progressBar.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerViewID.getLayoutManager());
                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = linearLayoutManager.getChildCount();
                        int totalItemCount = linearLayoutManager.getItemCount();



                        Log.d(TAG, "onScrollChange: item dekhi "+ firstVisibleItemPosition +" "+ visibleItemCount+" "+totalItemCount);


                        if ((firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {

                            Log.d(TAG, "onScrolled: mmmmll dhukse");
                            Query nextQuery;
                            nextQuery= firebaseFirestore.collection(getString(R.string.col_callLog))
                                    .whereEqualTo("mobileUID",thismobile)
                                    .orderBy("callStart", Query.Direction.DESCENDING)
                                    .startAfter(lastVisible).limit(limit);

                            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                    if (t.isSuccessful()) {
                                        // list.clear();

                                        for (DocumentSnapshot d : t.getResult()) {
                                            CallLogClass productModel = d.toObject(CallLogClass.class);
                                            callLogClassList.add(productModel);
                                        }
                                        shouldscrol=true;
                                        progressBar.setVisibility(View.GONE);
                                        callLogsAdapter.notifyDataSetChanged();
                                        int xx=t.getResult().size();
                                        if(xx>0)lastVisible = t.getResult().getDocuments().get(xx - 1);

                                        if (t.getResult().size() < limit) {
                                            isLastItemReached = true;
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            });
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });



    }
}
