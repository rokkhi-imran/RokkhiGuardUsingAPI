package com.rokkhi.rokkhiguard.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.rokkhi.rokkhiguard.Model.api.GetInsideVisitorData;
import com.rokkhi.rokkhiguard.Model.api.VisitorOutModelClass;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.Utils.FullScreenAlertDialog;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class VisitorWaitingListAdapter extends RecyclerView.Adapter<VisitorWaitingListAdapter.VisitorViewHolder> {



    private LayoutInflater mInflater;

    public ArrayList<GetInsideVisitorData> list;
    private static final String TAG = "VisitorAdapter";
    SharedPreferences sharedPref;

    private Context context;

    public VisitorWaitingListAdapter(ArrayList<GetInsideVisitorData> list, Context context) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_visitor_waiting, parent, false);
        VisitorViewHolder visitorViewHolder = new VisitorViewHolder(view);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return visitorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VisitorViewHolder holder, int position) {

        final GetInsideVisitorData visitor = list.get(position);
        holder.nameVisitorWaitingID.setText(visitor.getName());

        if (!visitor.getImage().isEmpty()){

            Picasso.get().load(visitor.getImage()).placeholder( R.drawable.progress_animation ).error(R.drawable.male1).into(holder.visitorImageID);
        }


    }

    private void callVisitorOutFunction(Context context, int id) {


        FullScreenAlertDialog fullScreenAlertDialog = new FullScreenAlertDialog(context);

        Map<String, String> dataPost = new HashMap<>();
        dataPost.put("visitorId", String.valueOf(id));

        JSONObject jsonDataPost = new JSONObject(dataPost);
        String url = StaticData.baseURL + "" + StaticData.letTheVisitorOut;
        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(context);
        String token = sharedPrefHelper.getString(StaticData.KEY_FIREBASE_ID_TOKEN);


        AndroidNetworking.post(url)
                .addHeaders("authtoken", token)
                .setContentType("application/json")
                .addJSONObjectBody(jsonDataPost)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fullScreenAlertDialog.dismissdialog();


                        Log.e(TAG, "onResponse: =  =----------- " + response);

                        Gson gson = new Gson();
                        VisitorOutModelClass visitorOutModelClass = gson.fromJson(String.valueOf(response), VisitorOutModelClass.class);
                        StaticData.showSuccessDialog((FragmentActivity) context, "OUT Alert !", "Successfully out");

                    }

                    @Override
                    public void onError(ANError anError) {

                        fullScreenAlertDialog.dismissdialog();

                        StaticData.showErrorAlertDialog(context, "Alert !", "আবার চেষ্টা করুন ।");

                        Log.e(TAG, "onResponse: error message =  " + anError.getMessage());
                        Log.e(TAG, "onResponse: error code =  " + anError.getErrorCode());
                        Log.e(TAG, "onResponse: error body =  " + anError.getErrorBody());
                        Log.e(TAG, "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
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

    public class VisitorViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView nameVisitorWaitingID;
        CircleImageView visitorImageID;

        VisitorViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            nameVisitorWaitingID = view.findViewById(R.id.nameVisitorWaitingID);
            visitorImageID = view.findViewById(R.id.visitorImageID);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWaitingAlertDialog(getAdapterPosition(),list,context);
                }
            });

        }
    }

    private void showWaitingAlertDialog(int adapterPosition, ArrayList<GetInsideVisitorData> list, Context context) {

        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();

    }


}
