package com.app.validity.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TransportVehicleItem(
    @SerializedName("id") @Expose var id: String? = null,
    @SerializedName("user_id") @Expose var userId: String? = null,
    @SerializedName("vehicle_type_id") @Expose var vehicleTypeId: String? = null,
    @SerializedName("vehicle_brand_id") @Expose var vehicleBrandId: String? = null,
    @SerializedName("vehicle_category_id") @Expose var vehicleCategoryId: String? = null,
    @SerializedName("register_no") @Expose var registerNo: String? = null,
    @SerializedName("driver_name") @Expose var driverName: String? = null,
    @SerializedName("driver_address") @Expose var driverAddress: String? = null,
    @SerializedName("driver_phone_no") @Expose var driverPhoneNo: String? = null,
    @SerializedName("driver_license_no") @Expose var driverLicenseNo: String? = null,
    @SerializedName("driver_license_expiry_date") @Expose var driverLicenseExpiryDate: String? = null,
    @SerializedName("driver_license_type") @Expose var driverLicenseType: String? = null,
    @SerializedName("created_at") @Expose var createdAt: String? = null,
    @SerializedName("updated_at") @Expose var updatedAt: String? = null,
    @SerializedName("vehicle_type") @Expose var transportVehicleType: TransportVehicleType? = null,
    @SerializedName("vehicle_brand") @Expose var transportVehicleBrand: TransportVehicleBrand? = null,
    @SerializedName("vehicle_category") @Expose var transportVehicleCategory: TransportVehicleCategory? = null
) : Parcelable