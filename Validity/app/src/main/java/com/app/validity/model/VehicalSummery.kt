package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VehicalSummery {
    @SerializedName("purchase_price")
    @Expose
    var purchasePrice: Int? = null
    @SerializedName("fuel_sum")
    @Expose
    var fuelSum: Int? = null
    @SerializedName("service_sum")
    @Expose
    var serviceSum: Int? = null
    @SerializedName("expense_sum")
    @Expose
    var expenseSum: Int? = null
    @SerializedName("insurance_sum")
    @Expose
    var insuranceSum: Int? = null
    @SerializedName("permit_sum")
    @Expose
    var permitSum: Int? = null
    @SerializedName("puc_sum")
    @Expose
    var pucSum: Int? = null
    @SerializedName("accident_sum")
    @Expose
    var accidentSum: Int? = null
}