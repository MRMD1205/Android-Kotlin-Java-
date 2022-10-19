package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPersonalDocumentListResponse : BaseResponse() {
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
        @SerializedName("personal_document_type_id")
        @Expose
        var personalDocumentTypeId: Int? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("company_name")
        @Expose
        var companyName: String? = null
        @SerializedName("sum_assured")
        @Expose
        var sumAssured: String? = null
        @SerializedName("start_date")
        @Expose
        var startDate: String? = null
        @SerializedName("end_date")
        @Expose
        var endDate: String? = null
        @SerializedName("address")
        @Expose
        var address: String? = null
        @SerializedName("description")
        @Expose
        var description: String? = null
        @SerializedName("status")
        @Expose
        var status: Int? = null
        @SerializedName("fee")
        @Expose
        var fee: Int? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
        @SerializedName("personal_document_type")
        @Expose
        var personalDocumentType: PersonalDocumentType? = null
    }
}