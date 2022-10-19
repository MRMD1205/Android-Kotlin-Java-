package com.app.validity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClsTransportCertificateResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("vehicle_data")
    @Expose
    var vehicleData: VehicleData? = null

    @SerializedName("permit_type")
    @Expose
    var permitType: List<String>? = null

    @SerializedName("status_code")
    @Expose
    var statusCode: Int? = null

    inner class VehicleData {
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

        @SerializedName("vehicle_category_id")
        @Expose
        var vehicleCategoryId: Int? = null

        @SerializedName("register_no")
        @Expose
        var registerNo: String? = null

        @SerializedName("driver_name")
        @Expose
        var driverName: String? = null

        @SerializedName("driver_address")
        @Expose
        var driverAddress: String? = null

        @SerializedName("driver_phone_no")
        @Expose
        var driverPhoneNo: String? = null

        @SerializedName("driver_license_no")
        @Expose
        var driverLicenseNo: String? = null

        @SerializedName("driver_license_expiry_date")
        @Expose
        var driverLicenseExpiryDate: String? = null

        @SerializedName("driver_license_type")
        @Expose
        var driverLicenseType: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }

    inner class Data {
        @SerializedName("certificate_detail")
        @Expose
        var certificateDetail: CertificateDetail? = null

        @SerializedName("fitness_certificate_images")
        @Expose
        var fitnessCertificateImages: List<String>? = null

        @SerializedName("road_tax_images")
        @Expose
        var roadTaxImages: List<String>? = null

        @SerializedName("permit_images")
        @Expose
        var permitImages: List<String>? = null

        @SerializedName("speed_governer_images")
        @Expose
        var speedGovernerImages: List<String>? = null

        inner class CertificateDetail {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("transport_vehicle_id")
            @Expose
            var transportVehicleId: Int? = null

            @SerializedName("fitness_certificate_start_date")
            @Expose
            var fitnessCertificateStartDate: String? = null

            @SerializedName("fitness_certificate_end_date")
            @Expose
            var fitnessCertificateEndDate: String? = null

            @SerializedName("road_tax_start_date")
            @Expose
            var roadTaxStartDate: String? = null

            @SerializedName("road_tax_end_date")
            @Expose
            var roadTaxEndDate: String? = null

            @SerializedName("permit_type")
            @Expose
            var permitType: String? = null

            @SerializedName("permit_start_date")
            @Expose
            var permitStartDate: String? = null

            @SerializedName("permit_end_date")
            @Expose
            var permitEndDate: String? = null

            @SerializedName("speed_governer_start_date")
            @Expose
            var speedGovernerStartDate: String? = null

            @SerializedName("speed_governer_end_date")
            @Expose
            var speedGovernerEndDate: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null
        }
    }
}