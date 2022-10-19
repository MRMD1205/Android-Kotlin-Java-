package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VehicalExpense {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("vehicle_id")
    @Expose
    var vehicleId: Int? = null
    @SerializedName("expense_date")
    @Expose
    var expenseDate: String? = null
    @SerializedName("expense_type")
    @Expose
    var expenseType: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
    @SerializedName("km_reading")
    @Expose
    var kmReading: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}