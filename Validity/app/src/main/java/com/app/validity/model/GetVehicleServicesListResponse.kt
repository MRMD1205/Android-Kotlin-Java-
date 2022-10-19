package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehicleServicesListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<VehicalServiceItem> = ArrayList()
    @SerializedName("service_type")
    @Expose
    val listServiceType: MutableList<String> = ArrayList()
    @SerializedName("vechicle_data")
    @Expose
    val vechicle_data = VehicleItem()
}