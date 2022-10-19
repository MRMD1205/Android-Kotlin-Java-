package com.app.validity.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.validity.R
import com.app.validity.activity.DashboardActivity
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class NotificationUtils {

    private var alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    companion object {
        val instance: NotificationUtils by lazy { NotificationUtils() }
        private const val TAG = "NotificationUtils"
    }

    fun showNotification(context: Context, adminBroadcast: Boolean, imageName: String, title: String, message: String) {
        try {
            val mNotificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(context)
            val randomNotificationID = Random().nextInt(9999 - 1000) + 1000;
            mNotificationManagerCompat.notify(0, getChatNotification(context, adminBroadcast, imageName, title, message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showLocalNotification(context: Context, notificationId: Int, adminBroadcast: Boolean, imageName: String, title: String, message: String) {
        try {
            val mNotificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(context)
            val randomNotificationID = Random().nextInt(9999 - 1000) + 1000;
            mNotificationManagerCompat.notify(notificationId, getChatNotification(context, adminBroadcast, imageName, title, message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getChatNotification(
        context: Context, adminBroadcast: Boolean,
        imageName: String, firstName: String, message: String
    ): Notification {
        var bitmap: Bitmap? = null
        var name: String? = ""
        if (adminBroadcast) {
            bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
            name = context.getString(R.string.app_name)
        } else {
            if (!TextUtils.isEmpty(imageName)) {
                bitmap = getBitmapFromURL(imageName)
            } else {
                bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
            }
            name = firstName
        }

        val intent: Intent = Intent(context, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 1, intent, 0)

        val notificationChannelId: String = createChatNotificationChannel(context).toString()

        val notificationCompatBuilder = NotificationCompat.Builder(context, notificationChannelId)

        notificationCompatBuilder
            .setContentIntent(pendingIntent)
            .setContentTitle(name)
            .setContentText(message)
            .setTicker(context.getString(R.string.app_name))
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(bitmap)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setSound(alarmSound)
        val notification = notificationCompatBuilder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        return notification
    }

    private fun createChatNotificationChannel(context: Context): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId: String = context.getString(R.string.default_notification_channel_id)
            val channelName: CharSequence = context.getString(R.string.default_notification_channel_name)
            val channelDescription: String = "Messaging Notification"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            val channelEnableVibrate = true
            val channelLockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance)
            notificationChannel.description = channelDescription
            notificationChannel.enableVibration(channelEnableVibrate)
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = channelLockscreenVisibility

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            return channelId
        } else {
            return null;  // Returns null for pre-O (26) devices.
        }
    }

    private fun getBitmapFromURL(strURL: String): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}