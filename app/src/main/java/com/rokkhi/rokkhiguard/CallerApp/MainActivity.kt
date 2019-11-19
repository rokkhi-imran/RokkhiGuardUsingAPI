package com.rokkhi.rokkhiguard.CallerApp

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import com.rokkhi.rokkhiguard.R
import kotlinx.android.synthetic.main.activity_main_call.*

class MainActivity : AppCompatActivity() {
    lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_call)
//
//        phoneNumberInput.setText(intent?.data?.schemeSpecificPart)
        var intent = getIntent();
        number = intent.getStringExtra("phoneNumber")

        makeCall()
    }

    private fun makeCall() {
        if (PermissionChecker.checkSelfPermission(
                        this,
                        Manifest.permission.CALL_PHONE
                ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            val uri = "tel:${number}".toUri()
//            val uri = "tel:01687870058".toUri()
            startActivity(Intent(Intent.ACTION_CALL, uri))
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    9
            )
        }
    }


    override fun onStart() {
        super.onStart()
        offerReplacingDefaultDialer()

        phoneNumberInput.setOnEditorActionListener { _, _, _ ->
            makeCall()
            true
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 9 && PermissionChecker.PERMISSION_GRANTED in grantResults) {
            makeCall()
        }
    }

    private fun offerReplacingDefaultDialer() {

        if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                .let(::startActivity)
        }
    }

    companion object {
        const val REQUEST_PERMISSION = 0
    }

    fun makeCall(view: View) {

        makeCall()
    }
}
