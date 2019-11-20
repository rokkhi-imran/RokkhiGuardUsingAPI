package com.rokkhi.callerapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.telecom.Call
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.arekolek.phone.OngoingCall
import com.github.arekolek.phone.asString
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rokkhi.rokkhiguard.ChildrenList
import com.rokkhi.rokkhiguard.Model.CallLogClass
import com.rokkhi.rokkhiguard.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_call.*
import java.util.*
import java.util.concurrent.TimeUnit


class CallActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private lateinit var number: String
    private lateinit var buildid: String
    private lateinit var thismobileuid: String
    private lateinit var startTime: Date;
    private lateinit var endTime: Date;
    private lateinit var myPhoneNumber: String;
    private var isReceived: Boolean = false;
    private var isDial: Boolean = false;

//    var isReceived = false;


    lateinit var firebaseFirestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        number = intent.data.schemeSpecificPart

        firebaseFirestore = FirebaseFirestore.getInstance();


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        buildid = sharedPref.getString("buildid", "none")
        thismobileuid = FirebaseAuth.getInstance().uid.toString()

        startTime = Calendar.getInstance().getTime()

        myPhoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()


//        commid = sharedPref.getString("commid", "none")

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

        if (state.asString().endsWith("DIALING", true)) {
            isDial = true
        }
        if (state.asString().endsWith("ACTIVE", true)) {
            isReceived = true

            startTime = Calendar.getInstance().getTime()

        }
//        callInfo.text = "${state.asString().toLowerCase().capitalize()}\n$number"
        callInfo.text = "${state.asString().toLowerCase().capitalize()}"

        answer.isVisible = state == Call.STATE_RINGING
        hangup.isVisible = state in listOf(
                Call.STATE_DIALING,
                Call.STATE_RINGING,
                Call.STATE_ACTIVE
        )
    }

    override fun onStop() {

        if (isDial) {
            var prefs = getSharedPreferences("FlatNumber", MODE_PRIVATE);
            var flatName = prefs.getString("flat", "No name defined");


            endTime = Calendar.getInstance().getTime()

            var id = firebaseFirestore.collection(getString(R.string.col_callLog)).document()

            var callLogClass = CallLogClass(id.id, buildid, thismobileuid, myPhoneNumber,
                    number, startTime, endTime, flatName, isReceived);

            firebaseFirestore.collection(getString(R.string.col_callLog)).document(id.id)
                    .set(callLogClass)
                    .addOnSuccessListener(OnSuccessListener<Void?> {

                        Toast.makeText(this, "Data Save", Toast.LENGTH_LONG).show()
                    })
                    .addOnFailureListener(OnFailureListener {
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()

                    })

        }



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
