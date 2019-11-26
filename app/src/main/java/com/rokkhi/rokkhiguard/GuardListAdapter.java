package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GuardListAdapter extends RecyclerView.Adapter<GuardListAdapter.ListViewHolder> {
    ArrayList<Guards> guardsArrayList;
    Context context;


    public GuardListAdapter(ArrayList<Guards> guardsArrayList, Context context) {
        this.guardsArrayList = guardsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guard_list, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {


        holder.nameTV.setText(guardsArrayList.get(position).getG_name());
        UniversalImageLoader.setImage(guardsArrayList.get(position).getG_pic(), holder.circleImageView, null, "");


    }

    @Override
    public int getItemCount() {
        return guardsArrayList.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView nameTV;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.propic_guard_image);
            nameTV = itemView.findViewById(R.id.name_Guard_TV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    context.startActivity(new Intent(context, GuardQRCodeActivity.class)
                    .putExtra("GuardInfo", guardsArrayList.get(getAdapterPosition())));

                }
            });


        }
    }
}
