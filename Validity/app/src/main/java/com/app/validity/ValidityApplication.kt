package com.app.validity

import android.app.Application
import com.app.validity.util.PREF_FCM_TOKEN
import com.crashpot.util.PreferenceData
import com.app.validity.util.Utility.Companion.showLog
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class ValidityApplication : Application() {
    var preferenceData: PreferenceData? = null
        private set

    private var gson: Gson? = null

    companion object {
        private var sInstance: ValidityApplication? = null

        val instance: ValidityApplication
            @Throws(RuntimeException::class)
            get() {
                if (sInstance == null) {
                    throw RuntimeException(
                        (sInstance as ValidityApplication)
                            .getString(R.string.no_instance_found)
                    )
                }
                return sInstance as ValidityApplication
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
        FirebaseInstanceId
            .getInstance()
            .instanceId
            .addOnSuccessListener { instanceIdResult: InstanceIdResult ->
                val newToken: String = instanceIdResult.token
                showLog("newToken", newToken)
                preferenceData?.setValue(PREF_FCM_TOKEN, newToken)
            }
    }
}