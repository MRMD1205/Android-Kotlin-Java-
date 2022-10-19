package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetIndustrialListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<Datum> = ArrayList()

    class Datum {
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