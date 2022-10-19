package com.example.e_cart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import com.example.e_cart.R
import com.example.e_cart.fragment.WelcomeFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var btnLogout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {

            val sp = PreferenceManager.getDefaultSharedPreferences(this@HomeActivity)
            val editor = sp.edit()
            editor.clear()
            editor.apply()

         startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }
}