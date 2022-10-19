package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VehicalAccident {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("vehicle_id")
    @Expose
    var vehicleId: Int? = null
    @SerializedName("accident_date")
    @Expose
    var accidentDate: String? = null
    @SerializedName("accident_time")
    @Expose
    var accidentTime: String? = null
    @SerializedName("driver_name")
    @Expose
    var driverName: String? = null
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