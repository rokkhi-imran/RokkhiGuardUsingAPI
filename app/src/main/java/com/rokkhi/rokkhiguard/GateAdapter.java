package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;



public class GateAdapter extends RecyclerView.Adapter<GateAdapter.ListViewHolder> {

    public interface MyInterface{
         void dissmissdialog();
         void showprogressbar();
         void hideprogressbar();
    }

    private MyInterface myInterface;


    public List<Swroker> list;
    private static final String TAG = "GateAdapter";
    SharedPreferences sharedPref;
    FirebaseFirestore firebaseFirestore;
    String flatid = "", buildid = "", commid = "",famid="",userid="";

    private Context context;
    GateAdapter(List<Swroker> list, Context context) {
        this.list = list;
        this.context=context;
        try {
            this.myInterface = ((MyInterface) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendence, parent, false);
        ListViewHolder listViewHolder=new ListViewHolder(view);
        firebaseFirestore= FirebaseFirestore.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        //omail= sharedPref.getString("omail","none");
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        final Swroker swroker = list.get(position);
        holder.ename.setText(swroker.getS_name());
        UniversalImageLoader.setImage(swroker.getS_thumb(), holder.propic, null, "");
        holder.in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO firebase function for notifiaction
                myInterface.showprogressbar();
                Map<String,Object> mm= new HashMap<>();

                String auto_id= firebaseFirestore.collection(context.getString(R.string.col_attendance))
                        .document().getId();

                //TODO ekhane in ba out press korle ekta list show kora jaite pare je kon kon flat a jabe

                mm.put("auto_id",auto_id);
                mm.put("employee_id",swroker.getS_id());
                mm.put("build_id",buildid);
                mm.put("comm_id",commid);
                mm.put("isin",true);
                mm.put("e_checkin", FieldValue.serverTimestamp());
                mm.put("e_checkout", FieldValue.serverTimestamp());
                firebaseFirestore.collection(context.getString(R.string.col_attendance)).document(auto_id)
                        .set(mm)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                myInterface.hideprogressbar();
                                if(task.isSuccessful()){
                                    Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                                    myInterface.dissmissdialog();
                                }
                            }
                        });


            }
        });

        holder.out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.showprogressbar();
                String auto_id= firebaseFirestore.collection(context.getString(R.string.col_attendance))
                        .document().getId();
                Map<String,Object> mm= new HashMap<>();
                mm.put("auto_id",auto_id);
                mm.put("employee_id",swroker.getS_id());
                mm.put("build_id",buildid);
                mm.put("comm_id",commid);
                mm.put("isin",false);
                mm.put("e_checkin", FieldValue.serverTimestamp());
                mm.put("e_checkout", FieldValue.serverTimestamp());



                firebaseFirestore.collection(context.getString(R.string.col_attendance)).document(auto_id)
                        .set(mm)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                myInterface.hideprogressbar();
                                if(task.isSuccessful()){
                                    Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                                    myInterface.dissmissdialog();
                                }
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

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView ename ;
        CircleImageView propic;
        Button in,out;

        ListViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ename = view.findViewById(R.id.name);
            propic=view.findViewById(R.id.pic);
            in= view.findViewById(R.id.in);
            out= view.findViewById(R.id.out);

        }
    }


}
