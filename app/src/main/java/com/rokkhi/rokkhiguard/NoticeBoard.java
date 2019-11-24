package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rokkhi.rokkhiguard.Model.Notifications;

import java.util.ArrayList;
import java.util.List;



public class NoticeBoard extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot lastVisible=null;
    private boolean isLastItemReached = false;
    private static final String TAG = "NoticeBoard";
    List<Notifications> list;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    FirebaseUser user;
    View mrootView;
    Toolbar toolbar;
    ProgressBar progressBar;
    Context context;
    SharedPreferences sharedPref;
    CollectionReference notificationCollection;

    private int limit = 10;
    NestedScrollView myNestedScroll;
    boolean shouldscrol=true;


    Query getFirstQuery,getsecondquery;
    TextView title;
    String flatid = "", buildid = "", commid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseFirestore= FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        context=NoticeBoard.this;
        myNestedScroll= (NestedScrollView) findViewById(R.id.nested);
        progressBar= findViewById(R.id.progressBar2);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrootView=findViewById(R.id.root);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
       // title= findViewById(R.id.title);

        notificationCollection=firebaseFirestore.
                collection(getString(R.string.col_notification));


        getFirstQuery=notificationCollection.whereEqualTo("build_id",buildid).
                whereEqualTo("n_type","guard").orderBy("n_time", Query.Direction.DESCENDING).limit(limit);
        getsecondquery=notificationCollection.whereEqualTo("build_id",buildid).
                whereEqualTo("n_type","all").
                orderBy("n_time", Query.Direction.DESCENDING).limit(limit);

        list = new ArrayList<>();
        getfirstdata();


    }


    public void getfirstdata(){
        getFirstQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onComplete: kotoboro "+task.getResult().size());

                    for (DocumentSnapshot document : task.getResult()) {
                        Notifications notifications = document.toObject(Notifications.class);
                        list.add(notifications);
                    }
                    progressBar.setVisibility(View.GONE);
                    notificationAdapter = new NotificationAdapter(list,context);
                    notificationAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(notificationAdapter);
                    int xx=task.getResult().size();
                    if(xx>0)lastVisible = task.getResult().getDocuments().get(xx - 1);
                    loadmoredata();


                }
                else {

                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onComplete: kotoboro1");
                }
            }
        });

        getsecondquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: kotoboro "+task.getResult().size());
                    progressBar.setVisibility(View.VISIBLE);
                    for (DocumentSnapshot document : task.getResult()) {
                        Notifications notifications = document.toObject(Notifications.class);
                        list.add(notifications);
                    }
                    progressBar.setVisibility(View.GONE);
                    notificationAdapter = new NotificationAdapter(list,context);
                    notificationAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(notificationAdapter);
                    int xx=task.getResult().size();
                    if(xx>0)lastVisible = task.getResult().getDocuments().get(xx - 1);
                    //lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                    loadmoredata();


                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onComplete: kotoboro1");
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
                        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = linearLayoutManager.getChildCount();
                        int totalItemCount = linearLayoutManager.getItemCount();



                        Log.d(TAG, "onScrollChange: item dekhi "+ firstVisibleItemPosition +" "+ visibleItemCount+" "+totalItemCount);


                        if ((firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {

                            Log.d(TAG, "onScrolled: mmmmll dhukse");
                            Query nextQuery;
                            nextQuery= notificationCollection.whereEqualTo("build_id",buildid).
                                    whereEqualTo("n_type","guard").orderBy("n_time", Query.Direction.DESCENDING).startAfter(lastVisible).limit(limit);
                            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                    if (t.isSuccessful()) {
                                        // list.clear();

                                        for (DocumentSnapshot d : t.getResult()) {
                                            Notifications productModel = d.toObject(Notifications.class);
                                            list.add(productModel);
                                        }
                                        shouldscrol=true;
                                        progressBar.setVisibility(View.GONE);
                                        notificationAdapter.notifyDataSetChanged();
                                        int xx=t.getResult().size();
                                        if(xx>0)lastVisible = t.getResult().getDocuments().get(xx - 1);

                                        if (t.getResult().size() < limit) {
                                            isLastItemReached = true;
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            });


                            Query nextQuery1;
                            nextQuery1= notificationCollection.whereEqualTo("build_id",buildid).
                                    whereEqualTo("n_type" ,"all").
                                    orderBy("n_time", Query.Direction.DESCENDING).startAfter(lastVisible).limit(limit);
                            nextQuery1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                    if (t.isSuccessful()) {

                                        for (DocumentSnapshot d : t.getResult()) {
                                            Notifications productModel = d.toObject(Notifications.class);
                                            list.add(productModel);
                                        }

                                        shouldscrol=true;
                                        progressBar.setVisibility(View.GONE);
                                        notificationAdapter.notifyDataSetChanged();
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
