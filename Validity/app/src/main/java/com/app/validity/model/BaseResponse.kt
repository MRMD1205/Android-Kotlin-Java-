package com.app.validity.model

import android.text.TextUtils
import com.app.validity.R
import com.app.validity.util.Utility
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponse {

    @SerializedName("status")
    @Expose
    val isSuccess: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String? = null
        get() = if (!TextUtils.isEmpty(field)) field else
            Utility.getMyString(R.string.msg_server_error)

    @SerializedName("status_code")
    @Expose
    val statusCode: Int? = 200
}
