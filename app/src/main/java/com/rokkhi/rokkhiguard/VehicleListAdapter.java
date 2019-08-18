package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VehicleListAdapter extends BaseAdapter  {

    public interface MyInterface{
        public int foo();
    }

    private MyInterface listener;
    private static final String TAG = "VehicleListAdapter";


    private ArrayList<Vehicle> vehicles;


    private LayoutInflater mInflater;
    Context context;

    public VehicleListAdapter(ArrayList<Vehicle> mStringList, Context context) {

        this.vehicles = mStringList;

        this.context=context;

        mInflater = LayoutInflater.from(context);
        try {
            listener = ((MyInterface) context);
        } catch (ClassCastException e) {
            Log.d(TAG, "ActiveFlatAdapter: sfsf");
        }


    }


    @Override
    public int getItemViewType(int position) {

        return position;
    }



    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {

        return vehicles.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return vehicles.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }


    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder viewHolder;

        if (convertView == null) {

            viewHolder = new Holder();

            convertView = mInflater.inflate(R.layout.item_string, null);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.lin= convertView.findViewById(R.id.lin);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }


        viewHolder.name.setText(vehicles.get(position).getVehicle_number());


        return convertView;
    }

    private class Holder {

        LinearLayout lin;
        TextView name;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }




}