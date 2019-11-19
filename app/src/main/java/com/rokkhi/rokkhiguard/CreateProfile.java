package com.rokkhi.rokkhiguard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.rokkhiguard.Model.ActiveFlats;
import com.rokkhi.rokkhiguard.Model.BuildingChanges;
import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.Model.SLastHistory;
import com.rokkhi.rokkhiguard.Model.Swroker;
import com.rokkhi.rokkhiguard.Model.Types;
import com.rokkhi.rokkhiguard.Model.Whitelist;
import com.rokkhi.rokkhiguard.Utils.Normalfunc;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;
import com.rokkhi.rokkhiguard.data.FlatsRepository;
import com.rokkhi.rokkhiguard.data.WhiteListRepository;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateProfile extends AppCompatActivity implements ActiveFlatAdapter.MyInterface, IPickResult {


    private static final String TAG = "CreateProfile";
    CircleImageView userphoto;
    ArrayList<Types> types;
    ArrayList<ActiveFlats> activeFlats;
    EditText username, phone, type, flats, pins;
    Button done, generate;
    Map<String, Object> doc, shistory;
    String mFileUri = "";
    Context context;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    ProgressBar progressBar;
    StorageReference photoRef;
    Calendar myCalendar;
    Types typeselected;
    ActiveFlats flatselected;
    AlertDialog alertDialog;
    Normalfunc normalfunc;
    List<ActiveFlats> historyFlats;
    String totaltext = "";
    boolean flag;
    List<Swroker> list;
    String buildid, commid = "";
    Swroker swroker = null;
    String thismobileuid;
    FlatsRepository flatsRepository;
    WhiteListRepository whiteListRepository;
    ArrayList<String> wflats;
    ArrayList<Whitelist> whitelists;
    int mPosition = -1;
    Dialog mdialog;
    //    ArrayList<ActiveFlats> allflats;
    private Bitmap bitmap = null;
    private long mLastClickTime = 0;


    //add onstart method and check  data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        normalfunc = new Normalfunc();
        flag = false;

        context = CreateProfile.this;

        done = findViewById(R.id.done);
        username = findViewById(R.id.user_name);
        phone = findViewById(R.id.user_Phone_ET);
        //changepic = findViewById(R.id.changeProfilePhoto);
        userphoto = findViewById(R.id.user_photo);
        progressBar = findViewById(R.id.progressBar1);
        myCalendar = Calendar.getInstance();
        type = findViewById(R.id.user_wtype);
        flats = findViewById(R.id.user_flat);
        pins = findViewById(R.id.user_pin);
        generate = findViewById(R.id.generatepin);

        //get user mobile id
        thismobileuid = FirebaseAuth.getInstance().getUid();
        flatsRepository = new FlatsRepository(this);
        whiteListRepository = new WhiteListRepository(this);

        Log.d(TAG, "upload: yyyy " + normalfunc.makephone14("01703248520"));


        historyFlats = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        initonclick();

        types = new ArrayList<>();
        firebaseFirestore.collection("stype").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                types = new ArrayList<>();
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Types types1 = documentSnapshot.toObject(Types.class);

                        types.add(types1);
                    }
                }
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = normalfunc.getRandomNumberString5();
                pins.setText(ss);
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 6) {
                    String ss = s.toString().substring(6);
                    pins.setText(ss);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseFirestore.getInstance().collection("buildingChanges").document(buildid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    BuildingChanges buildingChanges = documentSnapshot.toObject(BuildingChanges.class);
                    ArrayList<String> flats = buildingChanges.getFlats();
                    ArrayList<String> whitelists = buildingChanges.getWhitelists();
                    ArrayList<String> vehicles = buildingChanges.getVehicles();


                    if (!flats.contains(thismobileuid)) {
                        Log.d("firebase", "Getting new Flats data because data is changed or updated");

                        getAllActiveFlatsAndSaveToLocalDatabase(buildingChanges);


                        //TODO alhn ei uid add korte hbe database a

                    } else {
                        Log.d("firebase", " Flats data is not changed or updated");
                    }


                    if (!whitelists.contains(thismobileuid)) {
                        getAllWhiteListAndSaveToLocalDatabase(buildingChanges);
                    }


                }
            }
        });


        whiteListRepository.getAllWhiteList().observe(this, new Observer<List<Whitelist>>() {
            @Override
            public void onChanged(@Nullable List<Whitelist> allWhiteLists) {
                wflats = new ArrayList<>();
                whitelists = new ArrayList<>();
                for (Whitelist whiteList : allWhiteLists) {
                    wflats.add(whiteList.getW_phone() + whiteList.getFlat_id());
                    whitelists.add(whiteList);
                    Log.d("room", "found a new WhiteList   " + whiteList.getF_no() + "  -- > " + whiteList.getFlat_id());
                }
            }
        });

        addallflats();

    }

    public void addallflats() {


        //getting the data from repository example
        //final FlatsRepository flatsRepository = new FlatsRepository(this);
        flatsRepository.getAllActiveFlats().observe(this, new Observer<List<ActiveFlats>>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable List<ActiveFlats> allFlatss) {
                activeFlats = new ArrayList<>();
                for (ActiveFlats flat : allFlatss) {
                    activeFlats.add(flat);
                    Log.d("room", "found a new Flat   " + flat.getF_no() + "  -- > " + flat.getFlat_id());
                }
            }
        });


    }

    public void getAllActiveFlatsAndSaveToLocalDatabase(final BuildingChanges buildingChanges) {


        // allActiveFlats = new ArrayList<>();
        // final FlatsRepository flatsRepository = new FlatsRepository(this);

        firebaseFirestore.collection(getString(R.string.col_activeflat))
                .whereEqualTo("build_id", buildid).orderBy("f_no", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Data Loaded");

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                        ActiveFlats activeFlat = documentSnapshot.toObject(ActiveFlats.class);

                        flatsRepository.deleteActiveFlat(activeFlat);
                        flatsRepository.insertActiveFlat(activeFlat);
                        // allActiveFlats.add(activeFlat);
                    }

                    Map<String, Object> data = new HashMap<>();
                    ArrayList<String> flatdata = new ArrayList<>();
                    flatdata = buildingChanges.getFlats();
                    flatdata.add(thismobileuid);
                    data.put("flats", flatdata);


                    firebaseFirestore.collection("buildingChanges").document(buildid)
                            .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Flat data changed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public void getAllWhiteListAndSaveToLocalDatabase(final BuildingChanges buildingChanges) {
        //allWhiteLists = new ArrayList<>();
        //final FlatsRepository flatsRepository = new FlatsRepository(this);


        firebaseFirestore.collection(getString(R.string.col_whitelists))
                .whereEqualTo("build_id", buildid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Whitelist whitelist = documentSnapshot.toObject(Whitelist.class);
                        whiteListRepository.deleteWhiteList(whitelist);
                        whiteListRepository.insert(whitelist);
                    }


                    Map<String, Object> data = new HashMap<>();
                    ArrayList<String> wldata = new ArrayList<>();
                    wldata = buildingChanges.getWhitelists();
                    wldata.add(thismobileuid);
                    data.put("whitelists", wldata);


                    firebaseFirestore.collection("buildingChanges").document(buildid)
                            .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Whitelists data changed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    whiteListRepository.deleteTask(buildid);

                } else {
                    Log.d(TAG, "onComplete: xxx5");
                }
            }
        });

    }

    public void initdialog() {
        mdialog = new Dialog(this);

        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void showdialog() {
        mdialog.show();
    }

    public void dismissdialog() {
        mdialog.dismiss();
    }

    public void showAllflats() {
//        Toast.makeText(context, "Create Profile Flat = "+activeFlats.size(), Toast.LENGTH_SHORT).show();

        final ActiveFlatAdapter activeFlatAdapter = new ActiveFlatAdapter(activeFlats, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_list_multiple, null);
        final EditText editText = convertView.findViewById(R.id.sear);
        //change Listview to Gridview
        final GridView lv = (GridView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.done);
        final Button selectbutton = convertView.findViewById(R.id.select);
        final Button unselectbutton = convertView.findViewById(R.id.deselect);
        final TextView tt = convertView.findViewById(R.id.selected);
        tt.setVisibility(View.VISIBLE);
        totaltext = "";


        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(activeFlatAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flats.setText(totaltext);
                alertcompany.dismiss();
            }
        });

        selectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < activeFlats.size(); i++) {
//                    view.setBackground(ContextCompat.getDrawable(context, R.color.orange_light));
                    activeFlatAdapter.changedata(activeFlats.get(i).getF_no(), true);
                    activeFlatAdapter.notifyDataSetChanged();
                    historyFlats.add(activeFlats.get(i));
                    totaltext = totaltext + "  " + activeFlats.get(i).getF_no();
                    tt.setText(totaltext);
                    unselectbutton.setVisibility(View.VISIBLE);
                    selectbutton.setVisibility(View.GONE);
                }
            }
        });

        unselectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < activeFlats.size(); i++) {
//                    view.setBackground(ContextCompat.getDrawable(context, R.color.orange_light));
                    activeFlatAdapter.changedata(activeFlats.get(i).getF_no(), false);
                    activeFlatAdapter.notifyDataSetChanged();
                    historyFlats.remove(activeFlats.get(i));
                    totaltext = totaltext.replace("  " + activeFlats.get(i).getF_no(), "");
                    tt.setText(totaltext);
                    unselectbutton.setVisibility(View.GONE);
                    selectbutton.setVisibility(View.VISIBLE);
                }
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


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                typeselected = (String) lv.getItemAtPosition(position);
//                //cname.setText(myoffice.getName());
//                type.setText(typeselected);
//                alertcompany.dismiss();

                ActiveFlats ss = (ActiveFlats) lv.getItemAtPosition(position);


                //selected na hoile selected er moto kaj korbe.. selection er subidhar jnno
                if (!historyFlats.contains(ss)) {

                    //view.setBackground(ContextCompat.getDrawable(context, R.color.orange_light));
                    activeFlatAdapter.changedata(ss.getF_no(), true);
                    historyFlats.add(ss);
                    activeFlatAdapter.notifyDataSetChanged();
                    totaltext = totaltext + "  " + ss.getF_no();
                    tt.setText(totaltext);
                    //activeFlatAdapter.notifyDataSetChanged();

                } else {
                    //view.setBackground(ContextCompat.getDrawable(context, R.color.white));
                    activeFlatAdapter.changedata(ss.getF_no(), false);
                    historyFlats.remove(ss);
                    totaltext = totaltext.replace("  " + ss.getF_no(), "");
                    activeFlatAdapter.notifyDataSetChanged();
                    tt.setText(totaltext);
                    // activeFlatAdapter.notifyDataSetChanged();
                }


            }
        });

    }

    public void addalltypes() {

        final TypesAdapter valueAdapter = new TypesAdapter(types, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_designation_list, null);
        final EditText editText = convertView.findViewById(R.id.sear);

        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        final Button done = convertView.findViewById(R.id.done);
        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        editText.setVisibility(View.GONE);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(valueAdapter);
        alertcompany.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //type.setText(editText.getText().toString());
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
                typeselected = (Types) lv.getItemAtPosition(position);
                //cname.setText(myoffice.getName());
                type.setText(typeselected.getBangla());
                alertcompany.dismiss();
            }
        });
    }


    public void initonclick() {

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11 && !flag) {
                    String pp = s.toString();
                    flag = true;
                    pp = normalfunc.makephone14(pp);

                    firebaseFirestore.collection(getString(R.string.col_sworker)).whereEqualTo("s_phone", pp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            list = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Swroker swroker = document.toObject(Swroker.class);
                                list.add(swroker);
                            }

                            if (list.size() > 0) {
                                alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setCancelable(false);
                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.item_person_profile, null);
                                final TextView name = convertView.findViewById(R.id.name);
                                final TextView gatepass = convertView.findViewById(R.id.pass);
                                CircleImageView pic = convertView.findViewById(R.id.propic);
                                Button cancel = convertView.findViewById(R.id.cancel);
                                Button edit = convertView.findViewById(R.id.edit);
                                TextView cc = convertView.findViewById(R.id.cc);
//                                cc.setVisibility(View.GONE);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                                edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(context, "edit Profile", Toast.LENGTH_SHORT).show();

                                        Log.e(TAG, "onClick: " + list.get(0).getS_id());
                                        Log.e(TAG, "onClick: " + list.get(0).getWho_add());

                                        startActivity(new Intent(CreateProfile.this, EditVisitorProfileActivity.class)
                                                .putExtra("s_id", list.get(0).getS_id())
                                                .putExtra("who_add", list.get(0).getWho_add())
                                                .putExtra("name", list.get(0).getS_name())
                                                .putExtra("", list.get(0).getS_phone())
                                        );


                                        alertDialog.dismiss();
/*
                                        swroker=new Swroker();

                                        Log.e(TAG, "onClick: "+list.get(0).getS_pic());
                                        Log.e(TAG, "onClick: "+list.get(0).getS_name());
                                        Log.e(TAG, "onClick: "+list.get(0).getS_mail());
                                        Log.e(TAG, "onClick: "+list.get(0).getS_id());
                                        Log.e(TAG, "onClick: "+list.get(0).getNid());
                                        Log.e(TAG, "onClick: "+list.get(0).getS_pass());
                                        Log.e(TAG, "onClick: "+list.get(0).getS_phone());
                                        Log.e(TAG, "onClick: "+list.get(0).getThumb_s_pic());
                                        Log.e(TAG, "onClick: "+list.get(0).getType());
                                        Log.e(TAG, "onClick: "+list.get(0).getWho_add());
                                        Log.e(TAG, "onClick: "+list.get(0).getWho_add());

//                                        EditText username, phone, type, flats, pins;
                                        UniversalImageLoader.setImage(list.get(0).getS_pic(), userphoto, null, "");
                                        phone.setText(list.get(0).getS_phone());
                                        pins.setText(list.get(0).getS_pass());
                                        username.setText(list.get(0).getS_name());
                                        type.setText(list.get(0).getType());

//                                        flats.setText(list.get(0));

                                        alertDialog.dismiss();*/

                                    }
                                });

                                name.setText(list.get(0).getS_name());
                                gatepass.setText("Gatepass: " + list.get(0).getS_pass());
                                UniversalImageLoader.setImage(list.get(0).getThumb_s_pic(), pic, null, "");

                                alertDialog.setView(convertView);
                                alertDialog.show();
                            }
                        }
                    });

                } else if (s.length() != 11) flag = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addalltypes();
            }
        });

        flats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllflats();
            }
        });

        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup setup = new PickSetup()
                        .setTitle("Choose Photo")
                        .setBackgroundColor(Color.WHITE)
                        .setButtonOrientation(LinearLayout.HORIZONTAL)
                        .setGalleryButtonText("Gallery")
                        .setCameraIcon(R.mipmap.camera_colored)
                        .setGalleryIcon(R.mipmap.gallery_colored)
                        .setCameraToPictures(false)
                        .setMaxSize(300);

                PickImageDialog.build(setup)
                        //.setOnClick(this)
                        .show(CreateProfile.this);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: yyyy");


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                username.setError(null);
                phone.setError(null);
                type.setError(null);

                // Store values at the time of the login attempt.
                final String iname = username.getText().toString();
                final String phoneno = phone.getText().toString();
                final String typetext = type.getText().toString();
                final String pintext = pins.getText().toString();


                boolean cancel = false;
                View focusView = null;


                if (TextUtils.isEmpty(iname)) {
                    username.setError(getString(R.string.error_field_required));
                    focusView = username;
                    cancel = true;

                }

                if (TextUtils.isEmpty(phoneno) && TextUtils.isEmpty(pintext)) {
                    phone.setError(getString(R.string.error_field_required));
                    focusView = phone;
                    cancel = true;
                }
                if (TextUtils.isEmpty(typetext)) {
                    type.setError(getString(R.string.error_field_required));
                    focusView = type;
                    cancel = true;
                }

                if (!normalfunc.isvalidphone(phoneno)) {
                    phone.setError("Invalid Phone no");
                    focusView = phone;
                    cancel = true;
                }


                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    if (firebaseUser == null) return;
                    initdialog();
                    showdialog();

                    upload();

                }


            }


        });
    }


    private Date futuredate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, 4000);
        Date date = calendar.getTime();
        return date;
    }


    public void upload() {


        List<String> ll = normalfunc.splitstring(username.getText().toString());
        ll.add(normalfunc.makephone11(phone.getText().toString()));
        ll.add(normalfunc.makephone14(phone.getText().toString()));
        ll.addAll(normalfunc.splitchar(typeselected.getEnglish().toLowerCase()));
        final List<String> ll1 = ll;
//        ll.add(mail.getText().toString().toLowerCase());


        final String s_id = firebaseFirestore.collection(getString(R.string.col_sworker)).document().getId();

        doc = new HashMap<>();
        doc.put("s_name", username.getText().toString()); //
        doc.put("s_id", s_id);
        doc.put("s_phone", normalfunc.makephone14(phone.getText().toString()));
        doc.put("s_mail", "");
        doc.put("s_pic", "");//
        doc.put("thumb_s_pic", "");
        doc.put("s_bday", futuredate());
        doc.put("experience", futuredate());
        doc.put("starttime", 0);
        doc.put("endtime", 0);
        doc.put("nid", "");
        doc.put("type", typeselected.getEnglish());//
        doc.put("who_add", firebaseUser.getUid());
        doc.put("when_add", FieldValue.serverTimestamp());
        //when_updated and who_updated
        doc.put("s_pass", pins.getText().toString()); //
        doc.put("address", new ArrayList<>());
        doc.put("s_array", ll); //


        photoRef = FirebaseStorage.getInstance().getReference()
                .child("sworkers/" + s_id + "/s_pic");


        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        if (bitmap != null) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] data = baos.toByteArray();

            UploadTask uploadTask = photoRef.putBytes(data);
            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded
                            Log.d(TAG, "uploadFromUri:onSuccess yyyy");

                            // Get the public download URL
                            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String picurl = uri.toString();
                                    doc.put("s_pic", picurl);
                                    doc.put("thumb_s_pic", picurl);

                                    WriteBatch batch = firebaseFirestore.batch();


                                    //office update

                                    DocumentReference setsworker = firebaseFirestore.collection(getString(R.string.col_sworker))
                                            .document(s_id);

                                    batch.set(setsworker, doc);

                                    if (typeselected.getEnglish().equals("guard")) {

                                        final Guards guards = new Guards(buildid, commid, username.getText().toString()
                                                , normalfunc.getRandomNumberString5(), "", Calendar.getInstance().getTime(), normalfunc.futuredate(), "", picurl, "", picurl,
                                                normalfunc.makephone14(phone.getText().toString()), s_id, ll1);
                                        DocumentReference setguard = firebaseFirestore.collection(getString(R.string.col_guards))
                                                .document(s_id);

                                        batch.set(setguard, guards);

                                    }

                                    ArrayList<String> stringid = new ArrayList<>();
                                    ArrayList<String> stringno = new ArrayList<>();

                                    for (int i = 0; i < historyFlats.size(); i++) {
                                        stringid.add(historyFlats.get(i).getFlat_id());
                                        stringno.add(historyFlats.get(i).getF_no());
                                    }

                                    SLastHistory sLastHistory = new SLastHistory(s_id, buildid, stringid, stringno, Calendar.getInstance().getTime());
                                    DocumentReference setflat = firebaseFirestore.collection(getString(R.string.col_sworker))
                                            .document(s_id).collection("shistory").document(buildid);

                                    batch.set(setflat, sLastHistory);


                                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "onComplete: yyyy");
                                                //progressBar.setVisibility(View.GONE);
                                                dismissdialog();
                                                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                                                username.setText("");
                                                phone.setText("");
                                                flats.setText("");
                                                type.setText("");
                                                UniversalImageLoader.setImage("", userphoto, null, "");
                                            }
                                        }
                                    });

                                }
                            });


                            // [END_EXCLUDE]
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Upload failed
                            Log.w(TAG, "uploadFromUri:onFailure", exception);

                        }
                    });
        } else {
            WriteBatch batch = firebaseFirestore.batch();


            DocumentReference setsworker = firebaseFirestore.collection(getString(R.string.col_sworker))
                    .document(s_id);

            batch.set(setsworker, doc);

            if (typeselected.getEnglish().equals("guard")) {

                final Guards guards = new Guards(buildid, commid, username.getText().toString()
                        , normalfunc.getRandomNumberString5(), "", Calendar.getInstance().getTime(), normalfunc.futuredate(), "", "", "", "",
                        normalfunc.makephone14(phone.getText().toString()), s_id, ll1);
                DocumentReference setguard = firebaseFirestore.collection(getString(R.string.col_guards))
                        .document(s_id);

                batch.set(setguard, guards);

            }

            ArrayList<String> stringid = new ArrayList<>();
            ArrayList<String> stringno = new ArrayList<>();

            for (int i = 0; i < historyFlats.size(); i++) {
                stringid.add(historyFlats.get(i).getFlat_id());
                stringno.add(historyFlats.get(i).getF_no());
            }

            SLastHistory sLastHistory = new SLastHistory(s_id, buildid, stringid, stringno, Calendar.getInstance().getTime());
            DocumentReference setflat = firebaseFirestore.collection(getString(R.string.col_sworker))
                    .document(s_id).collection("shistory").document(buildid);

            batch.set(setflat, sLastHistory);


            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: yyyy");
                        //progressBar.setVisibility(View.GONE);
                        dismissdialog();
                        Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                        username.setText("");
                        phone.setText("");
                        flats.setText("");
                        type.setText("");
                        UniversalImageLoader.setImage("", userphoto, null, "");
                    }
                }
            });
        }
    }

    @Override
    public int foo() {
        return mPosition;
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.

            userphoto.setImageURI(null);

            mFileUri = r.getUri().toString();
            bitmap = r.getBitmap();
            userphoto.setImageBitmap(r.getBitmap());

            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
