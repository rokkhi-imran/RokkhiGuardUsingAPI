package com.rokkhi.rokkhiguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.rokkhi.rokkhiguard.Utils.ITelephony;

import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;


public class IncomingCallReceiver extends BroadcastReceiver {






    private static final String TAG = "IncomingCallReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {



       // listener = (MyListerner )context;
        Toast.makeText(context, "dhukse" ,Toast.LENGTH_SHORT ).show();
        Log.d(TAG, "onReceive: dhukse");






        ITelephony telephonyService;
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Method m = tm.getClass().getDeclaredMethod("getITelephony");


                    m.setAccessible(true);
                    telephonyService = (ITelephony) m.invoke(tm);

                    if ((number != null)) {
                        telephonyService.endCall();
                       // Toast.makeText(context, "Ending the call from: " + number, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Ring " + number, Toast.LENGTH_SHORT).show();

            }
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){

                new CountDownTimer(300, 300) {

                    public void onTick(long millisUntilFinished) {

                    }
                    public void onFinish() {

                        final Intent intent1= new Intent(context, CallActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_MULTIPLE_TASK);

                        context.startActivity(intent1);
                    }
                }.start();

                Toast.makeText(context, "Answered " + number, Toast.LENGTH_SHORT).show();
            }
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context, "Idle "+ number, Toast.LENGTH_SHORT).show();
                Intent local = new Intent();
                local.setAction("finish");
                context.sendBroadcast(local);
               // listener.performSomething();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
