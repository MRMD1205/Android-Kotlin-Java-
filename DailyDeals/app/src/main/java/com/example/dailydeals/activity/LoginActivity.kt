package com.example.dailydeals.activity

import android.os.Bundle
import com.example.dailydeals.R
import com.example.dailydeals.base.BaseActivity
import com.example.dailydeals.fragment.LoginFragment
import com.example.dailydeals.fragment.RegisterFragment
import com.example.dailydeals.utility.LOGIN_FRAGMENT

class LoginActivity : BaseActivity() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initViews() {

        navigateToFragment(LoginFragment(), false, LOGIN_FRAGMENT)

    }

    override fun setListeners() {
    }

   /* override fun onBackPressed() {

        if(supportFragmentManager.backStackEntryCount > 1){
            clearBackStack()
            popScreen()
        }
        else{
            super.onBackPressed()
        }

    }*/
}