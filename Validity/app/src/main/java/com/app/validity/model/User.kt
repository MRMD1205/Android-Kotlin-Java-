package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User : BaseResponse() {
    @SerializedName("token")
    @Expose
    val token = ""
}