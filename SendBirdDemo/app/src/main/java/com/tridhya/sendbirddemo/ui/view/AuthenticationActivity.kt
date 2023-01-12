package com.tridhya.sendbirddemo.ui.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.live.uikit.SendbirdLiveUIKit
import com.sendbird.uikit.adapter.SendbirdUIKitAdapter
import com.sendbird.uikit.interfaces.UserInfo
import com.tridhya.sendbirddemo.R

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        progressDialog = ProgressDialog(this@AuthenticationActivity)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Authenticating")
        progressDialog.show()

        SendbirdLiveUIKit.init(
            object : SendbirdUIKitAdapter {
                override fun getAppId(): String = getString(R.string.send_bird_app_id)

                override fun getAccessToken(): String = getString(R.string.send_bird_access_token)

                override fun getUserInfo(): UserInfo = object : UserInfo {
                    override fun getUserId(): String = getString(R.string.send_bird_user_id)

                    override fun getNickname(): String? = null

                    override fun getProfileUrl(): String? = null
                }

                override fun getInitResultHandler(): InitResultHandler {
                    return object : InitResultHandler {
                        override fun onInitFailed(e: SendbirdException) {
                            progressDialog.hide()
                            // If DB migration fails, this method is called.
                        }

                        override fun onInitSucceed() {
                            // If DB migration is successful, this method is called and you can proceed to the next step.
                            // In the sample app, the LiveData class notifies you on the initialization progress
                            // and observes the `MutableLiveData<InitState> initState` value in `SplashActivity()`.
                            // If successful, the `LoginActivity` screen
                            // or the `HomeActivity` screen will show.

                            SendbirdLiveUIKit.connect { _, e ->
                                progressDialog.hide()
                                if (e != null) {
                                    Log.e("onInitSucceed --> ", e.toString())
                                    return@connect
                                }
                                Log.e("onInitSucceed --> ", "Success")
                                Toast.makeText(
                                    this@AuthenticationActivity,
                                    "Authentication Success",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@AuthenticationActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        override fun onMigrationStarted() {
                            progressDialog.hide()
                            // DB migration has started.
                        }
                    }
                }
            },
            this
        )

    }
}