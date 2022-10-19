package com.onestopcovid.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.onestopcovid.R
import com.onestopcovid.activity.LoginActivity
import com.onestopcovid.base.BaseFragment
import com.onestopcovid.util.Utility
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment() {
    override fun setContentView(): Int = R.layout.fragment_forgot_password

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
    }

    override fun setListeners() {
        btnSubmit.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            if (Utility.isEmailValid(email)) {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Utility.showToast(activity, getString(R.string.enter_valid_email))
            }
        }

        ivBackArrowForgotPassword.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}