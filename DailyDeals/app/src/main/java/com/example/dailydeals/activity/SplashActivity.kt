package com.example.dailydeals.activity

import android.content.Intent
import android.os.Handler
import android.preference.PreferenceManager
import com.example.dailydeals.R
import com.example.dailydeals.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun initViews() {
        Handler().postDelayed(Runnable {
            val sp = PreferenceManager.getDefaultSharedPreferences(this@SplashActivity)
            val check = sp.getBoolean("booleanIsChecked", false)
            val onBoardingComplete = sp.getBoolean("onBoardingComplete", false)
            if (!onBoardingComplete){
                val i = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                startActivity(i)
                finish()
            }
            else if (check) {
                val i = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(i)
                finish()
            }
            else {
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 2000)
    }

    override fun setListeners() {
    }
}