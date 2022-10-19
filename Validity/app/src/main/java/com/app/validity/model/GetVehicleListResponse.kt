package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehicleListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<VehicleItem> = ArrayList()
    @SerializedName("fuel_type")
    @Expose
    val listFuelType: MutableList<String> = ArrayList()
    @SerializedName("vehicle_condition")
    @Expose
    val listVehicleCondition: MutableList<String> = ArrayList()
}