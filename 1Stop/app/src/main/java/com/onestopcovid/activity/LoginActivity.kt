package com.onestopcovid.activity

import com.onestopcovid.R
import com.onestopcovid.base.BaseActivity
import com.onestopcovid.fragment.LoginFragment
import com.onestopcovid.util.LOGIN_FRAGMENT

class LoginActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_login

    override fun initViews() {
        navigateToFragment(LoginFragment(), false, LOGIN_FRAGMENT)
    }

    override fun setListeners() {
    }
}