package com.rokkhi.callerapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.github.arekolek.phone.OngoingCall
import com.github.arekolek.phone.asString
import com.rokkhi.rokkhiguard.ChildrenList
import com.rokkhi.rokkhiguard.R
import com.rokkhi.rokkhiguard.Utils.Normalfunc
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_call.*
import java.util.concurrent.TimeUnit

class CallActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private lateinit var number: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        number = intent.data.schemeSpecificPart

    }

    override fun onStart() {
        super.onStart()

        answer.setOnClickListener {
            OngoingCall.answer()
        }

        hangup.setOnClickListener {
            OngoingCall.hangup()

            startActivity(Intent(this, ChildrenList::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))

//            finishAffinity()

//            System.exit(0)
        }

        OngoingCall.state
            .subscribe(::updateUi)
            .addTo(disposables)

        OngoingCall.state
            .filter { it == Call.STATE_DISCONNECTED }
            .delay(1, TimeUnit.SECONDS)
            .firstElement()
            .subscribe { finish() }
            .addTo(disposables)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
        callInfo.text = "${state.asString().toLowerCase().capitalize()}\n$number"

        answer.isVisible = state == Call.STATE_RINGING
        hangup.isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    companion object {
        fun start(context: Context, call: Call) {
            Intent(context, CallActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(context::startActivity)
        }
    }


}
