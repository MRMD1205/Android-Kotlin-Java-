package com.example.twitterlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authConfig = TwitterAuthConfig(resources.getString(R.string.twitter_consumer_key), resources.getString(R.string.twitter_consumer_secret))
        val config = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(authConfig)
                .debug(true)
                .build()
        Twitter.initialize(config)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, TwitterFragment())
            .addToBackStack(LOGIN_FRAGMENT)
            .commit()
    }

    private fun login(session: TwitterSession?) {
        val username = session?.userName
        val userId = session?.userId
        Log.e("Twitter UserName : ", username!!)
        Log.e("Twitter UserId : ", userId!!.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       //val fragment = supportFragmentManager.findFragmentByTag(LOGIN_FRAGMENT)
        /* if (fragment is TwitterFragment && requestCode == Twitter_SIGN_IN_CODE) {*/
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}