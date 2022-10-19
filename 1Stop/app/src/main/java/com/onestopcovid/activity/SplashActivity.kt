package com.onestopcovid.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.onestopcovid.OneStopApplication
import com.onestopcovid.util.PREF_IMEI
import com.onestopcovid.util.PreferenceData
import com.onestopcovid.util.SPLASH_TIME_OUT

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    @SuppressLint("HardwareIds")
    private fun initView() {
        //Get Device Unique Id
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        OneStopApplication.instance.preferenceData?.setValue(PREF_IMEI, androidId)

        navigateActivity()
    }

    private fun navigateActivity() {
        Handler().postDelayed({
            if (PreferenceData.isUserLoggedIn()) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)
    }
}