package com.example.firebaselogin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaselogin.fragments.LoginFragment
import com.example.firebaselogin.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = LoginFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerFrameLayout, fragment).commit();
    }
}