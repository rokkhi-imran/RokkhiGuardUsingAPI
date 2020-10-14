package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Model.UDetails;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class VisitorWaitingAdapter extends RecyclerView.Adapter<VisitorWaitingAdapter.VisitorViewHolderClass> {

    public ArrayList<Visitors> visitorsArrayList;
    Context context;

    ArrayList<UDetails> flatusers;



    @NonNull
    @Override
    public VisitorViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_visitor_waiting, parent, false);

        return new VisitorViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolderClass holder, int position) {

        Picasso.get().load(visitorsArrayList.get(position).getV_pic()).placeholder( R.drawable.progress_animation ).error(R.drawable.male1).into(holder.imageView);


        holder.nameVisitorWaitingID.setText(visitorsArrayList.get(position).getV_name());
        holder.visitorStatus.setText(visitorsArrayList.get(position).getResponse());

        if (visitorsArrayList.get(position).getResponse().equalsIgnoreCase("rejected")) {
            holder.visitorStatus.setTextColor(ContextCompat.getColor(context,R.color.darkRed));
        }
        else if (visitorsArrayList.get(position).getResponse().equalsIgnoreCase("accepted")) {
            holder.visitorStatus.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        else if (visitorsArrayList.get(position).getResponse().equalsIgnoreCase("intercom")) {
            holder.visitorStatus.setTextColor(ContextCompat.getColor(context,R.color.yellow));
        }
        else if (visitorsArrayList.get(position).getResponse().equalsIgnoreCase("mobile")) {
            holder.visitorStatus.setTextColor(ContextCompat.getColor(context,R.color.guardlistColor));
        }
    }

    @Override
    public int getItemCount() {
        return visitorsArrayList.size();
    }

    public class VisitorViewHolderClass extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView visitorStatus;
        TextView nameVisitorWaitingID;


        public VisitorViewHolderClass(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.visitorImageID);
            visitorStatus = itemView.findViewById(R.id.visitorStatusID);
            nameVisitorWaitingID = itemView.findViewById(R.id.nameVisitorWaitingID);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    getActiveBuildings(visitorsArrayList.get(getAdapterPosition()));

                }
            });

        }
    }
    private void getActiveBuildings(final Visitors visitors) {

        flatusers= new ArrayList<>();


    }


}
