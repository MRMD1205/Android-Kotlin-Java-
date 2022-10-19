package com.example.covidThreeStep.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.covidThreeStep.R
import com.example.covidThreeStep.activity.LoginActivity
import com.example.covidThreeStep.base.BaseFragment
import com.onestopcovid.fragment.VerifyUserFragment
import com.onestopcovid.util.Utility
import com.onestopcovid.util.VERIFY_USER_FRAGMENT
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment() {

    override fun setContentView(): Int = R.layout.fragment_register

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
    }

    override fun setListeners() {
        btnRegister.setOnClickListener {
            if (isValidate()) {
                (activity as LoginActivity).navigateToFragment(VerifyUserFragment(), true, VERIFY_USER_FRAGMENT, true)
            }
        }

        ivBackArrowRegister.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun isValidate(): Boolean {
        if (TextUtils.isEmpty(edtFirstName.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_first_name))
            return false
        } else if (TextUtils.isEmpty(edtLastName.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_last_name))
            return false
        } else if (!(Utility.isEmailValid(edtEmail.text.toString().trim()))) {
            Utility.showToast(activity, getString(R.string.enter_valid_email))
            return false
        } else if (TextUtils.isEmpty(edtSuffix.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_your_suffix))
            return false
        } else if (TextUtils.isEmpty(edtAddressOne.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_your_address))
            return false
        } else if (TextUtils.isEmpty(edtCity.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_your_city))
            return false
        } else if (TextUtils.isEmpty(edtState.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_your_state))
            return false
        } else if (TextUtils.isEmpty(edtZip.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_your_zip_code))
            return false
        } else if (!Utility.isPasswordValid(edtPassword.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_valid_password))
            return false
        } else if (TextUtils.isEmpty(edtConfirmPassword.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.enter_valid_password))
            return false
        } else if (!TextUtils.equals(edtPassword.text.toString().trim(), edtConfirmPassword.text.toString().trim())) {
            Utility.showToast(activity, getString(R.string.match_password_confirm_password))
            return false
        } else {
            return true
        }
    }
}