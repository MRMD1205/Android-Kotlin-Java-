package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VehicalReFuel {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("vehicle_id")
    @Expose
    var vehicleId: Int? = null
    @SerializedName("fuel_type")
    @Expose
    var fuelType: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
    @SerializedName("fuel_price")
    @Expose
    var fuelPrice: String? = null
    @SerializedName("quantity")
    @Expose
    var quantity: String? = null
    @SerializedName("fuel_station")
    @Expose
    var fuelStation: String? = null
    @SerializedName("km_reading")
    @Expose
    var kmReading: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}