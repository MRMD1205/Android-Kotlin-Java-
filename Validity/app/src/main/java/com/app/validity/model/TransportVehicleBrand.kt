package com.app.validity.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TransportVehicleBrand(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
) : Parcelable