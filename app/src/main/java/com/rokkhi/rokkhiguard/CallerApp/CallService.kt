package com.rokkhi.callerapp

import android.telecom.Call
import android.telecom.InCallService
import com.github.arekolek.phone.OngoingCall


class CallService : InCallService() {

    override fun onCallAdded(call: Call) {
        OngoingCall.call = call
        CallActivity.start(this, call)
    }

    override fun onCallRemoved(call: Call) {
        OngoingCall.call = null
    }
}