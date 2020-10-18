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
import com.rokkhi.rokkhiguard.Model.api.SworkerData;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class SWorkerAdapter extends RecyclerView.Adapter<SWorkerAdapter.SWorkerViewHolder> implements Filterable {


    private LayoutInflater mInflater;
    Normalfunc normalfunc;

    public ArrayList<SworkerData> sworkerDataList;
    public ArrayList<SworkerData> sworkerDataFilterList;
    private static final String TAG = "SWorkerAdapter";

    private Context context;

    private ValueFilter valueFilter;


    public SWorkerAdapter(ArrayList<SworkerData> sworkerDataList, Context context) {
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

        try {
            holder.flatNumber.setText(sworkerDataFilterList.get(position).getFlat().getName());
            holder.name.setText(sworkerDataList.get(position).getName());
            holder.lastcome.setText(sworkerDataList.get(position).getPhone());
            Picasso.get()
                    .load(sworkerDataList.get(position).getImage() )
                    .placeholder( R.drawable.progress_animation )
                    .into( holder.propic );

        } catch (Exception e) {

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
        TextView name, lastcome,flatNumber;
        CircleImageView propic;

        SWorkerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.one);
            lastcome = view.findViewById(R.id.lastcome);
            flatNumber=view.findViewById(R.id.flatNumber);

            view.setOnClickListener(v -> {
                inOutSworker(context, getAdapterPosition(), sworkerDataList);
            });
        }
    }

    private void inOutSworker(Context context, int adapterPosition, ArrayList<SworkerData> sworkerData) {


        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = (View) inflater.inflate(R.layout.item_attendence, null);
        TextView textViewName = convertView.findViewById(R.id.name);
        CircleImageView circleImageView = convertView.findViewById(R.id.pic);

        EditText editTextFlat = convertView.findViewById(R.id.flats);
        Button buttonIn = convertView.findViewById(R.id.in);
        Button buttonOut = convertView.findViewById(R.id.out);

        textViewName.setText(sworkerData.get(adapterPosition).getName());

        if(!sworkerData.get(adapterPosition).getImage().isEmpty()){

            Picasso.get().load(sworkerData.get(adapterPosition).getImage()).placeholder( R.drawable.progress_animation ).into(circleImageView);
        }

        editTextFlat.setText(sworkerData.get(adapterPosition).getFlat().getName());

        buttonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callWorkerInOutFunction(context,sworkerData,adapterPosition);

            }
        });
        buttonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWorkerInOutFunction(context,sworkerData,adapterPosition);
            }
        });

        alertDialog.setView(convertView);
        alertDialog.show();


    }

    private void callWorkerInOutFunction(Context context, ArrayList<SworkerData> sworkerData, int adapterPosition) {




        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);

        FullScreenAlertDialog fullScreenAlertDialog = new FullScreenAlertDialog(context);

        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("serviceWorkerId", String.valueOf(sworkerData.get(adapterPosition).getId()));
        dataPost.put("buildingId", sharedPrefHelper.getString(StaticData.BUILD_ID));
        dataPost.put("flatId",String.valueOf( sworkerData.get(adapterPosition).getFlat().getId()));
        dataPost.put("guardId",sharedPrefHelper.getString(StaticData.USER_ID));
        dataPost.put("acknowledgedBy","");


        JSONObject jsonDataPost = new JSONObject(dataPost);
        String url = StaticData.baseURL + "" + StaticData.recordServiceWorkerEntry;
        String token = sharedPrefHelper.getString(StaticData.KEY_FIREBASE_ID_TOKEN);


        AndroidNetworking.post(url)
                .addHeaders("authtoken", token)
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
                        StaticData.showSuccessDialog((FragmentActivity) context, "OUT Alert !", "Service Worker Successfully out from the building");

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



    }


    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<SworkerData> filterList = new ArrayList<>();

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

                sworkerDataList = (ArrayList<SworkerData>) results.values;
            }


            notifyDataSetChanged();

        }

    }


}
