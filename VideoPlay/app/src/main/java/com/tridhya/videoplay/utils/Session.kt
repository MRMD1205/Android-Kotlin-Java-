package com.tridhya.videoplay.utils

import android.content.Context
import com.tridhya.videoplay.R

class Session(val context: Context) {
    val pref =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    var isConnected: Boolean
        get() = pref.contains(KEY_IS_CONNECTED) && pref.getBoolean(KEY_IS_CONNECTED, false)
        set(isLoggedIn) = storeDataByKey(KEY_IS_CONNECTED, isLoggedIn)


    var token: String
        get() = getDataByKey(TOKEN, "")
        set(token) = storeDataByKey(TOKEN, token)

    @JvmOverloads
    fun getDataByKey(Key: String, DefaultValue: String = ""): String {
        return if (pref.contains(Key)) {
            pref.getString(Key, DefaultValue).toString()
        } else {
            DefaultValue
        }
    }

    @JvmOverloads
    fun getDataByKey(Key: String, DefaultValue: Boolean = false): Boolean {
        return if (pref.contains(Key)) {
            pref.getBoolean(Key, DefaultValue)
        } else {
            DefaultValue
        }
    }

    fun storeDataByKey(key: String, Value: String) {
        pref.edit().putString(key, Value).apply()
    }

    fun storeDataByKey(key: String, Value: Boolean) {
        pref.edit().putBoolean(key, Value).apply()
    }

    operator fun contains(key: String): Boolean {
        return pref.contains(key)
    }

    fun remove(key: String) {
        pref.edit().remove(key).apply()
    }

    fun clearData() {
        if (isConnected) {
            /*val mDBReference =
                FirebaseDatabase.getInstance().reference.child(Constants.BUILD_CONFIG)

            val params: MutableMap<String, Any> = HashMap()
            params["isOnline"] = false
            params["lastSeen"] = System.currentTimeMillis()
            mDBReference
                .child(Constants.TABLE_USERS)
                .child(userId.toString())
                .updateChildren(params)*/
        }
/*
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()*/

//        val saved = this.savedDeviceData
//
//        pref.edit().clear().apply()
//
//        this.savedDeviceData = saved
    }

    private companion object {
        private const val KEY_IS_CONNECTED = "isConnected"
        private const val KEY_DEVICE_INFO = "device"
        private const val TOKEN = "token"
        private const val KEY_SAVED_DEVICE_DATA = "savedDeviceData"
    }
}