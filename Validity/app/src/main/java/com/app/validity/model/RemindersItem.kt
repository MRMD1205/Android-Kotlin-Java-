package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RemindersItem {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
}