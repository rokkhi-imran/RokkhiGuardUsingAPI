package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Model.api.SWorkerModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class SWorkerAdapter extends RecyclerView.Adapter<SWorkerAdapter.SWorkerViewHolder> {



    private LayoutInflater mInflater;
    Normalfunc normalfunc;

    public SWorkerModelClass list;
    private static final String TAG = "SWorkerAdapter";

    private Context context;
    public SWorkerAdapter(SWorkerModelClass list, Context context) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public SWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sworkers, parent, false);
        SWorkerViewHolder visitorViewHolder = new SWorkerViewHolder(view);
        normalfunc= new Normalfunc();

        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SWorkerViewHolder holder, int position) {

        try {
            holder.name.setText(list.getData().get(position).getName());
            holder.lastcome.setText(list.getData().get(position).getPhone());
            Picasso.get().load(list.getData().get(position).getImage()).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.propic);
        }catch (Exception e){

        }


    }


    @Override
    public int getItemCount() {
        return list.getData().size();
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
