package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.rokkhi.rokkhiguard.Model.UDetails;
import com.rokkhi.rokkhiguard.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersAdapter extends BaseAdapter implements Filterable {

    private ArrayList<UDetails> Stringlist;
    private ArrayList<UDetails> mStringFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;
    Context context;

    public UsersAdapter(ArrayList<UDetails> mStringList, Context context) {

        this.Stringlist = mStringList;
        this.mStringFilterList = mStringList;
        this.context= context;
        mInflater = LayoutInflater.from(context);
        getFilter();
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {
        return Stringlist.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {
        return Stringlist.get(position);
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
        final UDetails uDetails= Stringlist.get(position);
        if (convertView == null) {
            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.item_person_flat, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.flatnumber = (TextView) convertView.findViewById(R.id.flat);
            viewHolder.propic =  convertView.findViewById(R.id.one);



            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        viewHolder.name.setText(uDetails.getName());
        viewHolder.flatnumber.setText(uDetails.getF_no());
        if( !uDetails.getThumb_pic().isEmpty() && !uDetails.getThumb_pic().equals("none")){

            Glide.with(context).load(uDetails.getThumb_pic()).placeholder(R.drawable.male1).into(viewHolder.propic);

//            UniversalImageLoader.setImage(uDetails.getThumb_pic(), viewHolder.propic, null, "");
        }
        return convertView;
    }
    private class Holder {

        TextView name,flatnumber;
        CircleImageView propic;

    }

    //Returns a filter that can be used to constrain data with a filtering pattern.
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<UDetails> filterList = new ArrayList<>();

                for (int i = 0; i < mStringFilterList.size(); i++) {
                    //muserFilterList.get(i).getE_name().toLowerCase().startsWith(constraint.toString().toLowerCase())
                    if (mStringFilterList.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())
                    || mStringFilterList.get(i).getF_no().toLowerCase().contains(constraint.toString().toLowerCase())
                            || mStringFilterList.get(i).getPhone().replace("+88","").equals(constraint.toString())
                    ) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {

                results.count = mStringFilterList.size();

                results.values = mStringFilterList;

            }

            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            Stringlist = (ArrayList<UDetails>) results.values;

            notifyDataSetChanged();


        }

    }
}