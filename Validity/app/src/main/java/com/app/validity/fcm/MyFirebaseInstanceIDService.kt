//package com.kharabeesh.quizcash.fcm
//
//import android.annotation.SuppressLint
//import com.google.firebase.iid.FirebaseInstanceId
//import com.google.firebase.iid.FirebaseInstanceIdService
//import com.kharabeesh.quizcash.constant.AppConstants
//import com.kharabeesh.quizcash.constant.preference.PrefsUtils
//import com.kharabeesh.quizcash.http.tasks.RestTasks
//import com.kharabeesh.quizcash.utils.LogUtils
//
//class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
//
//    @SuppressLint("LongLogTag")
//    override fun onTokenRefresh() {
//        // Get updated InstanceID token.
//        val refreshedToken = FirebaseInstanceId.getInstance().token
//        Log.d(TAG, "Refreshed token: " + refreshedToken!!)
//
//        PrefsUtils.putString(AppConstants.GCM_TOKEN,refreshedToken);
//        RestTasks.instance.sendGCMTokenToServer(this, refreshedToken)
//    }
//
//    companion object {
//        private const val TAG = "MyFirebaseInstanceIDService"
//    }
//}
