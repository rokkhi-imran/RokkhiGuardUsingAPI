package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Model.api.RecordVehicleEntryModelClass;
import com.rokkhi.rokkhiguard.Model.api.VehicleData;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.GridViewHolder> implements Filterable {

    public List<VehicleData> list;
    private static final String TAG = "GridAdapter";
    SharedPreferences sharedPref;
    AlertDialog alertDialog;

    private ArrayList<VehicleData> mflatFilterList;
    private ValueFilter valueFilter;
    private LayoutInflater mInflater;



    @Override
    public Filter getFilter() {

        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    private Context context;

    public CarListAdapter(ArrayList<VehicleData> list, Context context) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mflatFilterList = list;

        getFilter();

        try {
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

        return gridViewHolder;
    }


    private void confirmdialog(final VehicleData vehicleData, final int position) {


        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.dialog_confirm_vehicle, null);
        final Button cancel = convertView.findViewById(R.id.cancel);
        final Button in = convertView.findViewById(R.id.in);
        final Button out = convertView.findViewById(R.id.out);
        TextView carModelTV = convertView.findViewById(R.id.carModelTV);
        String ftext = "Car model: " + vehicleData.getModel();

        carModelTV.setText(ftext);

        alertDialog.setView(convertView);
        alertDialog.setCancelable(true);
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

                String url = StaticData.baseURL + "" + StaticData.recordVehicleEntry;
                callVehicleEntryData(vehicleData,context,url);

            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = StaticData.baseURL + "" + StaticData.recordVehicleExit;
                callVehicleEntryData(vehicleData,context,url);

            }
        });

    }

    private void callVehicleEntryData(VehicleData vehicleData, Context context, String url) {


        SharedPrefHelper sharedPrefHelper=new SharedPrefHelper(context);

        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("vehicleId", String.valueOf(vehicleData.getId()));
        dataPost.put("buildingId", sharedPrefHelper.getString(StaticData.BUILD_ID));
        dataPost.put("flatId",String.valueOf( vehicleData.getFlat().getId()));
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("guardId", sharedPrefHelper.getString(StaticData.USER_ID));
        dataPost.put("acknowledgedBy", "");
        JSONObject jsonDataPost = new JSONObject(dataPost);


        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);
        Log.e("TAG", "onCreate: ---------------------- ");


        AndroidNetworking.post(url)
                .addHeaders("authtoken", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, "onResponse: =  =----------- " + response);

                        Gson gson = new Gson();
                        RecordVehicleEntryModelClass recordVehicleEntryModleClass = gson.fromJson(String.valueOf(response), RecordVehicleEntryModelClass.class);

                        StaticData.showSuccessDialog((FragmentActivity) context,"Success !","Your action completed.");


                    }

                    @Override
                    public void onError(ANError anError) {


                        StaticData.showErrorAlertDialog(context,"Alert !","আবার চেষ্টা করুন ।");

                        Log.e(TAG, "onResponse: error message =  " + anError.getMessage());
                        Log.e(TAG, "onResponse: error code =  " + anError.getErrorCode());
                        Log.e(TAG, "onResponse: error body =  " + anError.getErrorBody());
                        Log.e(TAG, "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });



    }


    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {

        final VehicleData parkings = list.get(position);
        holder.carNumberTV.setText(parkings.getNumber());
        holder.carModelTV.setText(parkings.getModel());
        holder.carColorTV.setText(parkings.getColor());
        try {
            holder.flatTV.setText("Flat: "+parkings.getFlat().getNumber());
        }catch (Exception e){

        }

        holder.view.setOnClickListener(v -> confirmdialog(list.get(position),position));

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
        TextView flatTV;

        GridViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            carNumberTV = view.findViewById(R.id.carNumberTV);
            carModelTV = view.findViewById(R.id.carModelTV);
            carColorTV = view.findViewById(R.id.carColorTV);
            flatTV = view.findViewById(R.id.flatTV);
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
                                    || mflatFilterList.get(i).getFlat().getNumber().toLowerCase().contains(constraint.toString().toLowerCase())
                                    || mflatFilterList.get(i).getFlat().getName().toLowerCase().contains(constraint.toString().toLowerCase())
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