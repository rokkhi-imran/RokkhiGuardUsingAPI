package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.rokkhi.rokkhiguard.Model.api.ChildData;
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


public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.VisitorViewHolder> implements Filterable {


    private ArrayList<ChildData> mvisitorFilterList;
    private ValueFilter valueFilter;

    public ArrayList<ChildData> childList;

    private Context context;
    private SharedPrefHelper sharedPrefHelper;

    public ChildListAdapter(ArrayList<ChildData> childList, Context context) {
        this.childList = childList;
        mvisitorFilterList = childList;
        this.context = context;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
        VisitorViewHolder visitorViewHolder = new VisitorViewHolder(view);


        visitorViewHolder.setIsRecyclable(false);

        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VisitorViewHolder holder, int position) {


        holder.name.setText(childList.get(position).getName());

        try {
            holder.flat.setText(childList.get(position).getFlat().getNumber());
        } catch (Exception e) {

        }

        if (childList.get(position).getImage().isEmpty()) {

        } else {

            Picasso.get().load(childList.get(position).getImage()).fit().placeholder(R.drawable.progress_animation).error(R.drawable.male1).into(holder.propic);
        }
    }


    @Override
    public int getItemCount() {
        return childList.size();
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
        TextView flat;
        CircleImageView propic;
        ImageView call;

        VisitorViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.one);
            call = view.findViewById(R.id.call);
            flat = view.findViewById(R.id.flatNumberET);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View convertView = (View) inflater.inflate(R.layout.dialog_confirm_child_out, null);
                    final Button cancel = convertView.findViewById(R.id.cancel);
                    final Button confirm = convertView.findViewById(R.id.confirm);

                    alertDialog.setView(convertView);
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

                            childOutRecord(context, getAdapterPosition());
                        }
                    });


                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!childList.get(getAdapterPosition()).getContactPersonPhone().isEmpty()) {

                        sharedPrefHelper = new SharedPrefHelper(context);
                        sharedPrefHelper.putString(StaticData.CALL_FLAT_NAME, childList.get(getAdapterPosition()).getFlat().getName());


                        String number = childList.get(getAdapterPosition()).getContactPersonPhone();
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        context.startActivity(callIntent);

                    } else {
                        showNoContactAlert();
                    }

                }
            });
        }
    }

    private void showNoContactAlert() {
        new AlertDialog.Builder(context)
                .setTitle("সমস্যা ! ")
                .setMessage("কোন নাম্বার পাওয়া যায় নাই । ")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void childOutRecord(Context context, int adapterPosition) {

        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);
        FullScreenAlertDialog fullScreenAlertDialog = new FullScreenAlertDialog(context);
        fullScreenAlertDialog.showdialog();


        Map<String, Object> dataPost = new HashMap<>();

        dataPost.put("limit", "");
        dataPost.put("pageId", "");
        dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));
        dataPost.put("requesterBuildingId", Integer.parseInt(sharedPrefHelper.getString(StaticData.BUILD_ID)));
        dataPost.put("requesterCommunityId", Integer.parseInt(sharedPrefHelper.getString(StaticData.COMM_ID)));
        dataPost.put("requesterUserRole", Integer.parseInt(sharedPrefHelper.getString(StaticData.USER_ROLE)));
        dataPost.put("requesterFlatId", 0);
        dataPost.put("childrenId", String.valueOf(childList.get(adapterPosition).getId()));
        dataPost.put("guardId", sharedPrefHelper.getString(StaticData.USER_ID));
        JSONObject jsonDataPost = new JSONObject(dataPost);


        String url = StaticData.baseURL + "" + StaticData.recordChildrenExitFromCompound;

        Log.e("TAG", "onCreate: " + jsonDataPost);
        Log.e("TAG", "onCreate: " + url);
        Log.e("TAG", "onCreate: ---------------------- ");


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

                        StaticData.showSuccessDialog((FragmentActivity) context, "Alert !", "রেকর্ড করা হয়েছে । ");

                        AndroidNetworking.cancelAll();
                    }

                    @Override
                    public void onError(ANError anError) {
                        fullScreenAlertDialog.dismissdialog();

                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                        Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                        Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                        Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                    }
                });


    }

    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            results.values = null;

            ArrayList<ChildData> filterList = new ArrayList<>();
            if (constraint != null && constraint.length() > 0) {

                filterList.clear();

                for (int i = 0; i < mvisitorFilterList.size(); i++) {
                    if (mvisitorFilterList.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase()) || mvisitorFilterList.get(i).getFlat().getName().toLowerCase().contains(constraint.toString().toLowerCase()) || mvisitorFilterList.get(i).getFlat().getNumber().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(mvisitorFilterList.get(i));
                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {
                filterList.clear();

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

            childList = (ArrayList<ChildData>) results.values;

            notifyDataSetChanged();

        }

    }


}
