package com.rokkhi.rokkhiguard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.rokkhiguard.CallerApp.MainActivity;
import com.rokkhi.rokkhiguard.Model.Child;
import com.rokkhi.rokkhiguard.Model.UDetails;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> implements Filterable {


    private static final String TAG = "ChildAdapter";
    public ArrayList<Child> list;
    AlertDialog alertDialog, alertDialog2;
    SharedPreferences sharedPref;
    private MyInterface myInterface;
    private ArrayList<Child> mchildFilterList;
    private ArrayList<UDetails> flatusers;
    private LayoutInflater mInflater;
    private ValueFilter valueFilter;
    private Context context;
    private FirebaseFirestore firebaseFirestore;

    ChildAdapter(ArrayList<Child> list, Context context) {
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

        final Child child = list.get(position);
        holder.name.setText(child.getM_name());
        holder.flat.setText("Flat:  " + child.getF_no());
        UniversalImageLoader.setImage(child.getThumb_m_pic(), holder.propic, null, "");
        if (child.isActivated()) {
            holder.active.setText("ACTIVE");
            holder.active.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.active.setText("NOT ACTIVE");
            holder.active.setTextColor(ContextCompat.getColor(context, R.color.darkRed));
        }
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = context.getSharedPreferences("FlatNumber", MODE_PRIVATE).edit();
                editor.putString("flat", child.getF_no());
                editor.apply();
/*
//make phone call
                view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class)
                .putExtra("phoneNumber",child.getPhoneno()));
//                myInterface.callparents(child.getPhoneno());

                */
                getFlatUser(child.getFlat_id());


            }
        });


    }

    private void getFlatUser(String flat_id) {
        flatusers = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting Number List");
        progressDialog.show();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(context.getString(R.string.col_userdetails)).whereEqualTo("flat_id", flat_id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        UDetails uDetails = documentSnapshot.toObject(UDetails.class);
                        flatusers.add(uDetails);
                        Log.e(TAG, "onComplete: " + uDetails.getPhone());
                    }

//                    progressDialog.dismiss();
                    showUsers(context, flatusers,progressDialog);
                }
            }
        });
    }


    public void showUsers(Context context, ArrayList<UDetails> flatusers, ProgressDialog progressDialog) {

        final UsersAdapter usersAdapter = new UsersAdapter(this.flatusers, this.context);
        final AlertDialog alertcompany = new AlertDialog.Builder(this.context).create();
//        LayoutInflater inflater = context.getLayoutInflater();
        View convertView = (View) LayoutInflater.from(context).inflate(R.layout.custom_list_forusers, null);
        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        alertcompany.setView(convertView);
        alertcompany.setCancelable(true);
        //valueAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
        lv.setAdapter(usersAdapter);
        alertcompany.show();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UDetails typeselected = (UDetails) lv.getItemAtPosition(position);
                //cname.setText(myoffice.getName());
                String phoneno = typeselected.getPhone();
//                onCallBtnClick();

                view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class)
                        .putExtra("phoneNumber", phoneno));

                alertcompany.dismiss();
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

    public interface MyInterface {
        public void callparents(String number);
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView name;
        TextView active, flat;
        CircleImageView propic;
        ImageView call;
        String phoneno = "";

        ChildViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.one);
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

                ArrayList<Child> filterList = new ArrayList<>();

                for (int i = 0; i < mchildFilterList.size(); i++) {
                    if (mchildFilterList.get(i).getM_name().toLowerCase().contains(constraint.toString().toLowerCase())
                            || mchildFilterList.get(i).getF_no().toLowerCase().contains(constraint.toString().toLowerCase()
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

            list = (ArrayList<Child>) results.values;

            notifyDataSetChanged();

        }

    }


}
