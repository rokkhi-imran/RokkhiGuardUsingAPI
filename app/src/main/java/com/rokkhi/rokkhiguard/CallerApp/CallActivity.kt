package com.rokkhi.callerapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.telecom.Call
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.arekolek.phone.OngoingCall
import com.github.arekolek.phone.asString
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
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
    private var flatName = "";



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        number = intent.data.schemeSpecificPart



        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        buildid = sharedPref.getString("buildid", "none")

        var prefs = getSharedPreferences("FlatNumber", MODE_PRIVATE);
        flatName = prefs.getString("flat", "No name defined");
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

            //cname.setText(myoffice.getName());
            val editor: SharedPreferences.Editor = this.getSharedPreferences("finish", Context.MODE_PRIVATE).edit()
            editor.putString("from", "0")
            editor.apply()

            finish()

           /* var prefs = getSharedPreferences("FlatNumber", MODE_PRIVATE);
            var from = prefs.getString("from", "No");

            if (from.equals("1")) {

                startActivity(Intent(this, ChildrenList::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
            if (from.equals("2")) {

                startActivity(Intent(this, AddVisitor::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
            if (from.equals("No")){
                startActivity(Intent(this, MainPage::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }

*/
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
//        callInfo.text = "${state.asString().toLowerCase().capitalize()}"
        callInfo.text = "${state.asString().toLowerCase().capitalize()} \n $flatName"

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
