package com.rokkhi.rokkhiguard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.rokkhi.rokkhiguard.CallerApp.MainActivity;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.UDetails;
import com.rokkhi.rokkhiguard.Model.Visitors;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class VisitorWaitingAdapter extends RecyclerView.Adapter<VisitorWaitingAdapter.VisitorViewHolderClass> {

    private static final Object PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    public ArrayList<Visitors> visitorsArrayList;
    Context context;

    boolean approve;


    String phoneno = "";
    ArrayList<UDetails> flatusers;
    ActiveFlats selected;
    FirebaseFirestore firebaseFirestore;
    private String res = "pending";


    public VisitorWaitingAdapter(ArrayList<Visitors> visitorsArrayList, MainPage mainPage) {
        this.visitorsArrayList = visitorsArrayList;
        this.context = mainPage;
        firebaseFirestore = FirebaseFirestore.getInstance();

        selected = new ActiveFlats();
    }

    @NonNull
    @Override
    public VisitorViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_visitor_waiting, parent, false);

        return new VisitorViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolderClass holder, int position) {

        UniversalImageLoader.setImage(visitorsArrayList.get(position).getV_pic(), holder.imageView, null, "");
        holder.nameVisitorWaitingID.setText(visitorsArrayList.get(position).getV_name());
        holder.visitorStatus.setText(visitorsArrayList.get(position).getResponse());

        if (visitorsArrayList.get(position).getResponse().equalsIgnoreCase("rejected")) {
            holder.visitorStatus.setTextColor(ContextCompat.getColor(context,R.color.darkRed));
        }
        else if (visitorsArrayList.get(position).getResponse().equalsIgnoreCase("accepted")) {
            holder.visitorStatus.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        else if (visitorsArrayList.get(position).getResponse().equalsIgnoreCase("intercom")) {
            holder.visitorStatus.setTextColor(ContextCompat.getColor(context,R.color.yellow));
        }
        else if (visitorsArrayList.get(position).getResponse().equalsIgnoreCase("mobile")) {
            holder.visitorStatus.setTextColor(ContextCompat.getColor(context,R.color.guardlistColor));
        }
    }

    @Override
    public int getItemCount() {
        return visitorsArrayList.size();
    }

    public class VisitorViewHolderClass extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView visitorStatus;
        TextView nameVisitorWaitingID;


        public VisitorViewHolderClass(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.visitorImageID);
            visitorStatus = itemView.findViewById(R.id.visitorStatusID);
            nameVisitorWaitingID = itemView.findViewById(R.id.nameVisitorWaitingID);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    getActiveBuildings(visitorsArrayList.get(getAdapterPosition()));

                }
            });

        }
    }
    private void getActiveBuildings(final Visitors visitors) {

        flatusers= new ArrayList<>();
        firebaseFirestore.collection(context.getString(R.string.col_udetails)).whereEqualTo("flat_id",visitors.getFlat_id())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        UDetails uDetails= documentSnapshot.toObject(UDetails.class);
                        flatusers.add(uDetails);
                    }
                }
            }
        });

        firebaseFirestore.collection(context.getString(R.string.col_activeflat))
                .document(visitors.getFlat_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){

                            selected = task.getResult().toObject(ActiveFlats.class);

                            dialogconfirmation(selected,visitors,context,visitors.getV_uid());
                        }

                    }
                });
    }



    public void dialogconfirmation(ActiveFlats selected , Visitors visitors, final Context context, final String uid) {

        Log.e("TAG", "dialogconfirmation: selected.isVacant =  "+selected.isVacant());
        if (selected.isVacant() || !selected.isUsing()) {
            approve = false;
        }
        else approve = true;


        firebaseFirestore.collection(context.getString(R.string.col_eintercom)).document(selected.getFlat_id())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        if(documentSnapshot.exists()){
                            phoneno= documentSnapshot.getString("number");
                            Log.d("TAG", "onEvent: oooo "+ phoneno);
                        }
                        else phoneno="none";
                    }
                });



        Log.d("TAG", "dialogconfirmation:  approve " + approve);

        final AlertDialog alertconfirm = new AlertDialog.Builder(context).create();
//        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) LayoutInflater.from(context).inflate(R.layout.dialog_confrimation, null);
        final TextView status = convertView.findViewById(R.id.status);
        final TextView whitelisted = convertView.findViewById(R.id.whitelisted);
        final Button submit = convertView.findViewById(R.id.submit);
        final Button call = convertView.findViewById(R.id.call);
        final Button addAnother = convertView.findViewById(R.id.addAnother);
        final CircleImageView responPic = convertView.findViewById(R.id.responsepic);
        final CircleImageView enter = convertView.findViewById(R.id.enter);
        final CircleImageView cancel = convertView.findViewById(R.id.cancel);


        final ProgressBar progressBar = convertView.findViewById(R.id.dialogprogress);
        alertconfirm.setView(convertView);
        alertconfirm.setCancelable(true);

        addAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertconfirm.dismiss();
                Intent intent= new Intent(context,AddVisitor.class);
                context.startActivity(intent);
//                finish();
            }
        });



        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Map<String, Object> mm= new HashMap<>();
                mm.put("response","accepted");
                mm.put("in",true);
                mm.put("completed",true);
                mm.put("responder",FirebaseAuth.getInstance().getCurrentUser().getUid());
                mm.put("statusOfEntry","in");

                firebaseFirestore.collection(context.getString(R.string.col_visitors)).document(uid)
                        .set(mm,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            alertconfirm.dismiss();
                            cleardata();
                        }
                    }
                });

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                Map<String, Object> mm= new HashMap<>();
                mm.put("response","rejected");
                mm.put("in",false);
                mm.put("completed",true);
                mm.put("responder",FirebaseAuth.getInstance().getCurrentUser().getUid());
                mm.put("statusOfEntry","out");

                firebaseFirestore.collection(context.getString(R.string.col_visitors)).document(uid)
                        .set(mm,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            alertconfirm.dismiss();
                            cleardata();
                        }
                    }
                });
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> mm= new HashMap<>();
                mm.put("completed",true);

                progressBar.setVisibility(View.VISIBLE);

                firebaseFirestore.collection(context.getString(R.string.col_visitors)).document(uid)
                        .set(mm,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                            alertconfirm.dismiss();
                            cleardata();
                        }
                    }
                });


            }
        });




        if (approve) {
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!phoneno.equals("none") && !phoneno.isEmpty()) onCallBtnClick();
                    else{
                        showUsers();
                    }
                }
            });

            if (res.equals("whitelisted")) {
                Log.d("TAG", "dialogconfirmation: xxx");
                enter.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                status.setText("Accepted  ( গৃহীত )");
                whitelisted.setVisibility(View.VISIBLE);
                status.setTextColor(Color.GREEN);
            }

            new CountDownTimer(60000, 100) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    if (res.equals("pending") ) {
                        progressBar.setVisibility(View.GONE);
                        status.setText("No response ( সাড়া পাওয়া যায়নি )");
                        status.setTextColor(Color.BLUE);
                        call.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }.start();
            firebaseFirestore.collection(context.getString(R.string.col_visitors)).document(uid)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("TAG", "Listen failed.", e);
                                return;
                            }

                            if (documentSnapshot != null && documentSnapshot.exists()) {

                                Visitors visitors = documentSnapshot.toObject(Visitors.class);
                                res = visitors.getResponse();
                                if (res.equals("rejected")) {

                                    //vvvvvv
                                    enter.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);
                                    responPic.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.reject));
                                    submit.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    status.setText("Rejected  ( বাতিল )");
                                    status.setTextColor(Color.RED);
                                } else if (res.equals("accepted")) {
                                    enter.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);
                                    submit.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    responPic.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.accept));
                                    status.setText("Accepted  ( গৃহীত )");
                                    status.setTextColor(Color.GREEN);
                                }
                                else if(res.equals("intercom")){
//                                    enter.setVisibility(View.GONE);
//                                    cancel.setVisibility(View.GONE);
//                                    submit.setVisibility(View.VISIBLE);
                                    responPic.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.telephone));
                                    progressBar.setVisibility(View.GONE);
                                    status.setText("Call By intercom  ( গৃহীত )");
                                    status.setTextColor(ContextCompat.getColor(context,R.color.yellow));
                                }
                                else if(res.equals("mobile")){
//                                    flatusers.clear();
                                    String responder= visitors.getResponder();
                                    for(int i=0;i<flatusers.size();i++){
                                        if(!flatusers.get(i).getUser_id().equals(responder)){
                                            flatusers.remove(flatusers.get(i));
                                        }
                                    }
                                    responPic.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.smartphone));
//                                    enter.setVisibility(View.GONE);
//                                    cancel.setVisibility(View.GONE);
//                                    submit.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    status.setText("Call in mobile  ( গৃহীত )");
                                    status.setTextColor(Color.MAGENTA);
                                }

                            } else {
                                Log.d("TAG", "Current data: null");
                            }
                        }
                    });
        } else {


            progressBar.setVisibility(View.GONE);
            call.setVisibility(View.GONE);
            responPic.setVisibility(View.GONE);
            whitelisted.setVisibility(View.VISIBLE);
            whitelisted.setText(" কোন এপ ইউজার পাওয়া যায়নি। অন্য উপায়ে যোগাযোগ করুন।");
            status.setText("No response ( সাড়া পাওয়া যায়নি )");
            status.setTextColor(Color.BLUE);

        }


        alertconfirm.show();
    }

    private void onCallBtnClick(){

        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
//            context.requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
        }

        else phoneCall();


    }

    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(context, ""+phoneno, Toast.LENGTH_SHORT).show();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if (!phoneno.isEmpty())callIntent.setData(Uri.parse("tel:"+ phoneno));

            context.startActivity(callIntent);


        }else{
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showUsers() {

        final UsersAdapter usersAdapter = new UsersAdapter(flatusers, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
//        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) LayoutInflater.from(context).inflate(R.layout.custom_list_forusers, null);
        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(usersAdapter);
        alertcompany.show();



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UDetails typeselected = (UDetails) lv.getItemAtPosition(position);
                //cname.setText(myoffice.getName());

                SharedPreferences.Editor editor = context.getSharedPreferences("FlatNumber", context.MODE_PRIVATE).edit();
                editor.putString("flat", flatusers.get(position).getF_no());
                editor.putString("from", "2");
                editor.apply();

                phoneno= typeselected.getPhone();

//                onCallBtnClick();
                alertcompany.dismiss();


                view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class)
                        .putExtra("phoneNumber", phoneno));
            }
        });
    }


    public void cleardata() {
        Intent intent = new Intent(context, MainPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
