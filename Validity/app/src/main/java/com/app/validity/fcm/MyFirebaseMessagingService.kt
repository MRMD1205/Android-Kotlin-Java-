package com.app.validity.fcm

import com.app.validity.ValidityApplication
import com.app.validity.util.NotificationUtils
import com.app.validity.util.PREF_FCM_TOKEN
import com.app.validity.util.Utility.Companion.showLog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val TAG = "FCMService"
    }

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        showLog("newToken", refreshedToken)
        ValidityApplication.instance.preferenceData?.setValue(PREF_FCM_TOKEN, refreshedToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        showLog(TAG, "From :: " + remoteMessage.from!!)
        showLog(TAG, "Message Data Body :: " + remoteMessage.data)

        if (remoteMessage.data.isNotEmpty()) {
            NotificationUtils.instance.showNotification(
                this,
                remoteMessage.data["isBrodcast"].toString() == "2",
                remoteMessage.data["imageurl"].toString(),
                remoteMessage.data["title"].toString(),
                remoteMessage.data["message"].toString()
            )
        }

    }
}
