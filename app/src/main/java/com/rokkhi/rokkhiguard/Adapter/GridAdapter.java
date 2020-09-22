package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Model.Vehicle;
import com.rokkhi.rokkhiguard.Model.api.VehicleData;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.data.VehiclesRepository;

import java.util.ArrayList;
import java.util.List;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> implements Filterable {

    public List<VehicleData> list;
    private static final String TAG = "GridAdapter";
    SharedPreferences sharedPref;
    AlertDialog alertDialog;
    Vehicle selected = new Vehicle();


    public interface MyInterface {
        ArrayList<Vehicle> fetchFlatVehicle(String flatid);
    }

    private MyInterface myInterface;


    private ArrayList<VehicleData> mflatFilterList;
    private ValueFilter valueFilter;
    private LayoutInflater mInflater;
    // private ValueFilter valueFilter;
    private Normalfunc normalfunc;

    VehiclesRepository vehiclesRepository;
    ArrayList<Vehicle> allVehicles;

    @Override
    public Filter getFilter() {

        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    private Context context;

    public GridAdapter(ArrayList<VehicleData> list, Context context) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        normalfunc = new Normalfunc();
        mflatFilterList = list;

        getFilter();

        try {
            this.myInterface = ((MyInterface) context);
        } catch (ClassCastException e) {
//            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }


    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        vehiclesRepository = new VehiclesRepository(context);
        return gridViewHolder;
    }


    private void confirmdialog(final VehicleData parkings, final int position) {


        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.dialog_confirm_vehicle, null);
        final Button cancel = convertView.findViewById(R.id.cancel);
        final Button in = convertView.findViewById(R.id.in);
        final Button out = convertView.findViewById(R.id.out);
        TextView carModelTV = convertView.findViewById(R.id.carModelTV);
        String ftext = "Car model: " + parkings.getModel();

        carModelTV.setText(ftext);

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

                Toast.makeText(context, "in", Toast.LENGTH_SHORT).show();

            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "out", Toast.LENGTH_SHORT).show();


            }
        });

    }




    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {

        final VehicleData parkings = list.get(position);
        holder.carNumberTV.setText(parkings.getNumber());
        holder.carModelTV.setText(parkings.getModel());
        holder.carColorTV.setText(parkings.getColor());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdialog(list.get(position),position);
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
        return position;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView carNumberTV;
        TextView carModelTV;
        TextView carColorTV;

        GridViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            carNumberTV = view.findViewById(R.id.carNumberTV);
            carModelTV = view.findViewById(R.id.carModelTV);
            carColorTV = view.findViewById(R.id.carColorTV);
        }

    }


    private class ValueFilter extends Filter {
        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {

                ArrayList<VehicleData> filterList = new ArrayList<>();

                for (int i = 0; i < mflatFilterList.size(); i++) {
                    if (
                            mflatFilterList.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())
                                    || mflatFilterList.get(i).getNumber().toLowerCase().contains(constraint.toString().toLowerCase())
                                    || mflatFilterList.get(i).getModel().toLowerCase().contains(constraint.toString().toLowerCase())
                                    || mflatFilterList.get(i).getColor().toLowerCase().contains(constraint.toString().toLowerCase())
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

            list = (ArrayList<VehicleData>) results.values;

            notifyDataSetChanged();


        }

    }
}