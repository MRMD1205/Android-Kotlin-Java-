package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehicleInsuranceListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<VehicalInsurance> = ArrayList()
    @SerializedName("policy_type")
    @Expose
    val listPolicyType: MutableList<String> = ArrayList()
    @SerializedName("payment_type")
    @Expose
    val listPaymentType: MutableList<String> = ArrayList()
    @SerializedName("vechicle_data")
    @Expose
    val vechicleData = VehicleItem()
}