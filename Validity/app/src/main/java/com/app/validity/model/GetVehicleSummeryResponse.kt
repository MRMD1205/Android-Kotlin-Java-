package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehicleSummeryResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val summeryData: VehicalSummery = VehicalSummery()
}