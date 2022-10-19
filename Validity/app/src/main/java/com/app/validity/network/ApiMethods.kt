package com.app.validity.network

import com.app.validity.model.*
import com.validity.rest.model.register.SignUpRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiMethods {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("push_notification_key") pushNotificationKey: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<User?>

    @FormUrlEncoded
    @POST("login")
    fun loginWithGoogle(
        @Field("push_notification_key") pushNotificationKey: String?,
        @Field("google_id") google_id: String?,
        @Field("name") name: String?,
        @Field("email") email: String?
    ): Call<User?>

    @POST("register")
    fun register(@Body signUpRequest: SignUpRequest): Call<User?>

    @GET("reminders")
    fun getReminderList(): Call<MutableList<RemindersItem>?>

    @GET("vehicles")
    fun getVehicleList(): Call<GetVehicleListResponse?>

    @GET("transport-vehicles")
    fun getTransportVehicleList(): Call<GetTransportVehicleListResponse?>

    @GET("vehicles/{vehicleId}/vehicle-services")
    fun getVehicleServices(@Path("vehicleId") vehicleId: String): Call<GetVehicleServicesListResponse?>

    @GET("vehicles/{vehicleId}/vehicle-fuels")
    fun getVehicleFuels(@Path("vehicleId") vehicleId: String): Call<GetVehicleReFuelListResponse?>

    @GET("vehicles/{vehicleId}/vehicle-expenses")
    fun getVehicleExpenses(@Path("vehicleId") vehicleId: String): Call<GetVehicleExpensesListResponse?>

    @GET("vehicles/{vehicleId}/vehicle-insurances")
    fun getVehicleInsurances(@Path("vehicleId") vehicleId: String): Call<GetVehicleInsuranceListResponse?>

    @GET("vehicles/{vehicleId}/vehicle-permits")
    fun getVehiclePermits(@Path("vehicleId") vehicleId: String): Call<GetVehiclePermitListResponse?>

    @GET("vehicles/{vehicleId}/vehicle-pucs")
    fun getVehiclePucs(@Path("vehicleId") vehicleId: String): Call<GetVehiclePucListResponse?>

    @GET("vehicles/{vehicleId}/vehicle-accidents")
    fun getVehicleAccidents(@Path("vehicleId") vehicleId: String): Call<GetVehicleAccidentListResponse?>

    @GET("vehicles/{vehicleId}/summery")
    fun getVehicleSummery(@Path("vehicleId") vehicleId: String): Call<GetVehicleSummeryResponse?>

    @GET("others")
    fun getOthers(): Call<GetOthersListResponse?>

    @Multipart
    @POST("others/add")
    fun addOthers(
        @Part("name") title: RequestBody?,
        @Part("start_date") start_date: RequestBody?,
        @Part("end_date") end_date: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-pucs/add")
    fun addVehiclePUC(
        @Path("vehicleId") vehicleId: String,
        @Part("puc_number") puc_number: RequestBody,
        @Part("issue_date") issue_date: RequestBody,
        @Part("expiry_date") expiry_date: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-pucs/{toolId}/update")
    fun updateVehiclePUC(
        @Path("vehicleId") vehicleId: String,
        @Path("toolId") toolId: String,
        @Part("puc_number") puc_number: RequestBody,
        @Part("issue_date") issue_date: RequestBody,
        @Part("expiry_date") expiry_date: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @POST("others/{othersId}/update")
    fun updateOthers(
        @Path("othersId") othersId: String,
        @Query("name") title: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("description") description: String
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-accidents/add")
    fun addVehicleAccident(
        @Path("vehicleId") vehicleId: String?,
        @Part("accident_date") accident_date: RequestBody?,
        @Part("accident_time") accident_time: RequestBody?,
        @Part("driver_name") driver_name: RequestBody?,
        @Part("km_reading") km_reading: RequestBody?,
        @Part("amount") amount: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-accidents/{toolId}/update")
    fun updateVehicleAccident(
        @Path("vehicleId") vehicleId: String,
        @Path("toolId") toolId: String,
        @Part("accident_date") accident_date: RequestBody?,
        @Part("accident_time") accident_time: RequestBody?,
        @Part("driver_name") driver_name: RequestBody?,
        @Part("km_reading") km_reading: RequestBody?,
        @Part("amount") amount: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-fuels/add")
    fun addVehicleFuel(
        @Path("vehicleId") vehicleId: String,
        @Part("date") date: RequestBody?,
        @Part("fuel_type") fuel_type: RequestBody?,
        @Part("amount") amount: RequestBody?,
        @Part("fuel_price") fuel_price: RequestBody?,
        @Part("fuel_station") fuel_station: RequestBody?,
        @Part("km_reading") km_reading: RequestBody?,
        @Part("quantity") quantity: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-fuels/{toolId}/update")
    fun updateVehicleFuel(
        @Path("vehicleId") vehicleId: String,
        @Path("toolId") toolId: String,
        @Part("date") date: RequestBody?,
        @Part("fuel_type") fuel_type: RequestBody?,
        @Part("amount") amount: RequestBody?,
        @Part("fuel_price") fuel_price: RequestBody?,
        @Part("fuel_station") fuel_station: RequestBody?,
        @Part("km_reading") km_reading: RequestBody?,
        @Part("quantity") quantity: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-expenses/add")
    fun addVehicleExpense(
        @Path("vehicleId") vehicleId: String,
        @Part("expense_date") expense_date: RequestBody,
        @Part("expense_type") expense_type: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("km_reading") km_reading: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-expenses/{toolId}/update")
    fun updateVehicleExpense(
        @Path("vehicleId") vehicleId: String,
        @Path("toolId") toolId: String,
        @Part("expense_date") expense_date: RequestBody,
        @Part("expense_type") expense_type: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("km_reading") km_reading: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    //TODO Added by Jaimit 12/02/2020
    // Permits
    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-permits/add")
    fun addVehiclePermits(
        @Path("vehicleId") vehicleId: String,
        @Part("permit_type") permit_type: RequestBody,
        @Part("permit_number") permit_number: RequestBody,
        @Part("issue_date") issue_date: RequestBody,
        @Part("expiry_date") expiry_date: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-permits/{toolId}/update")
    fun updateVehiclePermits(
        @Path("vehicleId") vehicleId: String,
        @Path("toolId") toolId: String,
        @Part("permit_type") permit_type: RequestBody,
        @Part("permit_number") permit_number: RequestBody,
        @Part("issue_date") issue_date: RequestBody,
        @Part("expiry_date") expiry_date: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-services/add")
    fun addVehicleServices(
        @Path("vehicleId") vehicleId: String,
        @Query("service_type[]", encoded = true) service_type: String,
        @Part("date") date: RequestBody,
        @Part("garage") garage: RequestBody,
        @Part("contact_no") contact_no: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("km_reading") km_reading: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-services/{toolId}/update")
    fun updateVehicleServices(
        @Path("vehicleId") vehicleId: String,
        @Path("toolId") toolId: String,
        @Query("service_type[]", encoded = true) service_type: String,
        @Part("date") date: RequestBody,
        @Part("garage") garage: RequestBody,
        @Part("contact_no") contact_no: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("km_reading") km_reading: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-insurances/add")
    fun addVehicleInsurances(
        @Path("vehicleId") vehicleId: String,
        @Part("company_name") company_name: RequestBody,
        @Part("policy_type") policy_type: RequestBody,
        @Part("policy_number") policy_number: RequestBody,
        @Part("issue_date") issue_date: RequestBody,
        @Part("expiry_date") expiry_date: RequestBody,
        @Part("payment_mode") payment_mode: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("premium") premium: RequestBody,
        @Part("agent_name") agent_name: RequestBody,
        @Part("agent_contact_no") agent_contact_no: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("vehicles/{vehicleId}/vehicle-insurances/{toolId}/update")
    fun updateVehicleInsurances(
        @Path("vehicleId") vehicleId: String,
        @Path("toolId") toolId: String,
        @Part("company_name") company_name: RequestBody,
        @Part("policy_type") policy_type: RequestBody,
        @Part("policy_number") policy_number: RequestBody,
        @Part("issue_date") issue_date: RequestBody,
        @Part("expiry_date") expiry_date: RequestBody,
        @Part("payment_mode") payment_mode: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("premium") premium: RequestBody,
        @Part("agent_name") agent_name: RequestBody,
        @Part("agent_contact_no") agent_contact_no: RequestBody,
        @Part("description") description: RequestBody,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @GET("vehicles/{vehicleId}/delete")
    fun deleteVehicle(@Path("vehicleId") vehicleId: String): Call<BaseResponse?>

    @GET("transport-vehicles/{vehicleId}/delete")
    fun deleteTransportVehicle(@Path("vehicleId") vehicleId: String): Call<BaseResponse?>

    @GET("transport-vehicle-types")
    fun getTransportVehicleTypes(): Call<TransportVehicalDropdownsResponse?>

    @FormUrlEncoded
    @POST("vehicles/{vehicleId}/update")
    fun updateVehicle(
        @Path("vehicleId") vehicleId: String,
        @Field("vehicle_type_id") vehicle_type_id: String?,
        @Field("vehicle_brand_id") vehicle_brand_id: String?,
        @Field("name") name: String?,
        @Field("owner_name") owner_name: String?,
        @Field("vehicle_condition") vehicle_condition: String?,
        @Field("build_year") build_year: String?,
        @Field("registration_number") registration_number: String?,
        @Field("fuel_type") fuel_type: String?,
        @Field("tank_capicity") tank_capicity: String?,
        @Field("purchase_date") purchase_date: String?,
        @Field("purchase_price") purchase_price: String?,
        @Field("km_reading") km_reading: String?
    ): Call<BaseResponse?>

    @GET("vehicle-types")
    fun getVehicleTypeList(): Call<GetVehicleTypeListResponse?>

    //TODO DELETE TOOLS
    @GET("vehicles/{vehicleId}/vehicle-fuels/{toolId}/delete")
    fun deleteFuels(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<BaseResponse?>

    @GET("vehicles/{vehicleId}/vehicle-services/{toolId}/delete")
    fun deleteService(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<BaseResponse?>

    @GET("vehicles/{vehicleId}/vehicle-expenses/{toolId}/delete")
    fun deleteExpenses(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<BaseResponse?>

    @GET("vehicles/{vehicleId}/vehicle-insurances/{toolId}/delete")
    fun deleteInsurances(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<BaseResponse?>

    @GET("vehicles/{vehicleId}/vehicle-permits/{toolId}/delete")
    fun deletePermits(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<BaseResponse?>

    @GET("vehicles/{vehicleId}/vehicle-pucs/{toolId}/delete")
    fun deletePUC(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<BaseResponse?>

    @GET("others/{othersId}/delete")
    fun deleteOthers(@Path("othersId") othersId: String): Call<BaseResponse?>

    //todo transport
    @GET("transport-vehicles/{othersId}/certificate")
    fun getTransportCertificate(@Path("othersId") othersId: String): Call<ClsTransportCertificateResponse?>

    @Multipart
    @POST("transport-vehicles-certificate/{othersId}/fitness-certificate/add")
    fun addFitnessCertificate(
        @Path("othersId") othersId: String,
        @Part("start_date") startDate: RequestBody?,
        @Part("end_date") endDate: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<ClsAddFitnessCertificateResponse?>

    @Multipart
    @POST("transport-vehicles-certificate/{othersId}/road-tax/add")
    fun addRoadTax(
        @Path("othersId") othersId: String,
        @Part("start_date") startDate: RequestBody?,
        @Part("end_date") endDate: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<ClsAddFitnessCertificateResponse?>

    @Multipart
    @POST("transport-vehicles-certificate/{othersId}/permit/add")
    fun addPermit(
        @Path("othersId") othersId: String,
        @Part("start_date") startDate: RequestBody?,
        @Part("end_date") endDate: RequestBody?,
        @Part("permit_type") permitType: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<ClsAddFitnessCertificateResponse?>

    @Multipart
    @POST("transport-vehicles-certificate/{othersId}/speed-governer/add")
    fun addSpeedGoverner(
        @Path("othersId") othersId: String,
        @Part("start_date") startDate: RequestBody?,
        @Part("end_date") endDate: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<ClsAddFitnessCertificateResponse?>

    @GET("vehicles/{vehicleId}/vehicle-accidents/{toolId}/delete")
    fun deleteAccident(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<BaseResponse?>

    //TODO Industries
    @GET("industries")
    fun getIndustrialList(): Call<GetIndustrialListResponse?>

    @GET("industry-types")
    fun getIndustryTypesList(): Call<GetIndustryTypeListResponse?>

    @GET("industries/{id}/delete")
    fun deleteIndustries(@Path("id") id: String): Call<BaseResponse?>

    @FormUrlEncoded
    @POST("industries/{id}/update")
    fun updateIndustries(
        @Path("id") id: String,
        @Field("industry_type_id") industry_type_id: String?,
        @Field("purchase_date") purchase_date: String?,
        @Field("expiry_date") expiry_date: String?,
        @Field("amount") amount: String?,
        @Field("description") description: String?
    ): Call<BaseResponse?>

    //TODO Personal_Documents
    @GET("personal-documents")
    fun getPersonalDocumentList(): Call<GetPersonalDocumentListResponse?>

    @GET("personal-document-types")
    fun getPersonalDocumentTypesList(): Call<GetPersonalDocumentTypeListResponse?>

    @GET("personal-documents/{id}/delete")
    fun deletePersonalDocument(@Path("id") id: String): Call<BaseResponse?>

    @FormUrlEncoded
    @POST("personal-documents/{id}/update")
    fun updatePersonalDocuments(
        @Path("id") id: String,
        @Field("personal_document_type_id") personal_document_type: String?,
        @Field("name") name: String?,
        @Field("start_date") start_date: String?,
        @Field("end_date") end_date: String?,
        @Field("address") address: String?,
        @Field("description") description: String?,
        @Field("fee") fee: String?,
        @Field("company_name ") companyName: String?,
        @Field("sum_assured ") sumAssurd: String?,
        @Field("status") status: String?
    ): Call<BaseResponse?>

    //TODO Home Appliances
    @GET("home-appliances")
    fun getHomeAppliancesList(): Call<GetHomeAppliancesListResponse?>

    @GET("home-appliance-types")
    fun getHomeAppliancesTypesList(): Call<GetHomeAppliancesTypeListResponse?>

    @GET("home-appliances/{id}/delete")
    fun deleteHomeAppliances(@Path("id") id: String): Call<BaseResponse?>

    @FormUrlEncoded
    @POST("home-appliances/{id}/update")
    fun updateHomeAppliances(
        @Path("id") id: String,
        @Field("home_appliance_type_id") home_appliance_type_id: String?,
        @Field("home_appliance_brand_id") home_appliance_brand_id: String?,
        @Field("name") name: String?,
        @Field("purchase_from") purchase_from: String?,
        @Field("purchase_date") purchase_date: String?,
        @Field("warrenty_period") warrenty_period: String?,
        @Field("warrenty_expired_date") warrenty_expired_date: String?,
        @Field("description") description: String?,
        @Field("amount") amount: String?,
        @Field("status") status: String?,
        @Field("purchaser_name") purchaserName: String?,
        @Field("purchase_from_address ") purchaseAddress: String?
    ): Call<BaseResponse?>

    //ToDO Property
    @GET("properties")
    fun getPropertyList(): Call<GetPropertyListResponse?>

    @GET("property-types")
    fun getPropertyTypesList(): Call<GetPropertyTypeListResponse?>

    @GET("properties/{id}/delete")
    fun deleteProperty(@Path("id") id: String): Call<BaseResponse?>

    @Multipart
    @POST("properties/{id}/update")
    fun updateProperty(
        @Path("id") id: String,
        @Part("property_type_id") property_type_id: RequestBody?,
        @Part("ownership_status") ownership_status: RequestBody?,
        @Part("tenant_name") tenant_name: RequestBody?,
        @Part("tenant_number") tenant_number: RequestBody?,
        @Part("agreement_start_date") agreement_start_date: RequestBody?,
        @Part("agreement_end_date") agreement_end_date: RequestBody?,
        @Part("rent_amount") rent_amount: RequestBody?,
        @Part("rent_collection_date") rent_collection_date: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("city_name") city_name: RequestBody?,
        @Part("property_address") property_address: RequestBody?,
        @Part("purchase_date") purchase_date: RequestBody?,
        @Part("tenant_permenent_address") tenant_permenent_address: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("properties/add")
    fun addProperty(
        @Part("property_type_id") property_type_id: RequestBody?,
        @Part("ownership_status") ownership_status: RequestBody?,
        @Part("tenant_name") tenant_name: RequestBody?,
        @Part("tenant_number") tenant_number: RequestBody?,
        @Part("agreement_start_date") agreement_start_date: RequestBody?,
        @Part("agreement_end_date") agreement_end_date: RequestBody?,
        @Part("rent_amount") rent_amount: RequestBody?,
        @Part("rent_collection_date") rent_collection_date: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("city_name") city_name: RequestBody?,
        @Part("property_address") property_address: RequestBody?,
        @Part("purchase_date") purchase_date: RequestBody?,
        @Part("tenant_permenent_address") tenant_permenent_address: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>


    //To do Reminder
    @FormUrlEncoded
    @POST("personal-documents/{id}/reminder")
    fun storePersonalDocumentsReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("home-appliances/{id}/reminder")
    fun storeHomeAppliancesReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("vehicles/{id}/reminder")
    fun storeVehicleReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("properties/{id}/reminder")
    fun storePropertyReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("industries/{id}/reminder")
    fun storeIndustrialReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("others/{id}/reminder")
    fun storeOthersReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("vehicles/{id}/vehicle-fuels/reminder")
    fun storeVehicleRefuelReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("transport-vehicles/{id}/reminder")
    fun transportVehiclReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("vehicles/{id}/vehicle-services/reminder")
    fun storeVehicleServicesReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("vehicles/{id}/vehicle-expenses/reminder")
    fun storeVehicleExpensesReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("vehicles/{id}/vehicle-insurances/reminder")
    fun storeVehicleInsuranceReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("vehicles/{id}/vehicle-permits/reminder")
    fun storeVehiclePermitsReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("vehicles/{id}/vehicle-pucs/reminder")
    fun storeVehiclePucReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    @FormUrlEncoded
    @POST("vehicles/{id}/vehicle-accidents/reminder")
    fun storeVehicleAccidentReminder(@Path("id") id: String, @Field("reminder_date") reminder_date: String): Call<Boolean?>

    //TODO Added by Jaimit 08/03/2020
    @GET("vehicles/{id}/edit")
    fun getVehicleDetails(@Path("id") id: String): Call<EditVehicleResponse?>

    @GET("transport-vehicles/{id}/edit")
    fun getTransportVehicleDetails(@Path("id") id: String): Call<EditTransportVehicleResponse?>

    @GET("personal-documents/{id}/edit")
    fun getPersonalDocumentsDetails(@Path("id") id: String): Call<EditPersonalDocumentsResponse?>

    @GET("home-appliances/{id}/edit")
    fun getHomeAppliancesDetails(@Path("id") id: String): Call<EditHomeAppliancesResponse?>

    @GET("industries/{id}/edit")
    fun getIndustriesDetails(@Path("id") id: String): Call<EditIndustriesResponse?>

    @GET("others/{id}/edit")
    fun getOthersDetails(@Path("id") id: String): Call<EditOthersResponse?>

    @GET("properties/{id}/edit")
    fun getPropertyDetails(@Path("id") id: String): Call<EditPropertyResponse?>

    @GET("vehicles/{vehicleId}/vehicle-accidents/{toolId}/edit")
    fun getVehicleAccidentDetails(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<EditVehicleAccidentResponse?>

    @GET("vehicles/{vehicleId}/vehicle-expenses/{toolId}/edit")
    fun getVehicleExpenseDetails(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<EditVehicleExpenseResponse?>

    @GET("vehicles/{vehicleId}/vehicle-insurances/{toolId}/edit")
    fun getVehicleInsuranceDetails(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<EditVehicleInsuranceResponse?>

    @GET("vehicles/{vehicleId}/vehicle-permits/{toolId}/edit")
    fun getVehiclePermitDetails(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<EditVehiclePermitResponse?>

    @GET("vehicles/{vehicleId}/vehicle-pucs/{toolId}/edit")
    fun getVehiclePucDetails(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<EditVehiclePucResponse?>

    @GET("vehicles/{vehicleId}/vehicle-fuels/{toolId}/edit")
    fun getVehicleRefuelDetails(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<EditVehicleRefuelResponse?>

    @GET("vehicles/{vehicleId}/vehicle-services/{toolId}/edit")
    fun getVehicleServiceDetails(@Path("vehicleId") vehicleId: String, @Path("toolId") toolId: String): Call<EditVehicleServiceResponse?>

    // Multipart
    @Multipart
    @POST("vehicles/add")
    fun addVehical(
        @Part("vehicle_type_id") vehicle_type_id: RequestBody?,
        @Part("vehicle_brand_id") vehicle_brand_id: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("owner_name") owner_name: RequestBody?,
        @Part("vehicle_condition") vehicle_condition: RequestBody?,
        @Part("build_year") build_year: RequestBody?,
        @Part("registration_number") registration_number: RequestBody?,
        @Part("fuel_type") fuel_type: RequestBody?,
        @Part("tank_capicity") tank_capicity: RequestBody?,
        @Part("purchase_date") purchase_date: RequestBody?,
        @Part("purchase_price") purchase_price: RequestBody?,
        @Part("km_reading") km_reading: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("transport-vehicles/add")
    fun addTransportVehical(
        @Part("vehicle_type_id") vehicle_type_id: RequestBody?,
        @Part("vehicle_brand_id") vehicle_brand_id: RequestBody?,
        @Part("vehicle_category_id") vehicle_category_id: RequestBody?,
        @Part("register_no") register_no: RequestBody?,
        @Part("driver_name") driver_name: RequestBody?,
        @Part("driver_address") driver_address: RequestBody?,
        @Part("driver_phone_no") driver_phone_no: RequestBody?,
        @Part("driver_license_no") driver_license_no: RequestBody?,
        @Part("driver_license_expiry_date") driver_license_expiry_date: RequestBody?,
        @Part("driver_license_type") driver_license_type: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    @Multipart
    @POST("transport-vehicles/{id}/update")
    fun updateTransportVehical(
        @Path("id") id: String?,
        @Part("vehicle_type_id") vehicle_type_id: RequestBody?,
        @Part("vehicle_brand_id") vehicle_brand_id: RequestBody?,
        @Part("vehicle_category_id") vehicle_category_id: RequestBody?,
        @Part("register_no") register_no: RequestBody?,
        @Part("driver_name") driver_name: RequestBody?,
        @Part("driver_address") driver_address: RequestBody?,
        @Part("driver_phone_no") driver_phone_no: RequestBody?,
        @Part("driver_license_no") driver_license_no: RequestBody?,
        @Part("driver_license_expiry_date") driver_license_expiry_date: RequestBody?,
        @Part("driver_license_type") driver_license_type: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    //TODO Personal_Documents
    @Multipart
    @POST("personal-documents/add")
    fun addPersonalDocument(
        @Part("personal_document_type_id") personal_document_type: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("start_date") start_date: RequestBody?,
        @Part("end_date") end_date: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("fee") fee: RequestBody?,
        @Part("company_name ") CompanyName: RequestBody?,
        @Part("sum_assured ") sumAssurd: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    //TODO Industries
    @Multipart
    @POST("industries/add")
    fun addIndustries(
        @Part("industry_type_id") industry_type_id: RequestBody?,
        @Part("purchase_date") purchase_date: RequestBody?,
        @Part("expiry_date") expiry_date: RequestBody?,
        @Part("amount") amount: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>

    //TODO Home Appliances
    @Multipart
    @POST("home-appliances/add")
    fun addHomeAppliances(
        @Part("home_appliance_type_id") home_appliance_type_id: RequestBody?,
        @Part("home_appliance_brand_id") home_appliance_brand_id: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("purchase_from") purchase_from: RequestBody?,
        @Part("purchase_date") purchase_date: RequestBody?,
        @Part("warrenty_period") warrenty_period: RequestBody?,
        @Part("warrenty_expired_date") warrenty_expired_date: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("amount") amount: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part("purchaser_name") purchaserName: RequestBody?,
        @Part("purchase_from_address ") purchaseFromAddress: RequestBody?,
        @Part filePart: ArrayList<MultipartBody.Part?>?
    ): Call<BaseResponse?>
}