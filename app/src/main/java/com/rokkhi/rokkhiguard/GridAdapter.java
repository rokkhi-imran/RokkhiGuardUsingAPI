package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.StringAdapter;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;

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

    VehiclesRepository vehiclesRepository ;
    ArrayList<Vehicle> allVehicles;

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
        vehiclesRepository = new VehiclesRepository(context);
        return gridViewHolder;
    }





    Dialog mdialog;

    private void showVehicleDialog(final Parkings parkings,final int position){

        //TODO allvehicle ene filter kora


//        vehiclesRepository.getVehicleFromPhoneAndFlatId(parkings.getFlat_id()).observe((LifecycleOwner) context, new Observer<List<Vehicle>>() {
//            @Override
//            public void onChanged(@Nullable List<Vehicle> allVehicles) {
//                for(Vehicle vehicle : allVehicles) {
//
//                    Log.d("room" , "found a new Vehicle   " + vehicle.getF_no()+"  -- > " + vehicle.getFlat_id());
//                }
//            }
//        });

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        final StringAdapter stringAdapter= new
        LayoutInflater  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.custom_recycler, null);
        final Button skip = convertView.findViewById(R.id.skip);
        RecyclerView recyclerView= convertView.findViewById(R.id.listView1);
        TextView flatno= convertView.findViewById(R.id.flatno);
        String ftext="Flatno: "+ parkings.getF_no();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        flatno.setText(ftext);

        alertDialog.setView(convertView);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void confirmdialog(final Parkings parkings , final int position) {



//        vehiclesRepository.getAllVehicle().observe(this, new Observer<List<Vehicle>>() {
//            @Override
//            public void onChanged(@Nullable List<Vehicle> allVehicles) {
//                for(Vehicle vehicle : allVehicles) {
//                    Log.d("room" , "found a new Vehicle   " + vehicle.getF_no()+"  -- > " + vehicle.getFlat_id());
//                }
//            }
//        });



        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.dialog_confirm_vehicle, null);
        final Button cancel = convertView.findViewById(R.id.cancel);
        final Button in = convertView.findViewById(R.id.in);
        final Button out = convertView.findViewById(R.id.out);
        TextView flatno= convertView.findViewById(R.id.flatno);
        String ftext="Flatno: "+ parkings.getF_no();

        flatno.setText(ftext);

        alertDialog.setView(convertView);
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
                WriteBatch batch = firebaseFirestore.batch();
                DocumentReference updatehasdone = firebaseFirestore
                        .collection(context.getString(R.string.col_parkings)).document(parkings.getFlat_id());

                batch.update(updatehasdone, "lastTime", FieldValue.serverTimestamp()
                        , "vacant", false);
                list.get(position).setVacant(false);
                notifyDataSetChanged();

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dismissdialog();
                            alertDialog.dismiss();
                        }
                    }
                });


            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
                WriteBatch batch = firebaseFirestore.batch();
                DocumentReference updatehasdone = firebaseFirestore
                        .collection(context.getString(R.string.col_parkings)).document(parkings.getFlat_id());
                batch.update(updatehasdone, "lastTime", FieldValue.serverTimestamp()
                        , "vacant", true);
                list.get(position).setVacant(true);
                notifyDataSetChanged();

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {

        final Parkings parkings = list.get(position);
        holder.flatno.setText(parkings.getF_no());

        if(!parkings.isVacant()){
            holder.view.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
            holder.flatno.setTextColor(ContextCompat.getColor(context,R.color.white));
        }
        else{
            holder.view.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            holder.flatno.setTextColor(ContextCompat.getColor(context,R.color.orange));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdialog(parkings , position);
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