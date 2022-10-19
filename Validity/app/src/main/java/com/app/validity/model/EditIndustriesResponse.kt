package com.app.validity.model

import com.app.validity.model.EditHomeAppliancesResponse.HomeApplianceTypeDatum
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class EditIndustriesResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("home_appliance_type_data")
    @Expose
    var homeApplianceTypeData: List<HomeApplianceTypeDatum>? = null

    @SerializedName("inustry_image_data")
    @Expose
    var inustryImageData: MutableList<String?>? = null

    @SerializedName("status_code")
    @Expose
    var statusCode: Int? = null

    class Data {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("industry_type_id")
        @Expose
        var industryTypeId: Int? = null

        @SerializedName("purchase_date")
        @Expose
        var purchaseDate: String? = null

        @SerializedName("expiry_date")
        @Expose
        var expiryDate: String? = null

        @SerializedName("amount")
        @Expose
        var amount: Int? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("industry_type")
        @Expose
        var industryType: IndustryType? = null
    }
}