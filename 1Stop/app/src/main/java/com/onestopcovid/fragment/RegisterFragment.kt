package com.onestopcovid.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.onestopcovid.R
import com.onestopcovid.activity.HomeActivity
import com.onestopcovid.activity.LoginActivity
import com.onestopcovid.base.BaseFragment
import com.onestopcovid.util.FORGOT_PASSWORD_FRAGMENT
import com.onestopcovid.util.Utility
import com.onestopcovid.util.VERIFY_USER_FRAGMENT
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment() {

    override fun setContentView(): Int = R.layout.fragment_register

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
    }

    override fun setListeners() {
        btnRegister.setOnClickListener {
            if (isValidate()){
                (activity as LoginActivity).navigateToFragment(
                    VerifyUserFragment(),
                    true,
                    VERIFY_USER_FRAGMENT
                )
            }
        }
    }

    private fun isValidate(): Boolean {

        if ((edtFirstName.toString()).isEmpty()){
            Utility.showToast(activity, "Enter First Name")
            return false
        }
        if ((edtMiddleName.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Middle Name")
            return false
        }
        if ((edtLastName.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Last Name")
            return false
        }
        if ((edtEmail.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Your Email")
            return false
        }
        if ((edtSuffix.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Your Suffix")
            return false
        }
        if ((edtAddressOne.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Your Address")
            return false
        }
        if ((edtCity.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Your City")
            return false
        }
        if ((edtState.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Your State")
            return false
        }
        if ((edtZip.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Zip Code")
            return false
        }
        if ((edtPassword.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Your Password")
            return false
        }
        if ((edtConfirmPassword.toString()).isEmpty()){
            Utility.showToast(activity, "Enter Confirm Password")
            return false
        }
        return true
    }
}