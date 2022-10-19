package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehiclePucListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<VehicalPUC> = ArrayList()
    @SerializedName("vechicle_data")
    @Expose
    val vechicleData = VehicleItem()
}