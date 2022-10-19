package com.example.covidThreeStep.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.covidThreeStep.R
import com.example.covidThreeStep.activity.HomeActivity
import com.example.covidThreeStep.activity.LoginActivity
import com.example.covidThreeStep.base.BaseFragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.onestopcovid.CovidThreeStopApplication
import com.onestopcovid.fragment.ForgotPasswordFragment
import com.onestopcovid.util.FORGOT_PASSWORD_FRAGMENT
import com.onestopcovid.util.GOOGLE_SIGN_IN_CODE
import com.onestopcovid.util.PREF_IS_USER_LOGGED_IN
import com.onestopcovid.util.REGISTER_FRAGMENT
import com.onestopcovid.util.Utility.Companion.showLog
import com.onestopcovid.util.Utility.Companion.showToast
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONException

class LoginFragment : BaseFragment() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var twitterAuthClient: TwitterAuthClient

    override fun setContentView(): Int {
        return R.layout.fragment_login
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        callbackManager = CallbackManager.Factory.create()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun setListeners() {
        btnLogin.setOnClickListener {
            if (isValidate()) {
                navigateToHomeScreen()
            }
        }

        btnRegister.setOnClickListener {
            (requireActivity() as LoginActivity).navigateToFragment(RegisterFragment(), true, REGISTER_FRAGMENT, true)
        }

        btnForgotPassword.setOnClickListener {
            (requireActivity() as LoginActivity).navigateToFragment(ForgotPasswordFragment(), true, FORGOT_PASSWORD_FRAGMENT, true)
        }

        imgGoogle.setOnClickListener {
            loginWithGmailButtonClick()
        }

        imgFacebook.setOnClickListener {
            loginWithFacebookButtonClick()
        }

        imgTwitter.setOnClickListener {
            twitterAuthClient = TwitterAuthClient()
            twitterAuthClient.authorize(requireActivity(), object : com.twitter.sdk.android.core.Callback<TwitterSession>() {
                override fun success(result: Result<TwitterSession>?) {
                    val session = TwitterCore.getInstance().sessionManager.activeSession
                    /*   val authToken = session.authToken
                    val token = authToken.token
                    val secret = authToken.secret*/
                    login(session)
                }

                override fun failure(exception: TwitterException?) {
                    showToast(activity, "Authentication failed!")
                }

            })
        }
    }

    private fun navigateToHomeScreen() {
        CovidThreeStopApplication.instance.preferenceData?.setValueBoolean(PREF_IS_USER_LOGGED_IN, true)
        val intent = Intent(activity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        requireActivity().finish()
    }

    private fun login(session: TwitterSession?) {
        val username = session?.userName
        val userId = session?.userId
        showLog("Twitter UserName : ", username!!)
        showLog("Twitter UserId : ", userId!!.toString())
        twitterAuthClient.requestEmail(session, object : Callback<String>() {
            override fun success(result: Result<String>?) {
                showLog("Twitter UserEmail : ", result!!.data)
            }

            override fun failure(exception: TwitterException?) {
                showLog("Twitter UserEmail : ", exception.toString())
            }

        })
        navigateToHomeScreen()
    }

    private fun loginWithFacebookButtonClick() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        loginWithFacebook()
    }

    private fun loginWithGmailButtonClick() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE)
    }

    private fun isValidate(): Boolean {
        if (TextUtils.isEmpty(edtUserName.text.toString())) {
            showToast(activity, getString(R.string.enter_user_name))
            return false
        } else if (TextUtils.isEmpty(edtPassword.text.toString().trim())) {
            showToast(activity, getString(R.string.enter_valid_password))
            return false
        } else {
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            twitterAuthClient.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val loggedIn = AccessToken.getCurrentAccessToken() == null
                showLog("Facebook", "onSuccess $loggedIn")
                navigateToHomeScreen()
                getUserProfileFacebook(AccessToken.getCurrentAccessToken())
            }

            override fun onCancel() {
                showLog("Facebook", "onCancel")
            }

            override fun onError(error: FacebookException?) {
                showLog("Facebook", "onError $error")
            }
        })
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            navigateToHomeScreen()
            getUserProfileGoogle(acct)
        } catch (e: ApiException) {
            showLog("Google Sign In Error", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun getUserProfileFacebook(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(currentAccessToken) { `object`, response ->
            Log.d("TAG", `object`.toString())

            if (`object` != null) {
                try {
                    val first_name = `object`.getString("first_name")
                    val last_name = `object`.getString("last_name")
                    val email = `object`.getString("email")
                    val id = `object`.getString("id")
                    val image_url = "https://graph.facebook.com/$id/picture?type=normal"
                    showLog("Facebook", "First Name: $first_name")
                    showLog("Facebook", "Last Name: $last_name")
                    showLog("Facebook", "email: $email")
                    showLog("Facebook", "id : $id")
                    showLog("Facebook", "image_url : $image_url")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                showToast(activity, "FB Login Error...")
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun getUserProfileGoogle(acct: GoogleSignInAccount?) {
        if (acct != null) {
            val personName: String? = acct.displayName
            val personGivenName: String? = acct.givenName
            val personFamilyName: String? = acct.familyName
            val personEmail: String? = acct.email
            val personId: String? = acct.id
            val personPhoto: Uri? = acct.photoUrl
            showLog("google", "Name: $personName")
            showLog("google", "Email: $personEmail")
            showLog("google", "ID: $personId")
            showLog("google", "URL : $personPhoto")
            showLog("google", "personGivenName : $personGivenName")
            showLog("google", "personFamilyName : $personFamilyName")
        } else {
            showToast(activity, "Gmail Login Error...")
        }
    }
}