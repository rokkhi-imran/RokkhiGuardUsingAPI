package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rokkhi.rokkhiguard.Model.api.ServiceWorkerInOutModel;
import com.rokkhi.rokkhiguard.Model.api.ServiceWorkerListModelData;
import com.rokkhi.rokkhiguard.Model.api.ServiceWorkerListModelWorkPlace;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SWorkerAdapter extends RecyclerView.Adapter<SWorkerAdapter.SWorkerViewHolder> implements Filterable {


    private LayoutInflater mInflater;
    Normalfunc normalfunc;

    public ArrayList<ServiceWorkerListModelData> sworkerDataList;
    public ArrayList<ServiceWorkerListModelData> sworkerDataFilterList;
    private static final String TAG = "SWorkerAdapter";

    private Context context;

    private ValueFilter valueFilter;


    public SWorkerAdapter(ArrayList<ServiceWorkerListModelData> sworkerDataList, Context context) {
        this.sworkerDataList = sworkerDataList;
        this.sworkerDataFilterList = sworkerDataList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        getFilter();

    }

    @NonNull
    @Override
    public SWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sworkers, parent, false);
        SWorkerViewHolder visitorViewHolder = new SWorkerViewHolder(view);
        normalfunc = new Normalfunc();

        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SWorkerViewHolder holder, int position) {

        Log.e(TAG, "onBindViewHolder: 5 position = " + position);
        try {
            Log.e(TAG, "onBindViewHolder:1 " + sworkerDataList.toString());
            int workPlaceSize = sworkerDataList.get(position).getWorkPlace().size();
            Log.e(TAG, "onBindViewHolder: 2 = " + workPlaceSize);
            holder.flatNumber.setText("");
            for (int i = 0; i < workPlaceSize; i++) {
                holder.flatNumber.append(sworkerDataList.get(position).getWorkPlace().get(i).getFlat().getName() + " ");
            }

            Log.e(TAG, "onBindViewHolder:3 " + sworkerDataList.toString());
            holder.name.setText(sworkerDataList.get(position).getName());
            holder.lastcome.setText(sworkerDataList.get(position).getPhone());
            Picasso.get()
                    .load(sworkerDataList.get(position).getImage())
                    .fit()
                    .placeholder(R.drawable.progress_animation)
                    .into(holder.propic);

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder:4 " + e.getMessage());

        }


    }


    @Override
    public int getItemCount() {
        return sworkerDataList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {


        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;

    }

    public class SWorkerViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView name, lastcome, flatNumber;
        CircleImageView propic;

        SWorkerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.one);
            lastcome = view.findViewById(R.id.lastcome);
            flatNumber = view.findViewById(R.id.flatNumber);

            view.setOnClickListener(v -> {
                inOutSworkerAlert(context, getAdapterPosition(), sworkerDataFilterList);
            });
        }
    }

    private void inOutSworkerAlert(Context context, int adapterPosition, ArrayList<ServiceWorkerListModelData> sworkerData) {


        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = (View) inflater.inflate(R.layout.item_attendence, null);
        TextView textViewName = convertView.findViewById(R.id.name);
        CircleImageView circleImageView = convertView.findViewById(R.id.pic);

        EditText editTextFlat = convertView.findViewById(R.id.flats);
        Button buttonIn = convertView.findViewById(R.id.in);
        Button buttonOut = convertView.findViewById(R.id.out);

        textViewName.setText(sworkerData.get(adapterPosition).getName());

        if (!sworkerData.get(adapterPosition).getImage().isEmpty()) {

            Picasso.get().load(sworkerData.get(adapterPosition).getImage()).fit().placeholder(R.drawable.progress_animation).into(circleImageView);
        }

        int workPlaceSize = sworkerData.get(adapterPosition).getWorkPlace().size();
        editTextFlat.setText("");
        for (int i = 0; i < workPlaceSize; i++) {
            editTextFlat.append(sworkerData.get(adapterPosition).getWorkPlace().get(i).getFlat().getName() + " ");
        }


        buttonIn.setOnClickListener(v -> {


            String url = StaticData.baseURL + "" + StaticData.recordServiceWorkerEntry;
            callWorkerInOutFunction(context, sworkerData, adapterPosition, url, "In Alert", "Service Worker Successfully in this building");

        });
        buttonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String url = StaticData.baseURL + "" + StaticData.recordServiceWorkerExit;
                String url = StaticData.baseURL + "" + StaticData.changeServiceWorkerStatus;

                changeServiceWorkerStatus(context, sworkerData, adapterPosition, url, "OUT Alert !", "Service Worker Successfully out from the building");
            }
        });

        alertDialog.setView(convertView);
        alertDialog.show();


    }

    private void callWorkerInOutFunction(Context context, ArrayList<ServiceWorkerListModelData> sWorkerData, int adapterPosition, String url, String alertTitle, String detailsAlert) {

        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);

        FullScreenAlertDialog fullScreenAlertDialog = new FullScreenAlertDialog(context);
        fullScreenAlertDialog.showdialog();


        List<Integer> flatIdList = new ArrayList<>();


        for (ServiceWorkerListModelWorkPlace serviceWorkerListModelDataWorkPlace : sWorkerData.get(adapterPosition).getWorkPlace()) {
            flatIdList.add(serviceWorkerListModelDataWorkPlace.getFlat().getId());
        }

        ServiceWorkerInOutModel serviceWorkerInOutModel = new ServiceWorkerInOutModel(0, Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)),
                Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)), flatIdList, Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ID)),
                "", "", sWorkerData.get(adapterPosition).getId(),
                sharedPrefHelper.getString(StaticData.TIME_ZONE));


        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(serviceWorkerInOutModel);
            JSONObject jsonDataPost = new JSONObject(json);
            Log.e(TAG, "callWorkerInOutFunction: " + jsonDataPost);

            AndroidNetworking.post(url)
                    .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                    .setContentType("application/json")
                    .addJSONObjectBody(jsonDataPost)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            fullScreenAlertDialog.dismissdialog();

                            Log.e(TAG, "onResponse: =  =----------- " + response);

//                        Gson gson = new Gson();
//                        VisitorOutModelClass visitorOutModelClass = gson.fromJson(String.valueOf(response), VisitorOutModelClass.class);
                            StaticData.showSuccessDialog((FragmentActivity) context, alertTitle, detailsAlert);

                            AndroidNetworking.cancelAll();
                        }

                        @Override
                        public void onError(ANError anError) {

                            fullScreenAlertDialog.dismissdialog();

                            StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                            Log.e(TAG, "onResponse: error message =  " + anError.getMessage());
                            Log.e(TAG, "onResponse: error code =  " + anError.getErrorCode());
                            Log.e(TAG, "onResponse: error body =  " + anError.getErrorBody());
                            Log.e(TAG, "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                        }
                    });


        } catch (JsonProcessingException | JSONException e) {
            Log.e(TAG, "callWorkerInOutFunction: " + e.getMessage());
            fullScreenAlertDialog.dismissdialog();
        }

    }

    private void changeServiceWorkerStatus(Context context, ArrayList<ServiceWorkerListModelData> sWorkerData, int adapterPosition, String url, String alertTitle, String detailsAlert) {

        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);

        FullScreenAlertDialog fullScreenAlertDialog = new FullScreenAlertDialog(context);
        fullScreenAlertDialog.showdialog();


        try {
            JSONObject dataPost = new JSONObject();
            dataPost.put("limit", "");
            dataPost.put("pageId", "");
            dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
            dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
            dataPost.put("serviceWorkerId", sWorkerData.get(adapterPosition).getId());
            dataPost.put("newStatus", StaticData.OUTSIDE_COMPOUND);

            Log.e(TAG, "changeServiceWorkerStatus: " + dataPost);

            AndroidNetworking.post(url)
                    .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                    .setContentType("application/json")
                    .addJSONObjectBody(dataPost)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            fullScreenAlertDialog.dismissdialog();

                            Log.e(TAG, "changeServiceWorkerStatus onResponse: =  =----------- " + response);

//                        Gson gson = new Gson();
//                        VisitorOutModelClass visitorOutModelClass = gson.fromJson(String.valueOf(response), VisitorOutModelClass.class);
                            StaticData.showSuccessDialog((FragmentActivity) context, alertTitle, detailsAlert);

                            AndroidNetworking.cancelAll();
                        }

                        @Override
                        public void onError(ANError anError) {

                            fullScreenAlertDialog.dismissdialog();

                            StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                            Log.e(TAG, "onResponse: changeServiceWorkerStatus error message =  " + anError.getMessage());
                            Log.e(TAG, "onResponse: changeServiceWorkerStatus error code =  " + anError.getErrorCode());
                            Log.e(TAG, "onResponse: changeServiceWorkerStatus error body =  " + anError.getErrorBody());
                            Log.e(TAG, "onResponse: changeServiceWorkerStatus error  getErrorDetail =  " + anError.getErrorDetail());
                        }
                    });


        } catch (JSONException e) {
            Log.e(TAG, "callWorkerInOutFunction: " + e.getMessage());
            fullScreenAlertDialog.dismissdialog();
        }

    }


    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<ServiceWorkerListModelData> filterList = new ArrayList<>();

                for (int i = 0; i < sworkerDataFilterList.size(); i++) {
                    if (sworkerDataFilterList.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(sworkerDataFilterList.get(i));
                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {

                results.count = sworkerDataFilterList.size();

                results.values = sworkerDataFilterList;

            }

            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.values != null) {

                sworkerDataList = (ArrayList<ServiceWorkerListModelData>) results.values;
            }


            notifyDataSetChanged();

        }

    }


}
