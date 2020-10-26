package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Activity.WaitingVisitorActivity;
import com.rokkhi.rokkhiguard.Model.api.GetInsideVisitorData;
import com.rokkhi.rokkhiguard.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class VisitorWaitingListAdapter extends RecyclerView.Adapter<VisitorWaitingListAdapter.VisitorViewHolder> {


    private LayoutInflater mInflater;

    public ArrayList<GetInsideVisitorData> list;
    private static final String TAG = "VisitorAdapter";
    SharedPreferences sharedPref;

    private Context context;

    public VisitorWaitingListAdapter(ArrayList<GetInsideVisitorData> list, Context context) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_visitor_waiting, parent, false);
        VisitorViewHolder visitorViewHolder = new VisitorViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VisitorViewHolder holder, int position) {

        final GetInsideVisitorData visitor = list.get(position);
        holder.nameVisitorWaitingID.setText(visitor.getName());

        if (!visitor.getImage().isEmpty()) {

            Picasso.get().load(visitor.getImage()).placeholder(R.drawable.progress_animation).error(R.drawable.male1).into(holder.visitorImageID);
        }


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

    public class VisitorViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView nameVisitorWaitingID;
        CircleImageView visitorImageID;

        VisitorViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            nameVisitorWaitingID = view.findViewById(R.id.nameVisitorWaitingID);
            visitorImageID = view.findViewById(R.id.visitorImageID);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWaitingAlertDialog(getAdapterPosition(), list, context);
                }
            });

        }
    }

    private void showWaitingAlertDialog(int adapterPosition, ArrayList<GetInsideVisitorData> list, Context context) {

        context.startActivity(new Intent(context, WaitingVisitorActivity.class)
                .putExtra("visitorID", list.get(adapterPosition).getId())
                .putExtra("name", list.get(adapterPosition).getName())
                .putExtra("phone", list.get(adapterPosition).getContact())
                .putExtra("image", list.get(adapterPosition).getImage())
                .putExtra("flat", list.get(adapterPosition).getFlat().getNumber())
                .putExtra("flatID", list.get(adapterPosition).getFlat().getId())
                .putExtra("purpose", list.get(adapterPosition).getPurpose())
                .putExtra("address", list.get(adapterPosition).getAddress())


        );

    }


}
