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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;



public class VisitorAdapter extends RecyclerView.Adapter<VisitorAdapter.VisitorViewHolder> implements Filterable {

    AlertDialog alertDialog, alertDialog2;

    public interface MyInterface {
        public void loadagain();
    }
    private MyInterface myInterface;
    private ArrayList<Visitors> mvisitorFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;

    public ArrayList<Visitors> list;
    private static final String TAG = "VisitorAdapter";
    SharedPreferences sharedPref;

    private Context context;
    VisitorAdapter(ArrayList<Visitors> list, Context context) {
        this.list = list;
        mvisitorFilterList = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);

        getFilter();
        try {
            this.myInterface = ((MyInterface) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
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
        //oCanNotification= sharedPref.getBoolean("oCanNotification",true);


        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VisitorViewHolder holder, int position) {

        final Visitors visitor = list.get(position);
        holder.name.setText(visitor.getV_name());

        Glide.with(context).load(visitor.getThumb_v_pic()).placeholder(R.drawable.male1).into(holder.propic);

//        UniversalImageLoader.setImage(visitor.getThumb_v_pic(), holder.propic, null, "");
        Date date1 = visitor.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.intime.setText(simpleDateFormat.format(cal.getTime()));
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



            }
        });


        holder.flat.setText("To: " + visitor.getF_no());

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
        TextView intime, outtime, flat;
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
            flat = view.findViewById(R.id.towhom);
        }
    }

    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<Visitors> filterList = new ArrayList<>();

                for (int i = 0; i < mvisitorFilterList.size(); i++) {
                    if (mvisitorFilterList.get(i).getV_name().toLowerCase().contains(constraint.toString().toLowerCase())
                            || mvisitorFilterList.get(i).getV_gpass().toLowerCase().contains(constraint.toString().toLowerCase())
                            || mvisitorFilterList.get(i).getF_no().toLowerCase().contains(constraint.toString().toLowerCase()
                    )) {
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

            list = (ArrayList<Visitors>) results.values;

            notifyDataSetChanged();

        }

    }


}
