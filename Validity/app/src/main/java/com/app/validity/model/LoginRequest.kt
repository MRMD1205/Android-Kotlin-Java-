package com.validity.rest.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequest {

    @SerializedName("email")
    @Expose
    var email = ""
    @SerializedName("password")
    @Expose
    var password = ""
}