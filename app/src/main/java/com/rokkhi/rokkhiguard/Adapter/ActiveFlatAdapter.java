package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.rokkhi.rokkhiguard.Model.api.ActiveFlatData;
import com.rokkhi.rokkhiguard.Model.api.ActiveFlatsModelClass;
import com.rokkhi.rokkhiguard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ActiveFlatAdapter extends BaseAdapter implements Filterable {

    public interface MyInterface{
        public int foo();
    }

    private MyInterface listener;
    private static final String TAG = "ActiveFlatAdapter";


    private ArrayList<ActiveFlatData> activeFlats;
    private Map<String,Boolean> bb= new HashMap<>();
    private ArrayList<ActiveFlatData> mFlatFilterList;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;
    Context context;

    public ActiveFlatAdapter(ActiveFlatsModelClass mStringList, Context context) {

        this.activeFlats = (ArrayList<ActiveFlatData>) mStringList.getData();

        this.mFlatFilterList = (ArrayList<ActiveFlatData>) mStringList.getData();
        this.context=context;

        mInflater = LayoutInflater.from(context);
        try {
            listener = ((MyInterface) context);
        } catch (ClassCastException e) {
            Log.d(TAG, "ActiveFlatAdapter: sfsf");
        }

        getFilter();

        for(int i=0;i<activeFlats.size();i++){
            bb.put(activeFlats.get(i).getNumber(),false);
        }
    }





    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {

        return activeFlats.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return activeFlats.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    public void changedata(String ss,Boolean value){
        bb.put(ss,value);
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder viewHolder;

        if (convertView == null) {

            viewHolder = new Holder();

            convertView = mInflater.inflate(R.layout.item_string_with_bg, null);

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.lin= convertView.findViewById(R.id.lin);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }


        viewHolder.name.setText(activeFlats.get(position).getNumber());

        if(bb.get(activeFlats.get(position).getNumber())!=null){
            Boolean flag=bb.get(activeFlats.get(position).getNumber());
            if(flag!=null && flag){

                viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.white));
                viewHolder.name.setBackground(ContextCompat.getDrawable(context,R.drawable.rectangletextviewwithbg));

            }
            else{

                viewHolder.name.setTextColor(ContextCompat.getColor(context,R.color.black));
                viewHolder.name.setBackground(ContextCompat.getDrawable(context,R.drawable.rectangletextview));

            }

        }

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

                ArrayList<ActiveFlatData> filterList = new ArrayList<>();

                for (int i = 0; i < mFlatFilterList.size(); i++) {



                    if (mFlatFilterList.get(i).getNumber().toLowerCase().contains(constraint.toString().toLowerCase())
                    ) {

                        filterList.add(mFlatFilterList.get(i));

                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {

                results.count = mFlatFilterList.size();

                results.values = mFlatFilterList;

            }



            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.values!=null){

                activeFlats = (ArrayList<ActiveFlatData>) results.values;
            }

            notifyDataSetChanged();


        }

    }
}