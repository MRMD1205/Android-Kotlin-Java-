package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPropertyListResponse : BaseResponse() {
    @SerializedName("data") @Expose val list: MutableList<Data> = ArrayList()
    @SerializedName("ownership_status") @Expose val listOwnershipStatus: MutableList<String> = ArrayList()

    class Data {
        @SerializedName("id") @Expose var id: String? = null
        @SerializedName("location") @Expose var location: String? = null
        @SerializedName("rented_property_address") @Expose var rentedPropertyAddress: String? = null
        @SerializedName("user_id") @Expose var userId: String? = null
        @SerializedName("property_type_id") @Expose var propertyTypeId: String? = null
        @SerializedName("ownership_status") @Expose var ownershipStatus: String? = null
        @SerializedName("city_name") @Expose var cityName: String? = null
        @SerializedName("property_address") @Expose var propertyAddress: String? = null
        @SerializedName("purchase_date") @Expose var purchaseDate: String? = null
        @SerializedName("tenant_name") @Expose var tenantName: String? = null
        @SerializedName("tenant_permenent_address") @Expose var tenantPermenentAddress: String? = null
        @SerializedName("tenant_number") @Expose var tenantNumber: String? = null
        @SerializedName("agreement_start_date") @Expose var agreementStartDate: String? = null
        @SerializedName("agreement_end_date") @Expose var agreementEndDate: String? = null
        @SerializedName("rent_amount") @Expose var rentAmount: String? = null
        @SerializedName("rent_collection_date") @Expose var rentCollectionDate: String? = null
        @SerializedName("status") @Expose var status: Int? = null
        @SerializedName("created_at") @Expose var createdAt: String? = null
        @SerializedName("updated_at") @Expose var updatedAt: String? = null
        @SerializedName("property_type") @Expose var propertyType: PropertyType? = null
    }
}