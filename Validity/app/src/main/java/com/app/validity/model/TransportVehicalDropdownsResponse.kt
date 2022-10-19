package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransportVehicalDropdownsResponse {
    @SerializedName("status") @Expose var status: Boolean? = null

    @SerializedName("type") @Expose var type: MutableList<Type>? = null

    @SerializedName("brand") @Expose var brand: MutableList<Brand>? = null

    @SerializedName("category") @Expose var category: MutableList<Category>? = null

    @SerializedName("status_code") @Expose var statusCode: Int? = null

    inner class Type {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }

    inner class Category {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }

    inner class Brand {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }
}