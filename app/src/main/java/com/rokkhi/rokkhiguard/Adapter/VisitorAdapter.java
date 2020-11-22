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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Model.api.GetInsideVisitorData;
import com.rokkhi.rokkhiguard.Model.api.VisitorOutModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class VisitorAdapter extends RecyclerView.Adapter<VisitorAdapter.VisitorViewHolder> implements Filterable {

    AlertDialog alertDialog;


    private ArrayList<GetInsideVisitorData> mvisitorFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;

    public ArrayList<GetInsideVisitorData> list;
    private static final String TAG = "VisitorAdapter";
    SharedPreferences sharedPref;

    private Context context;

    public VisitorAdapter(ArrayList<GetInsideVisitorData> list, Context context) {
        this.list = list;
        mvisitorFilterList = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);

        getFilter();

    }

    @Override
    public Filter getFilter() {

        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vis, parent, false);
        VisitorViewHolder visitorViewHolder = new VisitorViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VisitorViewHolder holder, int position) {

        final GetInsideVisitorData visitor = list.get(position);
        holder.name.setText(visitor.getName());

        if (visitor.getImage()!=null && !visitor.getImage().isEmpty()) {

            Picasso.get().load(visitor.getImage()).placeholder( R.drawable.progress_animation ).error(R.drawable.male1).into(holder.propic);
        }


            holder.intime.setText(visitor.getInTime());




        holder.out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    public void onClick(View v) {
                        callVisitorOutFunction(context, visitor.getId(),visitor.getFlat().getId(),visitor.getName());
                    }
                });


            }
        });


        holder.visitorContactNumberTV.setText(visitor.getContact());

    }

    private void callVisitorOutFunction(Context context, int id, int visitorFlatID, String name) {


        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);

        FullScreenAlertDialog fullScreenAlertDialog = new FullScreenAlertDialog(context);


        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("visitorId", String.valueOf(id));
        dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
        dataPost.put("newStatus", StaticData.OUTSIDE_COMPOUND);
        dataPost.put("visitorName", name);
        dataPost.put("flatId", String.valueOf(visitorFlatID));

        JSONObject jsonDataPost = new JSONObject(dataPost);
        String url = StaticData.baseURL + "" + StaticData.changeVisitorStatus;

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {

                Log.e("TAG", "onSuccess: " + getTokenResult.getToken());



                AndroidNetworking.post(url)
                        .addHeaders("authtoken", getTokenResult.getToken())
                        .setContentType("application/json")
                        .addJSONObjectBody(jsonDataPost)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                fullScreenAlertDialog.dismissdialog();


                                Log.e(TAG, "onResponse: =  =----------- " + response);

                                Gson gson = new Gson();
                                VisitorOutModelClass visitorOutModelClass = gson.fromJson(String.valueOf(response), VisitorOutModelClass.class);
                                StaticData.showSuccessDialog((FragmentActivity) context, "OUT Alert !", "Successfully out");

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

    public class VisitorViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView name;
        TextView intime, outtime, visitorContactNumberTV;
        CircleImageView propic;
        ImageView out;

        VisitorViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.one);
            intime = view.findViewById(R.id.starttime);
            outtime = view.findViewById(R.id.endtime);
            out = view.findViewById(R.id.outItems);
            visitorContactNumberTV = view.findViewById(R.id.visitorContactNumberTV);
        }
    }

    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<GetInsideVisitorData> filterList = new ArrayList<>();

                for (int i = 0; i < mvisitorFilterList.size(); i++) {
                    if (mvisitorFilterList.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())

                    ) {
                        filterList.add(mvisitorFilterList.get(i));
                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {

                results.count = mvisitorFilterList.size();

                results.values = mvisitorFilterList;

            }

            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            list = (ArrayList<GetInsideVisitorData>) results.values;

            notifyDataSetChanged();

        }

    }


}
