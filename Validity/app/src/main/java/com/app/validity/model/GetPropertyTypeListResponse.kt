package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPropertyTypeListResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    val list: MutableList<PropertyType> = ArrayList()
}