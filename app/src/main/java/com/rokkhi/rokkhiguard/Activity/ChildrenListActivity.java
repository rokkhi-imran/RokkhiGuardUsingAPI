package com.rokkhi.rokkhiguard.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.rokkhi.rokkhiguard.Adapter.ChildAdapter;
import com.rokkhi.rokkhiguard.R;


public class ChildrenListActivity extends AppCompatActivity {
    private static final String TAG = "ChildrenList";


    RecyclerView recyclerView;
    ChildAdapter childAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        childAdapter = new ChildAdapter(list, context);
//        childAdapter.setHasStableIds(true);
//        recyclerView.setAdapter(childAdapter);

    }

}
