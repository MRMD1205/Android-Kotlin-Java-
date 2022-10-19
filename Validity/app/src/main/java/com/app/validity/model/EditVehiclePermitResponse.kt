package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EditVehiclePermitResponse {
    @SerializedName("permit_image_data")
    @Expose
    var imageList: MutableList<String?>? = null
}