package com.rokkhi.rokkhiguard.CallerApp.services

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import com.rokkhi.rokkhiguard.CallerApp.activities.CallActivity
import com.simplemobiletools.dialer.helpers.CallManager

class CallService : InCallService() {
    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        val intent = Intent(this, CallActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        CallManager.call = call
        CallManager.inCallService = this
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        CallManager.call = null
        CallManager.inCallService = null
    }
}
