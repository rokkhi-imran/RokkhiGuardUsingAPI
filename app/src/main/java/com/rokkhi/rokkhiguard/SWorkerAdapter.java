package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rokkhi.rokkhiguard.Model.ServiceBuilding;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class SWorkerAdapter extends RecyclerView.Adapter<SWorkerAdapter.SWorkerViewHolder> {

    AlertDialog alertDialog, alertDialog2;


    private ArrayList<ServiceBuilding> mvisitorFilterList;
    private LayoutInflater mInflater;
    Normalfunc normalfunc;

    public ArrayList<ServiceBuilding> list;
    private static final String TAG = "SWorkerAdapter";
    SharedPreferences sharedPref;

    private Context context;
    private FirebaseFirestore firebaseFirestore;
    SWorkerAdapter(ArrayList<ServiceBuilding> list, Context context) {
        this.list = list;
        mvisitorFilterList = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public SWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sworkers, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        SWorkerViewHolder visitorViewHolder = new SWorkerViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        normalfunc= new Normalfunc();
        //oCanNotification= sharedPref.getBoolean("oCanNotification",true);


        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SWorkerViewHolder holder, int position) {

        final ServiceBuilding serviceBuilding = list.get(position);

        firebaseFirestore.collection(context.getString(R.string.col_sworker)).document(serviceBuilding.getS_id())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!=null)return;
                        if(documentSnapshot.exists()){
                            Swroker sworker = documentSnapshot.toObject(Swroker.class);
                            holder.name.setText(sworker.getS_name());
                            UniversalImageLoader.setImage(sworker.getThumb_s_pic(), holder.propic, null, "");

                        }
                    }
                });

        holder.lastcome.setText(normalfunc.getDateMMMMdyyyy(serviceBuilding.getLastday()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog(serviceBuilding.getS_id());
            }
        });

    }

    private void showdialog(String sid) {

        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = (View) inflater.inflate(R.layout.dialog_showlist, null);
        final RecyclerView recyclerView = convertView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final ProgressBar progressBar= convertView.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);


        firebaseFirestore.collection(context.getString(R.string.col_sworker))
                .whereEqualTo("s_id", sid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete:  cjj2 " );
                    ArrayList<Swroker> list1 = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Swroker swroker = document.toObject(Swroker.class);
                        list1.add(swroker);
                    }

                    GateAdapter gateAdapter = new GateAdapter(list1,context);
                    gateAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(gateAdapter);


                }
                else Log.d(TAG, "onComplete:  cjj3");

            }
        });

        alertDialog.setView(convertView);
        alertDialog.show();


    }
    public void  dissmissdialog(){
        alertDialog.dismiss();
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

    public class SWorkerViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView name,lastcome;
        CircleImageView propic;

        SWorkerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.propic);
            lastcome= view.findViewById(R.id.lastcome);
        }
    }




}
