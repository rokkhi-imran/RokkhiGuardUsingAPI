package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.rokkhiguard.Model.api.SworkerData;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        TextView name, lastcome;
        CircleImageView propic;

        SWorkerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.one);
            lastcome = view.findViewById(R.id.lastcome);

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

        editTextFlat.setText("No Flat Found From Api");

        alertDialog.setView(convertView);
        alertDialog.show();


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
