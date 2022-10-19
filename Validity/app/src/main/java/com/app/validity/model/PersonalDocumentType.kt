package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PersonalDocumentType {
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