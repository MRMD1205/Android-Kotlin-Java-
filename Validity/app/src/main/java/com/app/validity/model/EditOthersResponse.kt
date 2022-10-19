package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EditOthersResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("other_image_data")
    @Expose
    var otherImageData: MutableList<String?>? = null

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

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("start_date")
        @Expose
        var startDate: String? = null

        @SerializedName("end_date")
        @Expose
        var endDate: String? = null

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
}