package com.rokkhi.rokkhiguard;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rokkhi.rokkhiguard.Model.Notifications;
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
    Timestamp timestamp=new Timestamp(001,001);
    SharedPreferences sharedPref;


    private Context context;
    private FirebaseFirestore firebaseFirestore;
    NotificationAdapter(List<Notifications> list, Context context) {
        this.list = list;
        this.context=context;

    }


    @NonNull
    @Override
    public NotoficationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        firebaseFirestore= FirebaseFirestore.getInstance();
        NotoficationViewHolder notoficationViewHolder=new NotoficationViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return notoficationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotoficationViewHolder holder, int position) {

        final Notifications notifications = list.get(position);
        holder.title.setText(notifications.getN_title());
        //that means it might have a profile picture
        if(!notifications.getWho_add().equals(notifications.getComm_id())){
            firebaseFirestore.
                    collection(context.getString(R.string.col_users)).document(notifications.getWho_add())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot= task.getResult();
                        if(documentSnapshot!=null && documentSnapshot.exists()){
                            String propic= documentSnapshot.getString("e_thumb");
                            Glide.with(context).load(propic).placeholder(R.drawable.male1).into(holder.propic);

//                            UniversalImageLoader.setImage(propic, holder.propic, null, "");
                        }
                    }
                }
            });
        }
        else{


//            UniversalImageLoader.setImage("", holder.propic, null, "");
        }

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
