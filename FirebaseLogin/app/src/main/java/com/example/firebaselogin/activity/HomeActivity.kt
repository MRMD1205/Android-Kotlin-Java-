package com.example.firebaselogin.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaselogin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var btnSignOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnSignOut = findViewById(R.id.btnSignOut)

        btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val sp = PreferenceManager.getDefaultSharedPreferences(this@HomeActivity)
            val editor = sp.edit()
            editor.clear()
            editor.apply()

            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}