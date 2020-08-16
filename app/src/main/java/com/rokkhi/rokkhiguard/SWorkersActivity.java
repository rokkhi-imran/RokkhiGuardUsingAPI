package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.rokkhi.rokkhiguard.Model.ServiceBuilding;

import java.util.ArrayList;
import java.util.Date;

public class SWorkersActivity extends AppCompatActivity implements  GateAdapter.MyInterface {


    FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot lastVisible=null;
    private boolean isLastItemReached = false;
    private static final String TAG = "SWorkersActivity";
    ArrayList<ServiceBuilding> list;
    RecyclerView recyclerView;
    SWorkerAdapter sWorkerAdapter;
    FirebaseUser user;
    View mrootView;
    ProgressBar progressBar;
    Context context;
    SharedPreferences sharedPref;
    CollectionReference sworkerbuildingref;
    EditText search;
    SharedPreferences.Editor editor;
    NestedScrollView myNestedScroll;

    private int limit = 10;
    boolean shouldscrol=true;

    Query getFirstQuery;
    Date d;
    Date low,high;
    String flatid = "", buildid = "", commid = "";
    String seartText="n/a";

    protected Button createProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_sworkers);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseFirestore= FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        context=SWorkersActivity.this;
        myNestedScroll= (NestedScrollView) findViewById(R.id.nested);
        progressBar= findViewById(R.id.progressBar2);
        recyclerView=findViewById(R.id.sWorkerRecyclerViewID);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mrootView=findViewById(R.id.root);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor= sharedPref.edit();
        createProfile= findViewById(R.id.createprofile);
        search = findViewById(R.id.search);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");


        sworkerbuildingref=firebaseFirestore.
                collection(getString(R.string.col_servicebuildings)).document(buildid).collection(getString(R.string.col_sworker));

        Log.d(TAG, "onCreate: yyy "+ low+"  "+ high +" "+ buildid);



        getfirstdata();
        initdialog();
        list = new ArrayList<>();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len= s.length();
                if(len!=0){
                    seartText=s.toString().toLowerCase();

                }
                else{
                    seartText="n/a";
                }

                getfirstdata();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, CreateProfileActivity.class);
                startActivity(intent);
            }
        });



    }


    public void getfirstdata(){
        progressBar.setVisibility(View.VISIBLE);
        isLastItemReached=false;
        getFirstQuery= sworkerbuildingref.whereArrayContains("search",seartText).
                orderBy("lastday", Query.Direction.DESCENDING).limit(limit);
        shouldscrol=true;

        getFirstQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    list=new ArrayList<>();
                    Log.d(TAG, "onComplete: kotoboro "+task.getResult().size());


                    for (DocumentSnapshot document : task.getResult()) {
                        ServiceBuilding serviceBuilding = document.toObject(ServiceBuilding.class);
                        list.add(serviceBuilding);
                    }
                    progressBar.setVisibility(View.GONE);
                    sWorkerAdapter = new SWorkerAdapter(list,context);
                    sWorkerAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(sWorkerAdapter);
                    int xx=task.getResult().size();
                    if(xx>0)lastVisible = task.getResult().getDocuments().get(xx - 1);
                    loadmoredata();


                }
                else {
                    Log.d(TAG, "onComplete: kotoboro1");
                }
            }
        });
    }


//    public void loadmoredata(){
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged( RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//
//                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
//                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//                int visibleItemCount = linearLayoutManager.getChildCount();
//                int totalItemCount = linearLayoutManager.getItemCount();
//
//                Log.d(TAG, "onScrollChange: item dekhi "+ firstVisibleItemPosition +" "+ visibleItemCount+" "+totalItemCount);
//
//
//                if ((firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached
//                        && lastVisible!=null) {
//
//                    Query nextQuery;
//                    nextQuery= sworkerbuildingref.whereArrayContains("search",seartText).
//                            orderBy("lastday", Query.Direction.DESCENDING)
//                            .startAfter(lastVisible).limit(limit);
//
//                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> t) {
//                            if (t.isSuccessful()) {
//                                // list.clear();
//                                Log.d(TAG, "onComplete: kotoboro2 "+list.size() +" "+t.getResult().size());
//
//                                for (DocumentSnapshot d : t.getResult()) {
//                                    ServiceBuilding productModel = d.toObject(ServiceBuilding.class);
//                                    list.add(productModel);
//                                }
//                                progressBar.setVisibility(View.GONE);
//                                sWorkerAdapter.notifyDataSetChanged();
//                                int xx=t.getResult().size();
//                                if(xx>0)lastVisible = t.getResult().getDocuments().get(xx - 1);
//
//                                if (t.getResult().size() < limit) {
//                                    isLastItemReached = true;
//                                    progressBar.setVisibility(View.GONE);
//                                }
//                            }
//                        }
//                    });
//                }
//                else{
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//        });
//
//    }

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
                            nextQuery= sworkerbuildingref.whereArrayContains("search",seartText).
                                    orderBy("lastday", Query.Direction.DESCENDING)
                                    .startAfter(lastVisible).limit(limit);

                            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                    if (t.isSuccessful()) {
                                        // list.clear();
                                        Log.d(TAG, "onComplete: kotoboro2 "+list.size() +" "+t.getResult().size());

                                        for (DocumentSnapshot d : t.getResult()) {
                                            ServiceBuilding productModel = d.toObject(ServiceBuilding.class);
                                            list.add(productModel);
                                        }
                                        progressBar.setVisibility(View.GONE);
                                        sWorkerAdapter.notifyDataSetChanged();
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


    Dialog mdialog;

    public void initdialog(){
        mdialog=new Dialog(this);

        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);

    }



    @Override
    public void dissmissdialog() {
        sWorkerAdapter.dissmissdialog();

    }

    @Override
    public void showprogressbar(){
        mdialog.show(); }


    @Override
    public void hideprogressbar(){
        mdialog.dismiss();
    }
}
