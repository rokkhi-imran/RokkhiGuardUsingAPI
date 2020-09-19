package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Model.ServiceBuilding;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SWorkerAdapter extends RecyclerView.Adapter<SWorkerAdapter.SWorkerViewHolder> {



    private ArrayList<ServiceBuilding> mvisitorFilterList;
    private LayoutInflater mInflater;
    Normalfunc normalfunc;

    public ArrayList<ServiceBuilding> list;
    private static final String TAG = "SWorkerAdapter";
    SharedPreferences sharedPref;

    private Context context;
    SWorkerAdapter(ArrayList<ServiceBuilding> list, Context context) {
        this.list = list;
        mvisitorFilterList = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public SWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sworkers, parent, false);
        SWorkerViewHolder visitorViewHolder = new SWorkerViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        normalfunc= new Normalfunc();
        //oCanNotification= sharedPref.getBoolean("oCanNotification",true);


        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SWorkerViewHolder holder, int position) {


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class SWorkerViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView name,lastcome;
        CircleImageView propic;

        SWorkerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.one);
            lastcome= view.findViewById(R.id.lastcome);
        }
    }




}
