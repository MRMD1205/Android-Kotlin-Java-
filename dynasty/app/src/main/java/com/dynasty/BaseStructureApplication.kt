package com.dynasty

import android.app.Application
import android.app.Dialog
import android.view.Window
import com.dynasty.base.BaseActivity
import com.dynasty.util.PreferenceData
import com.dynasty.util.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class BaseStructureApplication : Application() {

    var preferenceData: PreferenceData? = null
        private set


    private var gson: Gson? = null

    fun getGson(): Gson {
        if (gson == null) {
            gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create()
        }
        return gson!!
    }

    companion object {
        private var sInstance: BaseStructureApplication? = null

        val instance: BaseStructureApplication
            @Throws(RuntimeException::class)
            get() {
                if (sInstance == null) {
                    throw RuntimeException(
                        (sInstance as BaseStructureApplication).getString(R.string.no_instance_found)
                    )
                }
                return sInstance as BaseStructureApplication
            }
    }

    override fun onCreate() {
        super.onCreate()

        /*val options = FirebaseOptions.Builder()
            .setApplicationId("1:30988250692:android:5cb61ec9a61303f56f9611") // Required for Analytics.
            .setApiKey("AIzaSyAfBD7nbCrGVKVeTqoiYM1Esnf7mIlfUpU")
            .setProjectId("dynasty-b596b")
            .build()
        FirebaseApp.initializeApp(this *//* Context *//*, options, "secondary")*/

        if(sInstance == null) {
            sInstance = this
            if(preferenceData == null) {
                preferenceData = PreferenceData(this)
            }
        }

        // refreshedToken()
    }

    /*
        private fun refreshedToken() {
            FirebaseInstanceId
                .getInstance()
                .instanceId
                .addOnSuccessListener { instanceIdResult: InstanceIdResult ->
                    val newToken: String = instanceIdResult.token
                    Log.e("newToken", newToken)
                    preferenceData?.setValue(PREF_FCM_TOKEN, newToken)
                }
        }
    */
}