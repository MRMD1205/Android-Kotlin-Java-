package com.tridhya.videoplay.networking.network

import android.os.StrictMode
import com.tridhya.videoplay.model.BaseModel
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.net.InetAddress

abstract class CallbackObserver<T> : DisposableObserver<T>() {

    abstract fun onSuccess(response: T)
    abstract fun onFailed(code: Int, message: String)

    override fun onComplete() {
        //Nothing happen here
    }

    override fun onNext(t: T) {
        try {
            val baseModel = t as BaseModel<Any?>
            //Status code is success
            if (baseModel.statusCode == 200) onSuccess(t)
            //status code is failed
            else onFailed(baseModel.statusCode, baseModel.message)
        } catch (e: java.lang.Exception) {
            onSuccess(t)
        }
    }

    override fun onError(e: Throwable) {
        if (!isInternetAvailable()) {
            onFailed(0, "No Internet connection")
        } else if (e is HttpException) {
            onFailed(e.code(), getErrorMessage(e.response()?.errorBody()))
        } else {
            onFailed(0, e.localizedMessage)
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val jsonObject = JSONObject(responseBody!!.string())
            jsonObject.getString("msg")
        } catch (e: Exception) {
            e.message.toString()
        }

    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val ipAddr = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }
}