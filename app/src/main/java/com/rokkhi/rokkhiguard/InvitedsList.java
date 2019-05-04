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
import com.rokkhi.rokkhiguard.Model.Invitees;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class InvitedsList extends AppCompatActivity implements InviteeAdapter.MyInterface {

    FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot lastVisible=null;
    private boolean isLastItemReached = false;
    private static final String TAG = "InvitedsList";
    ArrayList<Invitees> list;
    RecyclerView recyclerView;
    InviteeAdapter inviteeAdapter;
    FirebaseUser user;
    View mrootView;
    Toolbar toolbar;
    ProgressBar progressBar;
    Context context;
    SharedPreferences sharedPref;
    CollectionReference inviteeref;
    EditText search;


    private int limit = 10;
    NestedScrollView myNestedScroll;
    boolean shouldscrol=true;

    Query getFirstQuery;
    Date low,high;
    String  buildid = "", commid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseFirestore= FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        context= InvitedsList.this;
        myNestedScroll= (NestedScrollView) findViewById(R.id.nested);
        progressBar= findViewById(R.id.progressBar2);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrootView=findViewById(R.id.root);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        search = findViewById(R.id.search);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        inviteeref=firebaseFirestore.collection(getString(R.string.col_invitees));
        getdate();

        getFirstQuery=inviteeref.whereEqualTo("build_id",buildid).
                whereEqualTo("hasdone",false).whereGreaterThan("i_mtime",low)
                .whereLessThan("i_mtime",high).
                orderBy("i_mtime", Query.Direction.ASCENDING);
        getfirstdata();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inviteeAdapter.getFilter().filter(s);
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
                        Invitees invitee = document.toObject(Invitees.class);
                        list.add(invitee);
                    }
                    progressBar.setVisibility(View.GONE);
                    inviteeAdapter = new InviteeAdapter(list,context);
                    inviteeAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(inviteeAdapter);

                }
                else {
                    Log.d(TAG, "onComplete: kotoboro1");
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
