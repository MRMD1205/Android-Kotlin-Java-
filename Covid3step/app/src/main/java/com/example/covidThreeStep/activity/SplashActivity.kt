package com.example.covidThreeStep.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.covidThreeStep.R
import com.onestopcovid.CovidThreeStopApplication
import com.onestopcovid.util.PREF_IMEI
import com.onestopcovid.util.PreferenceData
import com.onestopcovid.util.SPLASH_TIME_OUT

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    @SuppressLint("HardwareIds")
    fun initViews() {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        CovidThreeStopApplication.instance.preferenceData?.setValue(PREF_IMEI, androidId)
        navigateActivity()
    }

    private fun navigateActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (PreferenceData.isUserLoggedIn()) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            }
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, SPLASH_TIME_OUT)
    }
}