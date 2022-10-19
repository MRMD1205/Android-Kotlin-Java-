package com.example.twitterlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.fragment_twitter.*

class TwitterFragment : Fragment() {

    private lateinit var twitterAuthClient: TwitterAuthClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_twitter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgTwitter.setOnClickListener {
            twitterAuthClient = TwitterAuthClient()
            twitterAuthClient.authorize(
                requireActivity(),
                object : com.twitter.sdk.android.core.Callback<TwitterSession>() {
                    override fun success(result: Result<TwitterSession>?) {
                        val session = TwitterCore.getInstance().sessionManager.activeSession
                        /*   val authToken = session.authToken
                    val token = authToken.token
                    val secret = authToken.secret*/
                        login(session)
                    }

                    override fun failure(exception: TwitterException?) {
                        Toast.makeText(
                            requireActivity(),
                            "Authentication failed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
        }
    }

    private fun login(session: TwitterSession?) {
        val username = session?.userName
        val userId = session?.userId
        Log.e("Twitter UserName : ", username!!)
        Log.e("Twitter UserId : ", userId!!.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterAuthClient.onActivityResult(requestCode, resultCode, data)
    }
}
