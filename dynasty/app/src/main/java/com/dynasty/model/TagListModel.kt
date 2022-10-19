package com.dynasty.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TagListModel(
    @SerializedName("TagId") @Expose var tagId: Int? = null,
    @SerializedName("TagName") @Expose var tagName: String? = null,
    @SerializedName("IsActive") @Expose var isActive: Boolean? = null,
    var isSelected: Boolean? = false
) : Serializable {
}
