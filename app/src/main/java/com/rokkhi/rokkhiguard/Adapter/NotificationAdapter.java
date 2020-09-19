package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rokkhi.rokkhiguard.Model.Notifications;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.NoticeDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;



public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotoficationViewHolder> {
    AlertDialog alertDialog;



    public List<Notifications> list;
    private static final String TAG = "NotificationAdapter";
    SharedPreferences sharedPref;


    private Context context;
    NotificationAdapter(List<Notifications> list, Context context) {
        this.list = list;
        this.context=context;

    }


    @NonNull
    @Override
    public NotoficationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        NotoficationViewHolder notoficationViewHolder=new NotoficationViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return notoficationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotoficationViewHolder holder, int position) {

        final Notifications notifications = list.get(position);
        holder.title.setText(notifications.getN_title());

        Date date1=notifications.getN_time();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);

        String myFormat = "EEE, MMM d , hh:mm a"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        holder.date.setText(sdf.format(cal.getTime()));

        holder.body.setText(notifications.getN_body());


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
        return  position;
    }

    public class NotoficationViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView title,body,date ;
        CircleImageView propic;

        NotoficationViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = view.findViewById(R.id.title);
            propic=view.findViewById(R.id.one);
            date= view.findViewById(R.id.date);
            body= view.findViewById(R.id.starttime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context.getApplicationContext(), NoticeDetailsActivity.class)
                    .putExtra("noticeDetails",list.get(getAdapterPosition())));

                }
            });
        }
    }




}
