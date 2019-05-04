package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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
import com.rokkhi.rokkhiguard.Model.Visitors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ChildrenList extends AppCompatActivity implements VisitorAdapter.MyInterface {
    FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot lastVisible=null;
    private boolean isLastItemReached = false;
    private static final String TAG = "VisitorsList";
    ArrayList<Visitors> list;
    RecyclerView recyclerView;
    VisitorAdapter visitorAdapter;
    FirebaseUser user;
    View mrootView;
    Toolbar toolbar;
    ProgressBar progressBar;
    Context context;
    SharedPreferences sharedPref;
    CollectionReference visitorref;
    EditText search;
    SharedPreferences.Editor editor;


    private int limit = 10;
    NestedScrollView myNestedScroll;
    boolean shouldscrol=true;


    Query getFirstQuery;
    Date d;
    Date low,high;
    String flatid = "", buildid = "", commid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseFirestore= FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        context= ChildrenList.this;
        myNestedScroll= (NestedScrollView) findViewById(R.id.nested);
        progressBar= findViewById(R.id.progressBar2);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrootView=findViewById(R.id.root);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor= sharedPref.edit();
        search = findViewById(R.id.search);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        getdate();


        visitorref=firebaseFirestore.
                collection(getString(R.string.col_visitors));


        getFirstQuery=visitorref.whereEqualTo("build_id",buildid).whereEqualTo("isin",true)
                .whereGreaterThan("v_checkin",low)
                .whereLessThan("v_checkin",high).
                orderBy("v_checkin", Query.Direction.ASCENDING).limit(limit);
        getfirstdata();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                visitorAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



    }

    public void getdate(){
        Calendar cal = Calendar.getInstance(); //current date and time
        cal.add(Calendar.DAY_OF_MONTH, 0); //add a day
        cal.set(Calendar.HOUR_OF_DAY, 23); //set hour to last hour
        cal.set(Calendar.MINUTE, 59); //set minutes to last minute
        cal.set(Calendar.SECOND, 59); //set seconds to last second
        cal.set(Calendar.MILLISECOND, 999); //set milliseconds to last millisecond

        high=cal.getTime();
        Calendar cal1 = Calendar.getInstance(); //current date and time
        cal1.add(Calendar.DAY_OF_MONTH, 0); //add a day
        cal1.set(Calendar.HOUR_OF_DAY, 0); //set hour to last hour
        cal1.set(Calendar.MINUTE, 0); //set minutes to last minute
        cal1.set(Calendar.SECOND, 0); //set seconds to last second
        cal1.set(Calendar.MILLISECOND, 0); //set milliseconds to last millisecond
        low= cal1.getTime();

        Log.d(TAG, "getdate: bb "+ high + " "+low);

    }

    public void getfirstdata(){
        getFirstQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: kotoboro "+task.getResult().size());
                    list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Visitors visitors = document.toObject(Visitors.class);
                        list.add(visitors);
                    }
                    progressBar.setVisibility(View.GONE);
                    visitorAdapter = new VisitorAdapter(list,context);
                    visitorAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(visitorAdapter);
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
                            nextQuery= visitorref.whereEqualTo("build_id",buildid).whereEqualTo("isin",true)
                                    .whereGreaterThan("v_checkin",low)
                                    .whereLessThan("v_checkin",high).
                                            orderBy("v_checkin", Query.Direction.ASCENDING)
                                    .startAfter(lastVisible).limit(limit);

                            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                    if (t.isSuccessful()) {
                                        // list.clear();

                                        for (DocumentSnapshot d : t.getResult()) {
                                            Visitors productModel = d.toObject(Visitors.class);
                                            list.add(productModel);
                                        }
                                        shouldscrol=true;
                                        progressBar.setVisibility(View.GONE);
                                        visitorAdapter.notifyDataSetChanged();
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

    @Override
    public void loadagain() {
        list.clear();
        progressBar.setVisibility(View.VISIBLE);
        search.setText("");
        getfirstdata();

    }
}
