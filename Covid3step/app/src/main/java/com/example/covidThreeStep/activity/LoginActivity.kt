package com.example.covidThreeStep.activity

import android.content.Intent
import android.util.Log
import com.example.covidThreeStep.R
import com.example.covidThreeStep.base.BaseActivity
import com.onestopcovid.fragment.LoginFragment
import com.onestopcovid.util.LOGIN_FRAGMENT
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

class LoginActivity : BaseActivity() {

    override fun getLayoutResId(): Int {
        initializeTwitter()
        return R.layout.activity_login
    }

    private fun initializeTwitter() {
        val authConfig = TwitterAuthConfig(resources.getString(R.string.twitter_consumer_key), resources.getString(R.string.twitter_consumer_secret))
        val config = TwitterConfig.Builder(this).logger(DefaultLogger(Log.DEBUG)).twitterAuthConfig(authConfig).debug(true).build()
        Twitter.initialize(config)
    }

    override fun initViews() {
        navigateToFragment(LoginFragment(), false, LOGIN_FRAGMENT, false)
    }

    override fun setListeners() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            if (supportFragmentManager.findFragmentByTag(LOGIN_FRAGMENT) is LoginFragment)
                fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}