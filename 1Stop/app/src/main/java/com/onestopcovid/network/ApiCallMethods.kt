package com.app.validity.network

import android.content.Context
import android.net.Uri
import com.onestopcovid.R
import com.onestopcovid.network.OnApiCallCompleted
import com.onestopcovid.util.RESPONSE_CODE_SUCCESS_200
import com.onestopcovid.util.RESPONSE_CODE_SUCCESS_201
import com.onestopcovid.util.Utility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException

class ApiCallMethods(private var mContext: Context) {

    private fun createMultipartBodyRequest(path: Uri?): MultipartBody.Part? {
        var body: MultipartBody.Part? = null
        var outFile: File? = null
        if (mContext.contentResolver != null) {
            outFile = Utility.copyUriToFile(mContext, path!!)
        }
        if (outFile != null && outFile.exists()) {
            val reqFile = outFile.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("filename[]", outFile.name, reqFile)
        }
        return body
    }

    private fun checkResponseCodes(
        response: Response<*>,
        onApiCallCompleted: OnApiCallCompleted<*>
    ): Boolean {
        try {
            return if (response.code() == RESPONSE_CODE_SUCCESS_200 ||
                response.code() == RESPONSE_CODE_SUCCESS_201
            ) {
                if (response.body() != null) {
                    true
                } else {
                    onApiCallCompleted.apiFailure(
                        mContext.getString(
                            R.string.msg_server_error
                        )
                    )
                    false
                }
            } else {
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    val jsonObject = JSONObject(errorBody.string())
                    onApiCallCompleted.apiFailureWithCode(jsonObject, response.code())
                } else {
                    onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_server_error))
                }
                false
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_server_error))
        } catch (e: IOException) {
            e.printStackTrace()
            onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_server_error))
        }
        return false
    }

    // API Call
}