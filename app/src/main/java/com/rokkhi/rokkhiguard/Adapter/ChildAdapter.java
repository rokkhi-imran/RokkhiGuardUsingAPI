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

import com.rokkhi.rokkhiguard.Model.Child;
import com.rokkhi.rokkhiguard.Model.UDetails;
import com.rokkhi.rokkhiguard.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder>{


    private static final String TAG = "ChildAdapter";
    public ArrayList<Child> list;
    SharedPreferences sharedPref;
    private ArrayList<Child> mchildFilterList;
    private ArrayList<UDetails> flatusers;
    private LayoutInflater mInflater;
    private Context context;

    ChildAdapter(ArrayList<Child> list, Context context) {
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

        final Child child = list.get(position);
        holder.name.setText(child.getM_name());
        holder.flat.setText("Flat:  " + child.getF_no());


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
            flat = view.findViewById(R.id.flat);
        }
    }
}
