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
import com.rokkhi.rokkhiguard.Model.api.GuardListData;
import com.rokkhi.rokkhiguard.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;



public class GuardAdapter extends BaseAdapter implements Filterable {

    private ArrayList<GuardListData> guardslist;
    private ArrayList<GuardListData> mguardsFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;

    public GuardAdapter(ArrayList<GuardListData> mStringList, Context context) {

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
        final GuardListData guardListData = guardslist.get(position);
        if (convertView == null) {
            viewHolder = new Holder();
            convertView = mInflater.inflate(R.layout.item_guard_list, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name_Guard_TV);
            viewHolder.pic =  convertView.findViewById(R.id.propic_guard_image);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        viewHolder.name.setText(guardListData.getName());

        Glide.with(convertView.getContext()).load(guardListData.getImage()).placeholder(R.drawable.male1)
                .into(viewHolder.pic);


//        UniversalImageLoader.setImage(guard.getThumb_g_pic(), viewHolder.pic, null, "");
        return convertView;
    }

    private class Holder {

        TextView name;
        CircleImageView pic;
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

                ArrayList<GuardListData> filterList = new ArrayList<>();

                for (int i = 0; i < mguardsFilterList.size(); i++) {
                    //muserFilterList.get(i).getE_name().toLowerCase().startsWith(constraint.toString().toLowerCase())
                    if (mguardsFilterList.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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

            guardslist = (ArrayList<GuardListData>) results.values;

            notifyDataSetChanged();


        }

    }
}