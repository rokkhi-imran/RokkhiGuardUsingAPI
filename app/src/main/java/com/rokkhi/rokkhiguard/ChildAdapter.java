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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.CSecurity;
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
        public void loadagain();
    }

    private MyInterface myInterface;


    private ArrayList<CSecurity> mvisitorFilterList;

    private LayoutInflater mInflater;

    private ValueFilter valueFilter;



    public ArrayList<CSecurity> list;
    private static final String TAG = "ChildAdapter";
    SharedPreferences sharedPref;

    private Context context;
    private FirebaseFirestore firebaseFirestore;

    ChildAdapter(ArrayList<CSecurity> list, Context context) {
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
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vis, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ChildViewHolder visitorViewHolder = new ChildViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        //oCanNotification= sharedPref.getBoolean("oCanNotification",true);


        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChildViewHolder holder, int position) {

        final CSecurity child = list.get(position);
        holder.name.setText(child.getM_name());
        UniversalImageLoader.setImage(child.getM_thumb(), holder.propic, null, "");
        Date date1 = child.getStarttime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.intime.setText(simpleDateFormat.format(cal.getTime()));
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onCallBtnClick();


            }
        });


        firebaseFirestore.collection(context.getString(R.string.col_activeflat))
                .document(visitor.getFlat_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        ActiveFlats allMails = documentSnapshot.toObject(ActiveFlats.class);
                        holder.flat.setText("To: " + allMails.getF_no());
                    }
                }
            }
        });

    }

    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if (phoneno.isEmpty())callIntent.setData(Uri.parse("tel:01521110045"));
            else callIntent.setData(Uri.parse("tel:"+ phoneno));
            startActivity(callIntent);
        }else{
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
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
        TextView intime, outtime, flat;
        CircleImageView propic;
        ImageView call;

        ChildViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic = view.findViewById(R.id.propic);
            intime = view.findViewById(R.id.body);
            outtime = view.findViewById(R.id.outtime);
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

                ArrayList<Visitors> filterList = new ArrayList<>();

                for (int i = 0; i < mvisitorFilterList.size(); i++) {
                    if (mvisitorFilterList.get(i).getV_name().toLowerCase().contains(constraint.toString().toLowerCase())
                            || mvisitorFilterList.get(i).getV_gpass().toLowerCase().contains(constraint.toString().toLowerCase()
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
