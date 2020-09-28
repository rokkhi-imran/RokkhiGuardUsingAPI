package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Activity.DaroanPassActivity;
import com.rokkhi.rokkhiguard.Model.api.GuardListData;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class GuardListAdapter extends RecyclerView.Adapter<GuardListAdapter.SWorkerViewHolder> {



    private LayoutInflater mInflater;
    Normalfunc normalfunc;

    private static final String TAG = "SWorkerAdapter";
    public ArrayList<GuardListData> guardListDataArrayList;

    private Context context;
    public GuardListAdapter(ArrayList<GuardListData> guardListDataArrayList, Context context) {
        this.guardListDataArrayList = guardListDataArrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public SWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guard_list, parent, false);
        SWorkerViewHolder visitorViewHolder = new SWorkerViewHolder(view);
        normalfunc= new Normalfunc();

        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SWorkerViewHolder holder, int position) {

        try {
            holder.name.setText(guardListDataArrayList.get(position).getName());
            Picasso.get().load(guardListDataArrayList.get(position).getImage()).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.propic);
        }catch (Exception e){

        }


    }


    @Override
    public int getItemCount() {
        return guardListDataArrayList.size();
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
        TextView name;
        CircleImageView propic;

        SWorkerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name_Guard_TV);
            propic = view.findViewById(R.id.propic_guard_image);

            view.setOnClickListener(v -> {
                Gson gson = new Gson();
                String myJson = gson.toJson(guardListDataArrayList.get(getAdapterPosition()));
                context.startActivity(new Intent(context, DaroanPassActivity.class)
                .putExtra("guardinfo",myJson ));
            });

        }
    }




}
