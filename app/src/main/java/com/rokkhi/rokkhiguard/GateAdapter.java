package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {

        final Swroker swroker = list.get(position);
        holder.ename.setText(swroker.getS_name());
        UniversalImageLoader.setImage(swroker.getS_thumb(), holder.propic, null, "");
        holder.in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                myInterface.showprogressbar();
                firebaseFirestore.collection(context.getString(R.string.col_sworker)).document(swroker.getS_id())
                        .collection(context.getString(R.string.col_sflats)).get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    ArrayList<ActiveFlats>activeFlats;
                                    activeFlats= new ArrayList<>();
                                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                                        ActiveFlats activeFlat= documentSnapshot.toObject(ActiveFlats.class);
                                        activeFlats.add(activeFlat);
                                    }
                                    holder.total="";
                                    final ActiveFlatAdapter valueAdapter=new ActiveFlatAdapter(activeFlats,context);
                                    final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View convertView = (View) inflater.inflate(R.layout.custom_list, null);
                                    final EditText editText=convertView.findViewById(R.id.sear);
                                    final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                                    final Button done = convertView.findViewById(R.id.done);
                                    final TextView selected= convertView.findViewById(R.id.selected);
                                    alertcompany.setView(convertView);
                                    alertcompany.setCancelable(false);
                                    //valueAdapter.notifyDataSetChanged();

                                    lv.setAdapter(valueAdapter);
                                    final ArrayList<String>farray=new ArrayList<>();
                                    alertcompany.show();



                                    done.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Map<String,Object> mm= new HashMap<>();

                                            String auto_id= firebaseFirestore.collection(context.getString(R.string.col_attendance))
                                                    .document().getId();

                                            //TODO ekhane in ba out press korle ekta list show kora jaite pare je kon kon flat a jabe

                                            mm.put("auto_id",auto_id);
                                            mm.put("employee_id",swroker.getS_id());
                                            mm.put("build_id",buildid);
                                            mm.put("comm_id",commid);
                                            mm.put("isin",true);
                                            mm.put("flat_array",farray);
                                            mm.put("e_checkin", FieldValue.serverTimestamp());
                                            mm.put("e_checkout", FieldValue.serverTimestamp());
                                            firebaseFirestore.collection(context.getString(R.string.col_attendance)).document(auto_id)
                                                    .set(mm)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            myInterface.hideprogressbar();
                                                            if(task.isSuccessful()){
                                                                alertcompany.dismiss();
                                                                Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                                                                myInterface.dissmissdialog();
                                                            }
                                                        }
                                                    });
                                            alertcompany.dismiss();
                                        }
                                    });
                                    editText.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            valueAdapter.getFilter().filter(s);
                                        }
                                        @Override
                                        public void afterTextChanged(Editable s) {
                                        }
                                    });

                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if(!view.isSelected()){
                                                ActiveFlats aa=(ActiveFlats) lv.getItemAtPosition(position);
                                                farray.add(aa.getFlat_id());
                                                view.setSelected(true);
                                                holder.total=holder.total+aa.getF_no()+" ";
                                            }
                                            else{
                                                ActiveFlats aa=(ActiveFlats) lv.getItemAtPosition(position);
                                                farray.remove(aa.getFlat_id());
                                                view.setSelected(true);
                                                holder.total.replace(aa.getF_no()+" ","");
                                               // holder.total.replace(aa.getF_no()+" ","");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                );



                //TODO firebase function for notifiaction



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
                        .set(mm,SetOptions.merge())
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
        String total;

        ListViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ename = view.findViewById(R.id.name);
            propic=view.findViewById(R.id.pic);
            in= view.findViewById(R.id.in);
            out= view.findViewById(R.id.out);
            total="";

        }
    }


}
