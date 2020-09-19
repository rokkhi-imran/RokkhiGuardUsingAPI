package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.rokkhi.rokkhiguard.Activity.EditWrokerProfileActivity;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;

import com.rokkhi.rokkhiguard.Model.SLastHistory;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class GateAdapter extends RecyclerView.Adapter<GateAdapter.ListViewHolder> {


    public interface MyInterface{
         void dissmissdialog();
         void showprogressbar();
         void hideprogressbar();
    }

    private MyInterface myInterface;
    Normalfunc normalfunc;


    public List<Swroker> list;
    public ArrayList<ActiveFlats> allflats;
    private static final String TAG = "GateAdapter";
    SharedPreferences sharedPref;
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
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        normalfunc=new Normalfunc();


        return listViewHolder;
    }

    public void addallflats(final ListViewHolder holder) {

        final ActiveFlatAdapter activeFlatAdapter = new ActiveFlatAdapter(allflats, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View convertView = (View) inflater.inflate(R.layout.custom_list_multiple, null);
        final EditText editText = convertView.findViewById(R.id.sear);
       //convert listView to gridView
        final GridView lv = (GridView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.done);
        final Button selectbutton = convertView.findViewById(R.id.select);
        final Button unselectbutton = convertView.findViewById(R.id.deselect);
        final TextView tt = convertView.findViewById(R.id.selected);
        tt.setVisibility(View.VISIBLE);
        tt.setText(holder.total);


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

//        if (!holder.total.isEmpty()){
//
//            unSelectAllBtn.setVisibility(View.VISIBLE);
//            selectAllBtn.setVisibility(View.GONE);
//        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.flats.setText(holder.total);
                alertcompany.dismiss();
            }
        });

        selectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.total="";
                holder.historyflatid.clear();
                holder.historyflatno.clear();
                for(int i=0;i<allflats.size();i++){
                    activeFlatAdapter.changedata(allflats.get(i).getF_no(), true);

                    holder.historyflatid.add(allflats.get(i).getFlat_id());
                    holder.historyflatno.add(allflats.get(i).getF_no());

                    holder.total=  holder.total+" "+allflats.get(i).getF_no();
                    Log.e(TAG, "onClick: "+holder.total.length() );
                    tt.setText(holder.total);

                    activeFlatAdapter.notifyDataSetChanged();

                    unselectbutton.setVisibility(View.VISIBLE);
                    selectbutton.setVisibility(View.GONE);
                }
            }
        });


        unselectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.total="";
                tt.setText("");
                holder.historyflatid.clear();
                holder.historyflatno.clear();
                for(int i=0;i<allflats.size();i++){
                    activeFlatAdapter.changedata(allflats.get(i).getF_no(), false);
                    activeFlatAdapter.notifyDataSetChanged();
                    unselectbutton.setVisibility(View.GONE);
                    selectbutton.setVisibility(View.VISIBLE);
                }

                /*for(int i=0;i<allflats.size();i++){
//                    view.setBackground(ContextCompat.getDrawable(context, R.color.orange_light));
                    activeFlatAdapter.changedata(allflats.get(i).getF_no(), false);
                    activeFlatAdapter.notifyDataSetChanged();
//                    holder.historyflatno.remove(allflats.get(i));

                    holder.total=  holder.total .replace(" " + allflats.get(i).getF_no(), "");
                    tt.setText( holder.total);
                    unSelectAllBtn.setVisibility(View.GONE);
                    selectAllBtn.setVisibility(View.VISIBLE);
                }*/

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
        }
        activeFlatAdapter.notifyDataSetChanged();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ActiveFlats ss = (ActiveFlats) lv.getItemAtPosition(position);


                //selected na hoile selected er moto kaj korbe.. selection er subidhar jnno
                if (!holder.historyflatno.contains(ss.getF_no())) {

                    Log.d(TAG, "onItemClick: rrr1");

                    view.findViewById(R.id.name).setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_textsize_with_bg));
                    holder.ename.setTextColor(ContextCompat.getColor(context,R.color.white));
                    activeFlatAdapter.changedata(ss.getF_no(), true);
                    activeFlatAdapter.notifyDataSetChanged();
                    holder.historyflatid.add(ss.getFlat_id());
                    holder.historyflatno.add(ss.getF_no());
                    holder.total = holder.total + " " + ss.getF_no();
                    tt.setText(holder.total);
                    //

                } else {
                    Log.d(TAG, "onItemClick: rrr2");
                    view.findViewById(R.id.name).setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_textsize));
                    holder.ename.setTextColor(ContextCompat.getColor(context,R.color.Black));

                    activeFlatAdapter.changedata(ss.getF_no(), false);
                    activeFlatAdapter.notifyDataSetChanged();
                    holder.historyflatid.remove(ss.getFlat_id());
                    holder.historyflatno.remove(ss.getF_no());
                    holder.total = holder.total.replace(" " + ss.getF_no(), "");
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

        Glide.with(context).load(swroker.getThumb_s_pic()).placeholder(R.drawable.male1).into(holder.propic);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EditWrokerProfileActivity.class)
                .putExtra("s_id",swroker.getS_id()));
            }
        });



        holder.in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(normalfunc.checkcontainsspaceonly(holder.flats.getText().toString())) {
                    Toast.makeText(context, "Please select flats!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d(TAG, "onClick: uuuu "+ holder.flats.getText().toString());
                    myInterface.showprogressbar();

                    SLastHistory sLastHistory = new SLastHistory(swroker.getS_id(), buildid,holder.historyflatid,holder.historyflatno,Calendar.getInstance().getTime());

                }


            }
        });

        holder.out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(normalfunc.checkcontainsspaceonly(holder.flats.getText().toString())){
                    Toast.makeText(context,"Please select flats!",Toast.LENGTH_SHORT).show();
                }
                else{
                }

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
        Button in,out,editButton;
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
            editButton= view.findViewById(R.id.editButton);
            out= view.findViewById(R.id.out);
            flats= view.findViewById(R.id.flats);
            historyflatid= new ArrayList<>();
            historyflatno= new ArrayList<>();
            total=" ";

        }
    }


}