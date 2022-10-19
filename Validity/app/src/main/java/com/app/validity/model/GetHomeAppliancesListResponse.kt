package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetHomeAppliancesListResponse : BaseResponse() {
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
        @SerializedName("home_appliance_type_id")
        @Expose
        var homeApplianceTypeId: Int? = null
        @SerializedName("home_appliance_brand_id")
        @Expose
        var homeApplianceBrandId: Int? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("purchase_from")
        @Expose
        var purchaseFrom: String? = null
        @SerializedName("purchaser_name")
        @Expose
        var purchaserName: String? = null
        @SerializedName("purchase_from_address")
        @Expose
        var purchaseFromAddress: String? = null
        @SerializedName("iconUrl")
        @Expose
        var iconUrl: String? = null
        @SerializedName("purchase_date")
        @Expose
        var purchaseDate: String? = null
        @SerializedName("warrenty_period")
        @Expose
        var warrentyPeriod: String? = null
        @SerializedName("warrenty_expired_date")
        @Expose
        var warrentyExpiredDate: String? = null
        @SerializedName("amount")
        @Expose
        var amount: Int? = null
        @SerializedName("description")
        @Expose
        var description: String? = null
        @SerializedName("status")
        @Expose
        var status: Int? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
        @SerializedName("home_appliance_type")
        @Expose
        var homeApplianceType: HomeApplianceType? = null
        @SerializedName("home_appliance_brand")
        @Expose
        var homeApplianceBrand: HomeApplianceBrand? = null

        class HomeApplianceType {
            @SerializedName("id")
            @Expose
            var id: Int? = null
            @SerializedName("name")
            @Expose
            var name: String? = null
            @SerializedName("status")
            @Expose
            var status: Int? = null
            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null
            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }

        class HomeApplianceBrand {
            @SerializedName("id")
            @Expose
            var id: Int? = null
            @SerializedName("home_appliance_type_id")
            @Expose
            var homeApplianceTypeId: Int? = null
            @SerializedName("name")
            @Expose
            var name: String? = null
            @SerializedName("status")
            @Expose
            var status: Int? = null
            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null
            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }
    }
}