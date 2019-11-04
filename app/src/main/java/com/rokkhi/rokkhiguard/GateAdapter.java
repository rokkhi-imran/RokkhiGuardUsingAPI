package com.rokkhi.rokkhiguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.Attendence;
import com.rokkhi.rokkhiguard.Model.SLastHistory;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
    public ArrayList<ActiveFlats> allflats;
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
        firebaseFirestore.collection(context.getString(R.string.col_activeflat)).whereEqualTo("build_id",buildid).
                orderBy("f_no", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    allflats= new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        ActiveFlats activeFlat= documentSnapshot.toObject(ActiveFlats.class);
                        allflats.add(activeFlat);
                    }
                }
            }
        });

        //omail= sharedPref.getString("omail","none");
        return listViewHolder;
    }

    public void addallflats(final ListViewHolder holder) {

        final ActiveFlatAdapter activeFlatAdapter = new ActiveFlatAdapter(allflats, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.custom_list, null);
        final EditText editText = convertView.findViewById(R.id.sear);
       //convert listView to gridView
        final GridView lv = (GridView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.done);
        final TextView tt = convertView.findViewById(R.id.selected);
        tt.setVisibility(View.VISIBLE);
        tt.setText(holder.total);


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.flats.setText(holder.total);
                alertcompany.dismiss();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                activeFlatAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        for(int i=0;i<holder.historyflatno.size();i++){
            activeFlatAdapter.changedata(holder.historyflatno.get(i), true);
            activeFlatAdapter.notifyDataSetChanged();
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                typeselected = (String) lv.getItemAtPosition(position);
//                //cname.setText(myoffice.getName());
//                type.setText(typeselected);
//                alertcompany.dismiss();

                ActiveFlats ss = (ActiveFlats) lv.getItemAtPosition(position);


                //selected na hoile selected er moto kaj korbe.. selection er subidhar jnno
                if (!holder.historyflatno.contains(ss.getF_no())) {

                    Log.d(TAG, "onItemClick: rrr1");

                    view.setBackground(ContextCompat.getDrawable(context, R.color.orange_light));
                    activeFlatAdapter.changedata(ss.getF_no(), true);
                    holder.historyflatid.add(ss.getFlat_id());
                    holder.historyflatno.add(ss.getF_no());
                    holder.total = holder.total + "  " + ss.getF_no();
                    tt.setText(holder.total);
                    //activeFlatAdapter.notifyDataSetChanged();

                } else {
                    Log.d(TAG, "onItemClick: rrr2");
                    view.setBackground(ContextCompat.getDrawable(context, R.color.white));
                    activeFlatAdapter.changedata(ss.getF_no(), false);
                    holder.historyflatid.remove(ss.getFlat_id());
                    holder.historyflatno.remove(ss.getF_no());
                    holder.total = holder.total.replace("  " + ss.getF_no(), "");
                    tt.setText(holder.total);
                    // activeFlatAdapter.notifyDataSetChanged();
                }


            }
        });

    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {

        final Swroker swroker = list.get(position);
        holder.ename.setText(swroker.getS_name());
        UniversalImageLoader.setImage(swroker.getThumb_s_pic(), holder.propic, null, "");


        firebaseFirestore.collection(context.getString(R.string.col_sworker))
                .document(swroker.getS_id()).collection("shistory").document(buildid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    holder.flats.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            addallflats(holder);

                        }
                    });
                    if(documentSnapshot.exists()){
                        SLastHistory sLastHistory= documentSnapshot.toObject(SLastHistory.class);
                        holder.historyflatid=sLastHistory.getFlatsId();
                        holder.historyflatno=sLastHistory.getFlatsNo();
                        for(int i=0;i<holder.historyflatno.size();i++){
                            holder.total=holder.total + holder.historyflatno.get(i)+"  ";
                            holder.flats.setText(holder.total);

                        }

                    }
                }
            }
        });



        holder.in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.flats.getText().toString().isEmpty()){
                    Toast.makeText(context,"Please select flats!",Toast.LENGTH_SHORT).show();
                    return;
                }
                myInterface.showprogressbar();
                WriteBatch batch = firebaseFirestore.batch();



                for (int i = 0; i < holder.historyflatno.size(); i++) {

                    String auto_id= firebaseFirestore.collection(context.getString(R.string.col_attendance))
                            .document().getId();

                    Attendence attendence= new Attendence(auto_id,swroker.getS_id(),buildid,commid
                    ,Calendar.getInstance().getTime(),holder.historyflatid.get(i),
                            holder.historyflatno.get(i),true);



                    DocumentReference setattendence = firebaseFirestore.collection(context.getString(R.string.col_attendance))
                            .document(auto_id);

                    batch.set(setattendence, attendence);
                }
                SLastHistory sLastHistory = new SLastHistory(swroker.getS_id(), buildid,holder.historyflatid,holder.historyflatno,Calendar.getInstance().getTime());
                DocumentReference setflat = firebaseFirestore.collection(context.getString(R.string.col_sworker))
                        .document(swroker.getS_id()).collection("shistory").document(buildid);
                batch.set(setflat, sLastHistory);





                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                WriteBatch batch = firebaseFirestore.batch();



                for (int i = 0; i < holder.historyflatno.size(); i++) {

                    String auto_id= firebaseFirestore.collection(context.getString(R.string.col_attendance))
                            .document().getId();

                    Attendence attendence= new Attendence(auto_id,swroker.getS_id(),buildid,commid
                            ,Calendar.getInstance().getTime(),holder.historyflatid.get(i),
                            holder.historyflatno.get(i),false);




                    DocumentReference setattendence = firebaseFirestore.collection(context.getString(R.string.col_attendance))
                            .document(auto_id);

                    batch.set(setattendence, attendence);
                }



                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
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
        EditText flats;
        ArrayList<String>historyflatid;
        ArrayList<String>historyflatno;

        ListViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ename = view.findViewById(R.id.name);
            propic=view.findViewById(R.id.pic);
            in= view.findViewById(R.id.in);
            out= view.findViewById(R.id.out);
            flats= view.findViewById(R.id.flats);
            historyflatid= new ArrayList<>();
            historyflatno= new ArrayList<>();
            total="  ";

        }
    }


}
