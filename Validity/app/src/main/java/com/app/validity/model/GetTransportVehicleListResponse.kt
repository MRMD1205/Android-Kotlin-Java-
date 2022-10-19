package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetTransportVehicleListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<TransportVehicleItem> = ArrayList()
    @SerializedName("license_type")
    @Expose
    val listLicenseType: MutableList<String> = ArrayList()
}