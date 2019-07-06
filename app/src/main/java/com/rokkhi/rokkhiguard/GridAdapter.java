package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Invitees;
import com.rokkhi.rokkhiguard.Model.Parkings;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> implements Filterable {

    public List<Parkings> list;
    private static final String TAG = "GridAdapter";
    SharedPreferences sharedPref;
    AlertDialog alertDialog;



    private ArrayList<Parkings> mflatFilterList;
    private ValueFilter valueFilter;
    private LayoutInflater mInflater;
    // private ValueFilter valueFilter;
    private Normalfunc normalfunc;
    int count=0;

    @Override
    public Filter getFilter() {

        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    private Context context;
    private FirebaseFirestore firebaseFirestore;
    GridAdapter(ArrayList<Parkings> list, Context context) {
        this.list = list;
        this.context=context;
        mInflater = LayoutInflater.from(context);
        normalfunc= new Normalfunc();
        mflatFilterList=list;

        getFilter();

    }


    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        firebaseFirestore= FirebaseFirestore.getInstance();
        GridViewHolder gridViewHolder=new GridViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        initdialog();
        return gridViewHolder;
    }

    Dialog mdialog;

    public void confirmdialog(final GridViewHolder holder,final Parkings parkings) {

        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.dialog_confirm, null);
        final Button cancel = convertView.findViewById(R.id.cancel);
        final Button confirm = convertView.findViewById(R.id.confirm);

        alertDialog.setView(convertView);
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
                WriteBatch batch = firebaseFirestore.batch();
                DocumentReference updatehasdone = firebaseFirestore
                        .collection(context.getString(R.string.col_parkings)).document(parkings.getFlat_id());

                if(parkings.isVacant()){
                    Log.d(TAG, "onClick: yyy1 ");
                    batch.update(updatehasdone, "lastTime", FieldValue.serverTimestamp(),
                            "beforeLastTime",parkings.getLastTime()
                            , "vacant", false);

                    holder.view.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
                    holder.flatno.setTextColor(ContextCompat.getColor(context,R.color.white));


                }

                else{
                    Log.d(TAG, "onClick: yyy2 ");
                    batch.update(updatehasdone, "lastTime", FieldValue.serverTimestamp(),
                            "beforeLastTime",parkings.getLastTime()
                            , "vacant", true);
                    holder.view.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
                    holder.flatno.setTextColor(ContextCompat.getColor(context,R.color.orange));
                }


                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: yyy3");
                            dismissdialog();
                            alertDialog.dismiss();
                        }
                    }
                });

            }
        });

    }

    public void initdialog(){
        mdialog=new Dialog(context);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);

    }

    public void showdialog(){
        mdialog.show();
    }
    public void dismissdialog(){
        mdialog.dismiss();
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, int position) {

        final Parkings parkings = list.get(position);
        holder.flatno.setText(parkings.getF_no());
        count++;
       // Log.d(TAG, "onBindViewHolder: yyy" + count + parkings.isVacant());

        if(!parkings.isVacant()){

            Log.d(TAG, "onBindViewHolder: yyy99");
            holder.view.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
            holder.flatno.setTextColor(ContextCompat.getColor(context,R.color.white));
        }
        else{
            Log.d(TAG, "onBindViewHolder: yyy89");
            holder.view.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            holder.flatno.setTextColor(ContextCompat.getColor(context,R.color.orange));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdialog(holder,parkings);
            }
        });


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

    public class GridViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView flatno;

        GridViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            flatno= view.findViewById(R.id.flatno);
        }
    }


    private class ValueFilter extends Filter {
        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {

                ArrayList<Parkings> filterList = new ArrayList<>();

                for (int i = 0; i < mflatFilterList.size(); i++) {
                    if (mflatFilterList.get(i).getF_no().toLowerCase().contains(constraint.toString().toLowerCase())
                    ) {
                        filterList.add(mflatFilterList.get(i));
                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {
                results.count = mflatFilterList.size();
                results.values = mflatFilterList;
            }


            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            list = (ArrayList<Parkings>) results.values;

            notifyDataSetChanged();


        }

    }
}