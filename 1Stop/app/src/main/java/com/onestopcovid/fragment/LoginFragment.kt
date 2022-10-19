package com.onestopcovid.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.onestopcovid.R
import com.onestopcovid.activity.HomeActivity
import com.onestopcovid.activity.LoginActivity
import com.onestopcovid.base.BaseFragment
import com.onestopcovid.util.FORGOT_PASSWORD_FRAGMENT
import com.onestopcovid.util.REGISTER_FRAGMENT
import com.onestopcovid.util.Utility
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    override fun setContentView(): Int = R.layout.fragment_login

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {

    }

    override fun setListeners() {
        btnLogin.setOnClickListener {
            if (isValidate()) {
                startActivity(Intent(context, HomeActivity::class.java))
            }
        }

        btnForgotPassword.setOnClickListener {
            (activity as LoginActivity).navigateToFragment(
                ForgotPasswordFragment(),
                true,
                FORGOT_PASSWORD_FRAGMENT
            )
        }

        btnRegister.setOnClickListener {
            (activity as LoginActivity).navigateToFragment(
                RegisterFragment(),
                true,
                REGISTER_FRAGMENT
            )
        }
    }

    private fun isValidate(): Boolean {
        val userName = edtUserName.toString().trim()
        val userPassword = edtPassword.toString().trim()
        if (userName.isEmpty()) {
            Utility.showToast(activity, "Enter User Name")
            return false
        }
        if (userPassword.isEmpty()) {
            Utility.showToast(activity, "Enter Password")
            return false
        }
        return true
    }
}