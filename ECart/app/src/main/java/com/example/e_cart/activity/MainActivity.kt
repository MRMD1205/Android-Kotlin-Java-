package com.example.e_cart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_cart.R
import com.example.e_cart.fragment.WelcomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager.beginTransaction()
        val fragmentTransaction = fragmentManager.replace(R.id.containerFragment, WelcomeFragment()).commit()
    }
}