package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EditVehicleResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("vehicle_type_data")
    @Expose
    var vehicleTypeData: List<VehicleTypeDatum>? = null

    @SerializedName("vehicle_images")
    @Expose
    var vehicleImages: MutableList<String?>? = null

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

        @SerializedName("vehicle_type_id")
        @Expose
        var vehicleTypeId: Int? = null

        @SerializedName("vehicle_brand_id")
        @Expose
        var vehicleBrandId: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("owner_name")
        @Expose
        var ownerName: String? = null

        @SerializedName("vehicle_condition")
        @Expose
        var vehicleCondition: String? = null

        @SerializedName("build_year")
        @Expose
        var buildYear: Int? = null

        @SerializedName("registration_number")
        @Expose
        var registrationNumber: String? = null

        @SerializedName("fuel_type")
        @Expose
        var fuelType: String? = null

        @SerializedName("tank_capicity")
        @Expose
        var tankCapicity: String? = null

        @SerializedName("purchase_date")
        @Expose
        var purchaseDate: String? = null

        @SerializedName("purchase_price")
        @Expose
        var purchasePrice: Int? = null

        @SerializedName("km_reading")
        @Expose
        var kmReading: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("vehicle_type")
        @Expose
        var vehicleType: VehicleType? = null

        @SerializedName("vehicle_brand")
        @Expose
        var vehicleBrand: VehicleBrand? = null
    }

    class VehicleTypeDatum {
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

        @SerializedName("vehicle_brands")
        @Expose
        var vehicleBrands: List<VehicleBrand_>? = null

        class VehicleBrand_ {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("vehicle_type_id")
            @Expose
            var vehicleTypeId: Int? = null

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
    }
}