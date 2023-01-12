package com.tridhya.sendbirddemo

import android.app.Application

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
//        SendbirdLive.init(applicationContext, applicationId = getString(R.string.send_bird_app_id))
    }
}