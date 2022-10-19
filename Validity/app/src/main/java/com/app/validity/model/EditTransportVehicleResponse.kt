package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EditTransportVehicleResponse {

    @SerializedName("status") @Expose var status: Boolean? = null
    @SerializedName("vehicle_images") @Expose var imageList: MutableList<String?>? = null
    @SerializedName("status_code") @Expose var statusCode: Int? = null
}