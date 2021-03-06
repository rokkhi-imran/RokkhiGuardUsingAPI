package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Activity.NoticeDetailsActivity;
import com.rokkhi.rokkhiguard.Model.api.NoticeModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;



public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NotoficationViewHolder> {

    public NoticeModelClass noticeModelClass;
    private static final String TAG = "NotificationAdapter";
    SharedPrefHelper sharedPref;

    private Context context;
    public NoticeAdapter(NoticeModelClass list, Context context) {
        this.noticeModelClass = list;
        this.context=context;
    }

    @NonNull
    @Override
    public NotoficationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        NotoficationViewHolder notoficationViewHolder=new NotoficationViewHolder(view);
        sharedPref = new SharedPrefHelper(context);
        return notoficationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotoficationViewHolder holder, int position) {
        try {
            try {

                DateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);

                String inputText = noticeModelClass.getData().get(position).getDate();
                Date date = inputFormat.parse(inputText);
                String outputText = outputFormat.format(date);

                Log.e(TAG, "onBindViewHolder: localDate = "+outputText );
                Log.e(TAG, "onBindViewHolder: localDate in long = "+outputText);
                holder.title.setText(noticeModelClass.getData().get(position).getTitle());
                holder.body.setText(noticeModelClass.getData().get(position).getBody());
                holder.date.setText(outputText);

            }catch (Exception e ){
                Log.e(TAG, "onBindViewHolder: localDate = "+e.getMessage() );

            }
            Log.e(TAG, "onBindViewHolder: notice" );


            Picasso.get()
                    .load("").fit().placeholder( R.drawable.progress_animation ).error(R.drawable.noticeboard).into(holder.propic);


        }catch (Exception e ){

        }


    }

    @Override
    public int getItemCount() {
        return noticeModelClass.getData().size();
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
                    .putExtra("noticeTitle",noticeModelClass.getData().get(getAdapterPosition()).getTitle())
                    .putExtra("noticeDetails",noticeModelClass.getData().get(getAdapterPosition()).getBody())
                    .putExtra("noticeDate",noticeModelClass.getData().get(getAdapterPosition()).getDate())
                    );

                }
            });
        }
    }




}
