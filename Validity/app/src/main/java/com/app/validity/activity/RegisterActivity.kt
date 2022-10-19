package com.app.validity.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.app.validity.R
import com.app.validity.ValidityApplication
import com.app.validity.model.User
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.MESSAGE
import com.app.validity.util.PREF_DEVICE_TOKEN
import com.app.validity.util.PREF_EMAIL
import com.app.validity.util.PREF_IS_LOGIN
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.app.validity.util.Utility.Companion.getFcmToken
import com.validity.rest.model.register.SignUpRequest
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_register)

        txtBtnRegister.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.txtBtnRegister -> {
                if (checkValidation()) {
                    val signUpRequest: SignUpRequest = SignUpRequest()
                    signUpRequest.name = edtFullName.text.toString()
                    signUpRequest.email = edtEmail.text.toString()
                    signUpRequest.password = edtPassword.text.toString()
                    signUpRequest.passwordConfirm = edtConfirmPassword.text.toString()
                    signUpRequest.pushNotificationKey = getFcmToken()
                    register(signUpRequest)
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(edtFullName.text.toString())) {
            Utility.showToast(this, "Please enter full name")
            return false

        } else if (TextUtils.isEmpty(edtEmail.text.toString())) {
            Utility.showToast(this, "Please enter email")
            return false

        } else if (!Utility.isEmailValid(edtEmail.text.toString())) {
            Utility.showToast(this, "Please enter valid email")
            return false

        } else if (TextUtils.isEmpty(edtPassword.text.toString())) {
            Utility.showToast(this, "Please enter password")
            return false
        } else if (TextUtils.isEmpty(edtConfirmPassword.text.toString())) {
            Utility.showToast(this, "Please enter Confirm Password")
            return false
        } else if (edtConfirmPassword.text.toString() != edtPassword.text.toString()) {
            Utility.showToast(this, "Password and Confirm Password not match")
            return false
        } else {
            return true
        }
    }

    //API call
    private fun register(
        signUpRequest: SignUpRequest
    ) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(this)
        apiCallMethods.register(signUpRequest, object : OnApiCallCompleted<User> {
            override fun apiSuccess(obj: Any?) {
                val user: User = obj as User
                if (user.isSuccess) {

                    ValidityApplication.instance.preferenceData!!.setValueBoolean(
                        PREF_IS_LOGIN,
                        true
                    )
                    ValidityApplication.instance.preferenceData!!.setValue(
                        PREF_EMAIL, signUpRequest.email
                    )

                    ValidityApplication.instance.preferenceData!!.setValue(
                        PREF_DEVICE_TOKEN,
                        user.token
                    )

                    startActivity(Intent(this@RegisterActivity, DashboardActivity::class.java))
                    finishAffinity()
                } else {
                    Utility.showToast(this@RegisterActivity, user.message)
                }
            }

            override fun apiFailure(errorMessage: String) {
                Utility.showToast(this@RegisterActivity, errorMessage)
            }

            override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
                val errorMessage: String = errorObject.getString(MESSAGE)
                Utility.showToast(this@RegisterActivity, errorMessage)
            }
        })
    }
}
