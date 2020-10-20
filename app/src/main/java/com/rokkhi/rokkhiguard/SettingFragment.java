package com.rokkhi.rokkhiguard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rokkhi.rokkhiguard.Activity.GeneralActivity;
import com.rokkhi.rokkhiguard.Model.Reports;

import java.util.Calendar;
import java.util.Date;



public class SettingFragment extends Fragment {


    Context context;

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    AlertDialog alertDialog;
    SharedPreferences sharedPref;

    ConstraintLayout general;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String  buildid = "", commid = "";


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        context= getActivity();
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        general = view.findViewById(R.id.general);

        buttonclick();
        return view;
    }



    public void buttonclick(){
        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), GeneralActivity.class);
                startActivity(intent);

            }
        });

    }




        private void showdialogreport(){
        //signOut();
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_report, null);

        final Button cancel= convertView.findViewById(R.id.cancel);
        final Button submit= convertView.findViewById(R.id.submit);
        final EditText subject= convertView.findViewById(R.id.subject);
        final EditText body= convertView.findViewById(R.id.starttime);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stext= subject.getText().toString();
                String btext=body.getText().toString();
                Calendar calendar= Calendar.getInstance();
                Date date= calendar.getTime();

                Reports report= new Reports(stext,firebaseUser.getUid(),buildid,btext,date);


            }
        });



        alertDialog.setView(convertView);
        alertDialog.show();
    }


}
