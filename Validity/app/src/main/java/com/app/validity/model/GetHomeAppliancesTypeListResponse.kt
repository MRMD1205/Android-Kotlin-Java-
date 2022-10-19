package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetHomeAppliancesTypeListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<Datum> = ArrayList()

    class Datum {
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
        @SerializedName("home_appliance_brands")
        @Expose
        var homeApplianceBrands: List<HomeApplianceBrand>? = null

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