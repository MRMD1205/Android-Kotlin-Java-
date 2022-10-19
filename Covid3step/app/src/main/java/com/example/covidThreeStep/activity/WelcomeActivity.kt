package com.example.covidThreeStep.activity

import android.content.Intent
import com.example.covidThreeStep.R
import com.onestopcovid.base.BaseActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_welcome

    override fun initViews() {
    }

    override fun setListeners() {
        btnGetStarted.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            finish()
        }
    }
}