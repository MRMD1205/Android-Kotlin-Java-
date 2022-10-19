package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehicleReFuelListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<VehicalReFuel> = ArrayList()
    @SerializedName("fuel_type")
    @Expose
    val listFuelType: MutableList<String> = ArrayList()
    @SerializedName("vechicle_data")
    @Expose
    val vechicleData = VehicleItem()
}