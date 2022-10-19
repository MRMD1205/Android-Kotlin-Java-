package com.onestopcovid.fragment

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.onestopcovid.R
import com.onestopcovid.activity.HomeActivity
import com.onestopcovid.base.BaseFragment
import com.onestopcovid.util.Utility
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {
    override fun setContentView(): Int = R.layout.fragment_profile

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
    }

    override fun setListeners() {
        btnSaveProfile.setOnClickListener {
            if (isValidate()) {
                navigateToHomeScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setHeader(R.string.profile, true, false, false, false)
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
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