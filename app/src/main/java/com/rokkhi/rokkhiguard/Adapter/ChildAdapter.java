package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Model.api.ChildModelClass;
import com.rokkhi.rokkhiguard.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder>{


    private static final String TAG = "ChildAdapter";
    public ChildModelClass list;
    SharedPreferences sharedPref;
    private ChildModelClass mchildFilterList;
    private LayoutInflater mInflater;
    private Context context;

    public ChildAdapter(ChildModelClass list, Context context) {
        this.list = list;
        mchildFilterList = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);

        ChildViewHolder childViewHolder = new ChildViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return childViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChildViewHolder holder, int position) {

        holder.name.setText(list.getData().get(position).getName());
        holder.flat.setText("Flat:  Data Not Found From api" );

        Picasso.get().load(list.getData().get(position).getImage()).placeholder(R.drawable.male1).error(R.drawable.male1).into(holder.propic);


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




    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView name;
        TextView active, flat;
        CircleImageView propic;
        ImageView call;

        ChildViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.one);
            active = view.findViewById(R.id.activated);
            call = view.findViewById(R.id.call);
            flat = view.findViewById(R.id.flatNumberET);
        }
    }
}
