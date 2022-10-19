package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VehicalPermit {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("vehicle_id")
    @Expose
    var vehicleId: Int? = null
    @SerializedName("permit_type")
    @Expose
    var permitType: String? = null
    @SerializedName("permit_number")
    @Expose
    var permitNumber: String? = null
    @SerializedName("issue_date")
    @Expose
    var issueDate: String? = null
    @SerializedName("expiry_date")
    @Expose
    var expiryDate: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
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