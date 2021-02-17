package com.rokkhi.rokkhiguard.CallerApp.activities

import android.content.Intent
import com.simplemobiletools.commons.activities.BaseSplashActivity

class SplashActivity : BaseSplashActivity() {
    override fun initActivity() {
        startActivity(Intent(this, CallerMainActivity::class.java))
        finish()
    }
}
