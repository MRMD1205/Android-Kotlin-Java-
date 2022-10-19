package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VehicalServiceItem {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("vehicle_id")
    @Expose
    var vehicleId: Int? = null
    @SerializedName("service_type")
    @Expose
    var serviceType: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("garage")
    @Expose
    var garage: String? = null
    @SerializedName("contact_no")
    @Expose
    var contactNo: String? = null
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