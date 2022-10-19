package com.onestopcovid

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.onestopcovid.util.PreferenceData

class OneStopApplication : Application() {
    var preferenceData: PreferenceData? = null
        private set

    private var gson: Gson? = null

    companion object {
        private var sInstance: OneStopApplication? = null

        val instance: OneStopApplication
            @Throws(RuntimeException::class)
            get() {
                if (sInstance == null) {
                    throw RuntimeException(
                        (sInstance as OneStopApplication)
                            .getString(R.string.no_instance_found)
                    )
                }
                return sInstance as OneStopApplication
            }
    }

    fun getGson(): Gson {
        if (gson == null) {
            gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create()
        }
        return gson!!
    }

    override fun onCreate() {
        super.onCreate()
        if (sInstance == null) {
            sInstance = this
            if (preferenceData == null) {
                preferenceData = PreferenceData(this)
            }
        }

        refreshedToken()
    }

    private fun refreshedToken() {
//        FirebaseInstanceId
//            .getInstance()
//            .instanceId
//            .addOnSuccessListener { instanceIdResult: InstanceIdResult ->
//                val newToken: String = instanceIdResult.token
//                showLog("newToken", newToken)
//                preferenceData?.setValue(PREF_FCM_TOKEN, newToken)
//            }
    }
}