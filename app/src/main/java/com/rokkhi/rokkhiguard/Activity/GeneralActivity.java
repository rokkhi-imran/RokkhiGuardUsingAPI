package com.rokkhi.rokkhiguard.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.rokkhi.rokkhiguard.R;
import com.rokkhi.rokkhiguard.StaticData;
import com.rokkhi.rokkhiguard.helper.SharedPrefHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GeneralActivity extends AppCompatPreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(R.string.summary_choose_ringtone);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // buildingNameTV.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals("key_gallery_name")) {
                    // update the changed gallery buildingNameTV to summary filed
                    preference.setSummary(stringValue);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };
    private Context context;

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = GeneralActivity.this;

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);


            // notification preference change listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));


            Preference logout = findPreference("logout");

            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    removeDeviceToken();
                    return true;
                }
            });
        }

        private void callLogOutFunction(ProgressDialog progressDialog) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            String buildid = sharedPref.getString("buildid", "none");
            String thismobileuid = FirebaseAuth.getInstance().getUid();

            FirebaseAuth.getInstance().signOut();
            progressDialog.dismiss();
            Intent intent = new Intent(getContext(), GuardListActivity.class);
            startActivity(intent);
            getActivity().finish();
            AndroidNetworking.cancelAll();

        }

        private void removeDeviceToken() {
            SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getContext());

            Map<String, String> dataPost = new HashMap<>();
            dataPost.put("timeZone", sharedPrefHelper.getString(StaticData.TIME_ZONE));

            dataPost.put("limit", "");
            dataPost.put("pageId", "");
            dataPost.put("communityId", sharedPrefHelper.getString(StaticData.COMM_ID));
            dataPost.put("deviceToken", sharedPrefHelper.getString(StaticData.KRY_DEVICE_TOKEN));


            JSONObject jsonDataPost = new JSONObject(dataPost);

            String url = StaticData.baseURL + "" + StaticData.removeDeviceToken;

            Log.e("TAG", "onCreate: " + jsonDataPost);
            Log.e("TAG", "onCreate: " + url);
            Log.e("TAG", "onCreate: ---------------------- ");
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Action Executing.....");
            progressDialog.show();

            AndroidNetworking.post(url)
                    .addHeaders("jwtTokenHeader", sharedPrefHelper.getString(StaticData.JWT_TOKEN))
                    .setContentType("application/json")
                    .addJSONObjectBody(jsonDataPost)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            callLogOutFunction(progressDialog);
                            Log.e(TAG, "onResponse logout: "+response.toString() );

                        }

                        @Override
                        public void onError(ANError anError) {



                            Log.e("TAG", "onResponse: error message =  " + anError.getMessage());
                            Log.e("TAG", "onResponse: error code =  " + anError.getErrorCode());
                            Log.e("TAG", "onResponse: error body =  " + anError.getErrorBody());
                            Log.e("TAG", "onResponse: error  getErrorDetail =  " + anError.getErrorDetail());
                        }
                    });


        }


    }
}
