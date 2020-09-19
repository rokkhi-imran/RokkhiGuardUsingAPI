package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



public class GuardAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Guards> guardslist;
    private ArrayList<Guards> mguardsFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;

    public GuardAdapter(ArrayList<Guards> mStringList, Context context) {

        this.guardslist = mStringList;
        this.mguardsFilterList = mStringList;
        mInflater = LayoutInflater.from(context);
        getFilter();
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {
        return guardslist.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {
        return guardslist.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder viewHolder;
        final Guards guard= guardslist.get(position);
        if (convertView == null) {
            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.item_list, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.pic =  convertView.findViewById(R.id.pic);
            viewHolder.green =  convertView.findViewById(R.id.green);
            viewHolder.org = convertView.findViewById(R.id.org);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        viewHolder.green.setVisibility(View.GONE);
        viewHolder.org.setText(guard.getG_org());
        viewHolder.name.setText(guard.getG_name());

        Glide.with(convertView.getContext()).load(guard.getThumb_g_pic()).placeholder(R.drawable.male1)
                .into(viewHolder.pic);


//        UniversalImageLoader.setImage(guard.getThumb_g_pic(), viewHolder.pic, null, "");
        return convertView;
    }

    private class Holder {

        TextView name,org;
        CircleImageView pic;
        ImageView green;
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

                ArrayList<Guards> filterList = new ArrayList<>();

                for (int i = 0; i < mguardsFilterList.size(); i++) {
                    //muserFilterList.get(i).getE_name().toLowerCase().startsWith(constraint.toString().toLowerCase())
                    if (mguardsFilterList.get(i).getG_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(mguardsFilterList.get(i));
                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {

                results.count = mguardsFilterList.size();

                results.values = mguardsFilterList;

            }

            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            guardslist = (ArrayList<Guards>) results.values;

            notifyDataSetChanged();


        }

    }
}