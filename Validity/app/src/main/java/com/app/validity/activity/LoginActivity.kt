package com.app.validity.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.app.validity.R
import com.app.validity.ValidityApplication
import com.app.validity.model.User
import com.app.validity.network.ApiCallMethods
import com.app.validity.util.*
import com.crashpot.network.OnApiCallCompleted
import com.app.validity.util.Utility
import com.app.validity.util.Utility.Companion.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        txtBtnLogin.setOnClickListener(this)
        txtSignUp.setOnClickListener(this)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener(this)
        clBtnLoginWithGoogle.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.txtBtnLogin -> {
                if (checkValidation()) {
                    login(edtEmail.text.toString(), edtPassword.text.toString())
                }
            }

            R.id.txtSignUp -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            R.id.signInButton -> {
                val signInIntent = googleSignInClient.getSignInIntent()
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }

            R.id.clBtnLoginWithGoogle -> {
                val signInIntent = googleSignInClient.getSignInIntent()
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                //                edtEmail.setText(account.email)
                //                edtEmail.setSelection(edtEmail.text.toString().trim().length)
                loginWithGoogle(account.id.toString(), account.displayName.toString(), account.email.toString())
            }
            googleSignInClient.revokeAccess()
            // Signed in successfully, show authenticated UI.
            //            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            //            updateUI(null)
        }

    }

    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(edtEmail.text.toString())) {
            showToast(this, "Please enter email")
            return false

        } else if (!Utility.isEmailValid(edtEmail.text.toString())) {
            showToast(this, "Please enter valid email")
            return false

        } else if (TextUtils.isEmpty(edtPassword.text.toString())) {
            showToast(this, "Please enter password")
            return false
        } else {
            return true
        }
    }

    //API call
    private fun login(
        email: String,
        password: String
    ) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(this)
        apiCallMethods.login(email, password, object : OnApiCallCompleted<User> {
            override fun apiSuccess(obj: Any?) {
                val user: User = obj as User

                if (user.isSuccess) {
                    ValidityApplication.instance.preferenceData!!.setValueBoolean(
                        PREF_IS_LOGIN,
                        true
                    )
                    ValidityApplication.instance.preferenceData!!.setValue(
                        PREF_EMAIL, email
                    )

                    ValidityApplication.instance.preferenceData!!.setValue(
                        PREF_DEVICE_TOKEN,
                        user.token
                    )

                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    showToast(this@LoginActivity, user.message)
                }
            }

            override fun apiFailure(errorMessage: String) {
                showToast(this@LoginActivity, errorMessage)
            }

            override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
                val errorMessage: String = errorObject.getString(MESSAGE)
                showToast(this@LoginActivity, errorMessage)
            }

        })
    }

    private fun loginWithGoogle(
        google_id: String,
        name: String,
        email: String
    ) {
        val apiCallMethods: ApiCallMethods = ApiCallMethods(this)
        apiCallMethods.loginWithGoogle(google_id, name, email, object : OnApiCallCompleted<User> {
            override fun apiSuccess(obj: Any?) {
                val user: User = obj as User

                if (user.isSuccess) {
                    ValidityApplication.instance.preferenceData!!.setValueBoolean(
                        PREF_IS_LOGIN,
                        true
                    )
                    ValidityApplication.instance.preferenceData!!.setValue(
                        PREF_EMAIL, email
                    )

                    ValidityApplication.instance.preferenceData!!.setValue(
                        PREF_DEVICE_TOKEN,
                        user.token
                    )

                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    showToast(this@LoginActivity, user.message)
                }
            }

            override fun apiFailure(errorMessage: String) {
                showToast(this@LoginActivity, errorMessage)
            }

            override fun apiFailureWithCode(errorObject: JSONObject, code: Int) {
                val errorMessage: String = errorObject.getString(MESSAGE)
                showToast(this@LoginActivity, errorMessage)
            }

        })
    }
}
