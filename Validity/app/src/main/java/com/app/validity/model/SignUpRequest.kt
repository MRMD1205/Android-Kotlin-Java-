package com.validity.rest.model.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignUpRequest {

    @SerializedName("name")
    @Expose
    var name = ""
    @SerializedName("email")
    @Expose
    var email = ""
    @SerializedName("password")
    @Expose
    var password = ""
    @SerializedName("password_confirmation")
    @Expose
    var passwordConfirm = ""
    @SerializedName("push_notification_key")
    @Expose
    var pushNotificationKey = ""
}