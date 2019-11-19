package com.rokkhi.rokkhiguard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.rokkhi.rokkhiguard.Model.Child;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ChildrenList extends AppCompatActivity implements ChildAdapter.MyInterface {
    private static final String TAG = "ChildrenList";
    FirebaseFirestore firebaseFirestore;
    ArrayList<Child> list;
    RecyclerView recyclerView;
    ChildAdapter childAdapter;
    FirebaseUser user;
    View mrootView;
    Toolbar toolbar;
    ProgressBar progressBar;
    Context context;
    SharedPreferences sharedPref;
    CollectionReference childref;
    EditText search;
    SharedPreferences.Editor editor;
    NestedScrollView myNestedScroll;
    boolean shouldscrol = true;
    Query getFirstQuery;
    Date d;
    Date low, high;
    String flatid = "", buildid = "", commid = "";
    String phoneno = "";
    private DocumentSnapshot lastVisible = null;
    private boolean isLastItemReached = false;
    private int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        context = ChildrenList.this;
        myNestedScroll = (NestedScrollView) findViewById(R.id.nested);
        progressBar = findViewById(R.id.progressBar2);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrootView = findViewById(R.id.root);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
        search = findViewById(R.id.search);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        getdate();


        childref = firebaseFirestore.
                collection(getString(R.string.col_child));


        list = new ArrayList<>();
        getFirstQuery = childref.whereEqualTo("build_id", buildid).whereEqualTo("activated", true)
                .limit(limit);
        getfirstdata();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (list.size() > 0) {
                    childAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    public void getdate() {
        Calendar cal = Calendar.getInstance(); //current date and time

        low = cal.getTime();

        Log.d(TAG, "getdate: bb " + high + " " + low);

    }

    public void getfirstdata() {
        progressBar.setVisibility(View.VISIBLE);


        getFirstQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: kotoboro " + task.getResult().size());

                    for (DocumentSnapshot document : task.getResult()) {
                        Child buildingchild = document.toObject(Child.class);
                        list.add(buildingchild);
                    }

                    progressBar.setVisibility(View.GONE);
                    childAdapter = new ChildAdapter(list, context);
                    childAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(childAdapter);
                    int xx = task.getResult().size();
                    if (xx > 0) lastVisible = task.getResult().getDocuments().get(xx - 1);
                    loadmoredata();


                } else {
                    Log.d(TAG, "onComplete: kotoboro1");
                }
            }
        });
    }

    public void loadmoredata() {


        myNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY && shouldscrol) {

                        shouldscrol = false;

                        progressBar.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = linearLayoutManager.getChildCount();
                        int totalItemCount = linearLayoutManager.getItemCount();


                        Log.d(TAG, "onScrollChange: item dekhi " + firstVisibleItemPosition + " " + visibleItemCount + " " + totalItemCount);


                        if ((firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {

                            Log.d(TAG, "onScrolled: mmmmll dhukse");
                            Query nextQuery;
                            nextQuery = childref.whereEqualTo("build_id", buildid).whereEqualTo("activated", true)
                                    .startAfter(lastVisible).limit(limit);

                            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                    if (t.isSuccessful()) {
                                        // list.clear();

                                        for (DocumentSnapshot d : t.getResult()) {
                                            Child child = d.toObject(Child.class);
                                            list.add(child);
                                        }
                                        shouldscrol = true;
                                        progressBar.setVisibility(View.GONE);
                                        childAdapter.notifyDataSetChanged();
                                        int xx = t.getResult().size();
                                        if (xx > 0)
                                            lastVisible = t.getResult().getDocuments().get(xx - 1);

                                        if (t.getResult().size() < limit) {
                                            isLastItemReached = true;
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });


    }

    private void onCallBtnClick() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            phoneCall();
        } else {
            final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
            //Asking request Permissions
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch (requestCode) {
            case 9:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (permissionGranted) {
            phoneCall();
        } else {
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if (phoneno.isEmpty()) {
                callIntent.setData(Uri.parse("tel:01521110045"));
            } else {
                callIntent.setData(Uri.parse("tel:" + phoneno));
            }
            startActivity(callIntent);
        } else {
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void callparents(String number) {

        phoneno = number;

        onCallBtnClick();
    }


}
