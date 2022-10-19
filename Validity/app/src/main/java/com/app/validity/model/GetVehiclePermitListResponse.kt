package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehiclePermitListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<VehicalPermit> = ArrayList()
    @SerializedName("permit_type")
    @Expose
    val listPermitType: MutableList<String> = ArrayList()
    @SerializedName("vechicle_data")
    @Expose
    val vechicleData = VehicleItem()
}