package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetVehicleExpensesListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<VehicalExpense> = ArrayList()
    @SerializedName("expense_type")
    @Expose
    val listExpenseType: MutableList<String> = ArrayList()
    @SerializedName("vechicle_data")
    @Expose
    val vechicleData = VehicleItem()
}