package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VehicalInsurance {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("vehicle_id")
    @Expose
    var vehicleId: Int? = null
    @SerializedName("company_name")
    @Expose
    var companyName: String? = null
    @SerializedName("policy_type")
    @Expose
    var policyType: String? = null
    @SerializedName("policy_number")
    @Expose
    var policyNumber: String? = null
    @SerializedName("payment_mode")
    @Expose
    var paymentMode: String? = null
    @SerializedName("issue_date")
    @Expose
    var issueDate: String? = null
    @SerializedName("expiry_date")
    @Expose
    var expiryDate: String? = null
    @SerializedName("agent_name")
    @Expose
    var agentName: String? = null
    @SerializedName("agent_contact_no")
    @Expose
    var agentContactNo: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
    @SerializedName("premium")
    @Expose
    var premium: String? = null
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