package com.rokkhi.rokkhiguard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Buildingchild;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> implements Filterable {


    AlertDialog alertDialog, alertDialog2;

    public interface MyInterface {
        public void callparents(String number);
    }

    private MyInterface myInterface;


    private ArrayList<Buildingchild> mchildFilterList;

    private LayoutInflater mInflater;

    private ValueFilter valueFilter;



    public ArrayList<Buildingchild> list;
    private static final String TAG = "ChildAdapter";
    SharedPreferences sharedPref;

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    ChildAdapter(ArrayList<Buildingchild> list, Context context) {
        this.list = list;
        mchildFilterList = list;
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
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ChildViewHolder childViewHolder = new ChildViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        //oCanNotification= sharedPref.getBoolean("oCanNotification",true);

        return childViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChildViewHolder holder, int position) {

        final Buildingchild child = list.get(position);
        holder.name.setText(child.getM_name());
        UniversalImageLoader.setImage(child.getM_thumb(), holder.propic, null, "");
        if(child.isIsactivated()){
            holder.active.setText("ACTIVE");
            holder.active.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        else{
            holder.active.setText("NOT ACTIVE");
            holder.active.setTextColor(ContextCompat.getColor(context, R.color.darkRed));
        }
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myInterface.callparents(child.getWho_add_number());

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

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView name;
        TextView active, flat;
        CircleImageView propic;
        ImageView call;
        String phoneno="";

        ChildViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.propic);
            active = view.findViewById(R.id.activated);
            call = view.findViewById(R.id.call);
            flat = view.findViewById(R.id.flat);
        }
    }

    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<Buildingchild> filterList = new ArrayList<>();

                for (int i = 0; i < mchildFilterList.size(); i++) {
                    if (mchildFilterList.get(i).getM_name().toLowerCase().contains(constraint.toString().toLowerCase())
                            || mchildFilterList.get(i).getFlatno().toLowerCase().contains(constraint.toString().toLowerCase()
                    )) {
                        filterList.add(mchildFilterList.get(i));
                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {

                results.count = mchildFilterList.size();

                results.values = mchildFilterList;

            }

            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            list = (ArrayList<Buildingchild>) results.values;

            notifyDataSetChanged();

        }

    }


}
