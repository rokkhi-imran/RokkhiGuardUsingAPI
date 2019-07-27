package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.WriteBatch;
import com.rokkhi.rokkhiguard.Model.Invitees;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class InviteeAdapter extends RecyclerView.Adapter<InviteeAdapter.InviteeViewHolder>  implements Filterable {


    public List<Invitees> list;
    private static final String TAG = "InviteeAdapter";
    SharedPreferences sharedPref;
    AlertDialog alertDialog;

    public interface MyInterface{
        public void loadagain();

    }
    private MyInterface myInterface;
    private ArrayList<Invitees> minviteeFilterList;
    private ValueFilter valueFilter;
    private LayoutInflater mInflater;
   // private ValueFilter valueFilter;
    private Normalfunc normalfunc;



    @Override
    public Filter getFilter() {

        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    private Context context;
    private FirebaseFirestore firebaseFirestore;
    InviteeAdapter(ArrayList<Invitees> list, Context context) {
        this.list = list;
        this.context=context;
        mInflater = LayoutInflater.from(context);
        normalfunc= new Normalfunc();
        minviteeFilterList=list;

        getFilter();
        try {
            this.myInterface = ((MyInterface) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }


    @NonNull
    @Override
    public InviteeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invitee, parent, false);
        firebaseFirestore= FirebaseFirestore.getInstance();
        InviteeViewHolder inviteeViewHolder=new InviteeViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return inviteeViewHolder;
    }

    Dialog mdialog;

    public void initdialog(){
        mdialog=new Dialog(context);

        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);

    }

    public void showdialog(){
        mdialog.show();
    }
    public void dismissdialog(){
        mdialog.dismiss();
    }

    @Override
    public void onBindViewHolder(@NonNull final InviteeViewHolder holder, int position) {

        final Invitees invitee = list.get(position);
        holder.name.setText(invitee.getI_name());
        UniversalImageLoader.setImage(invitee.getI_thumb(), holder.propic, null, "");

        firebaseFirestore.collection(context.getString(R.string.col_flats)).document(invitee.getFlat_id())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot= task.getResult();
                    if(documentSnapshot.exists()){
                        String flatno= documentSnapshot.getString("f_no");
                        holder.flat.setText(flatno);
                    }
                }
            }
        });


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog = new AlertDialog.Builder(context).create();
                final LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
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
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //progressBar.setVisibility(View.VISIBLE);
                        initdialog();
                        showdialog();

                        WriteBatch batch = firebaseFirestore.batch();





                        //office update

                        DocumentReference updatehasdone= firebaseFirestore
                                .collection(context.getString(R.string.col_invitees)).document(invitee.getI_uid());

                        batch.update(updatehasdone,"hasdone", true);



                        String id= firebaseFirestore
                                .collection(context.getString(R.string.col_visitors)).document().getId();

                        DocumentReference setvisitorinoffice=firebaseFirestore
                                .collection(context.getString(R.string.col_visitors)).document(id);


                        Date date= Calendar.getInstance().getTime();

                        List<String>ll = normalfunc.splitstring(invitee.getI_name());
                        ll.addAll(normalfunc.splitchar(invitee.getI_phone().toLowerCase()));
                        ll.add(invitee.getI_phone().toLowerCase());

                        Visitors visitor=new Visitors();

                        batch.set(setvisitorinoffice,visitor);

                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //progressBar.setVisibility(View.GONE);
                                    dismissdialog();
                                    myInterface.loadagain();

                                    alertDialog.dismiss();

                                    Log.d(TAG, "onComplete: uuk1 ");
                                }

                                else Log.d(TAG, "onComplete: uuk2 ");
                            }
                        });

                    }
                });



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
        return  position;
    }

    public class InviteeViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView name ;
        TextView flat;
        CircleImageView propic;
        Button add;

        InviteeViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.name);
            propic=view.findViewById(R.id.propic);
            flat= view.findViewById(R.id.flat);
            add= view.findViewById(R.id.add);
        }
    }


    private class ValueFilter extends Filter {
        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {

                ArrayList<Invitees> filterList = new ArrayList<>();

                for (int i = 0; i < minviteeFilterList.size(); i++) {
                    if (minviteeFilterList.get(i).getI_name().toLowerCase().contains(constraint.toString().toLowerCase())
                            || minviteeFilterList.get(i).getI_token().toLowerCase().contains(constraint.toString().toLowerCase()
                    )) {
                        filterList.add(minviteeFilterList.get(i));
                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {
                results.count = minviteeFilterList.size();
                results.values = minviteeFilterList;
            }


            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            list = (ArrayList<Invitees>) results.values;

            notifyDataSetChanged();


        }

    }


}
