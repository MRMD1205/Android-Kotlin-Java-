package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehicleTypeListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<VehicleType> = ArrayList()
}