package com.rokkhi.rokkhiguard.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.rokkhi.rokkhiguard.Model.Parkings;
import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;

import java.util.ArrayList;
import java.util.List;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> implements Filterable {

    public List<Parkings> list;
    private static final String TAG = "GridAdapter";
    SharedPreferences sharedPref;
    AlertDialog alertDialog;
    Vehicle selected=new Vehicle();


    public interface MyInterface {
        ArrayList<Vehicle> fetchFlatVehicle(String flatid);
    }

    private MyInterface myInterface;



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
    GridAdapter(ArrayList<Parkings> list, Context context) {
        this.list = list;
        this.context=context;
        mInflater = LayoutInflater.from(context);
        normalfunc= new Normalfunc();
        mflatFilterList=list;

        getFilter();

        try {
            this.myInterface = ((MyInterface) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }


    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        GridViewHolder gridViewHolder=new GridViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        initdialog();
        vehiclesRepository = new VehiclesRepository(context);
        return gridViewHolder;
    }





    Dialog mdialog;

    private void showVehicleDialog(final Parkings parkings,final int position){

       ArrayList<Vehicle> flatVehicle=new ArrayList<>();

       flatVehicle=myInterface.fetchFlatVehicle(parkings.getFlat_id());

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        final VehicleListAdapter vehicleListAdapter= new VehicleListAdapter(flatVehicle,context);
        LayoutInflater  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.custom_list_for_vehicle, null);
        final Button skip = convertView.findViewById(R.id.skip);
        final ListView listView= convertView.findViewById(R.id.listView1);
        final TextView tt= convertView.findViewById(R.id.tt);


        alertDialog.setView(convertView);
        alertDialog.setCancelable(false);
        listView.setAdapter(vehicleListAdapter);
        alertDialog.show();

        if(flatVehicle.size()==0){
            tt.setVisibility(View.VISIBLE);
            skip.setText("okay");
        }


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected=null;
                alertDialog.dismiss();
                confirmdialog(parkings,position);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                selected = (Vehicle) listView.getItemAtPosition(pos);

                alertDialog.dismiss();
                confirmdialog(parkings,position);
            }
        });

    }



    private void confirmdialog(final Parkings parkings , final int position) {




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


            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String v_no="";
                if(selected==null)v_no="";
                else v_no=selected.getVehicle_id();

                list.get(position).setVacant(true);
                notifyDataSetChanged();

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
//            holder.view.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
            holder.view.setBackground(ContextCompat.getDrawable(context,R.drawable.rectangletextviewwithbg));
            holder.flatno.setTextColor(ContextCompat.getColor(context,R.color.white));
        }
        else{
            holder.view.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            holder.flatno.setTextColor(ContextCompat.getColor(context,R.color.orange));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVehicleDialog(parkings,position);
               // confirmdialog(parkings , position);
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


        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            list = (ArrayList<Parkings>) results.values;

            notifyDataSetChanged();


        }

    }
}