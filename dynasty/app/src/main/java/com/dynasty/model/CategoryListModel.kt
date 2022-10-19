package com.dynasty.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CategoryListModel(

        @SerializedName("CategoryId") @Expose var id: String? = null,
        @SerializedName("CategoryName") @Expose var name: String? = null,
        @SerializedName("CategoryLogo") @Expose var logo: String? = null,
        @SerializedName("IsActive") @Expose var isActive: Boolean? = null,
        @SerializedName("CreatedBy") @Expose var createdBy: String? = null,
        @SerializedName("ModifiedBy") @Expose var modifiedBy: String? = null,
        @SerializedName("CreatedDate") @Expose var createdDate: String? = null,
        @SerializedName("BusinessLink") @Expose var businessLink: String? = null,
        @SerializedName("TotalRecords") @Expose var totalRecords: String? = null,
        @SerializedName("ModifiedDate") @Expose var modifiedDate: String? = null,
        var isSelected: Boolean? = false
    ) : Serializable {
    }
