package com.example.e_cart.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.e_cart.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            val sp = PreferenceManager.getDefaultSharedPreferences(this@SplashActivity)
            val check = sp.getBoolean("booleanIsChecked", false)
            val admin = sp.getBoolean("AdminPanel", false)
            val user = sp.getBoolean("UserPanel", false)
            if (check) {
                if (admin) {
                    val i = Intent(this@SplashActivity, AdminPanelAddNewProductActivity::class.java)
                    startActivity(i)
                    finish()
                } else if (user) {
                    val i = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                }
            } else {
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 3000)
    }
}