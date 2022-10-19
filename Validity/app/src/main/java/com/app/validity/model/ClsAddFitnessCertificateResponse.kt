package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClsAddFitnessCertificateResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status_code")
    @Expose
    var statusCode: Int? = null
}