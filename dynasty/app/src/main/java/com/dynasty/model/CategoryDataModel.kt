package com.dynasty.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryDataModel(
    @SerializedName("categoryId")
    @Expose
    var categoryId: String? = null,

    @SerializedName("categoryName")
    @Expose
    var categoryName: String? = null,

    @SerializedName("categoryImage")
    @Expose
    var categoryImage: String? = null

) : Serializable {
}