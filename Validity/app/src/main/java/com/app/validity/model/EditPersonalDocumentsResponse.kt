package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class EditPersonalDocumentsResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("personal_document_type_data")
    @Expose
    var personalDocumentTypeData: List<PersonalDocumentTypeDatum>? = null

    @SerializedName("personal_document_image_data")
    @Expose
    var personalDocumentImageData: MutableList<String?>? = null

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

        @SerializedName("personal_document_type_id")
        @Expose
        var personalDocumentTypeId: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

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

    class PersonalDocumentTypeDatum {
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
}