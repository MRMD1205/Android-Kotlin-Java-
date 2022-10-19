package com.dynasty.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.provider.Settings
import com.dynasty.BaseStructureApplication
import com.dynasty.R
import com.dynasty.base.BaseActivity
import com.dynasty.util.PREF_IMEI
import com.dynasty.util.SPLASH_TIME_OUT

class SplashActivity : BaseActivity() {

    private var androidId: String = ""

    override fun getLayoutResId(): Int = R.layout.activity_splash

    @SuppressLint("HardwareIds")
    override fun initViews() {
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        BaseStructureApplication.instance.preferenceData?.setValue(PREF_IMEI, androidId)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }

    override fun setListeners() {
    }
}