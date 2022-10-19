package com.app.validity.network

import android.content.Context
import android.net.Uri
import com.app.validity.R
import com.app.validity.model.*
import com.app.validity.util.BASE_URL
import com.app.validity.util.RESPONSE_CODE_SUCCESS_200
import com.app.validity.util.RESPONSE_CODE_SUCCESS_201
import com.crashpot.network.OnApiCallCompleted
import com.crashpot.util.ProgressBarUtils.Companion.cancelProgress
import com.crashpot.util.ProgressBarUtils.Companion.showProgress
import com.app.validity.util.Utility
import com.validity.rest.model.register.SignUpRequest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*

class ApiCallMethods(private var mContext: Context) {

    private fun createMultipartBodyRequest(path: Uri?): MultipartBody.Part? {
        var body: MultipartBody.Part? = null
        var outFile: File? = null
        if (mContext.contentResolver != null) {
            outFile = Utility.copyUriToFile(mContext, path!!)
        }
        if (outFile != null && outFile.exists()) {
            val reqFile = outFile.asRequestBody("image/*".toMediaTypeOrNull())
            body = MultipartBody.Part.createFormData("filename[]", outFile.name, reqFile)
        }
        return body
    }

    private fun checkResponseCodes(
        response: Response<*>,
        onApiCallCompleted: OnApiCallCompleted<*>
    ): Boolean {
        try {
            return if (response.code() == RESPONSE_CODE_SUCCESS_200 ||
                response.code() == RESPONSE_CODE_SUCCESS_201
            ) {
                if (response.body() != null) {
                    true
                } else {
                    onApiCallCompleted.apiFailure(
                        mContext.getString(
                            R
                                .string.msg_server_error
                        )
                    )
                    false
                }
            } else {
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    val jsonObject = JSONObject(errorBody.string())
                    onApiCallCompleted.apiFailureWithCode(jsonObject, response.code())
                } else {
                    onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_server_error))
                }
                false
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_server_error))
        } catch (e: IOException) {
            e.printStackTrace()
            onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_server_error))
        }
        return false
    }

    //API CALL
    fun login(
        email: String,
        password: String,
        onApiCallCompleted: OnApiCallCompleted<User>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<User?> =
                ApiClient.getRestApiMethods(BASE_URL).login(
                    Utility.getFcmToken(),
                    email,
                    password
                )

            call.enqueue(object : Callback<User?> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: User? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loginWithGoogle(
        google_id: String,
        name: String,
        email: String,
        onApiCallCompleted: OnApiCallCompleted<User>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<User?> =
                ApiClient.getRestApiMethods(BASE_URL).loginWithGoogle(
                    Utility.getFcmToken(),
                    google_id,
                    name,
                    email
                )

            call.enqueue(object : Callback<User?> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: User? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun register(
        signUpRequest: SignUpRequest,
        onApiCallCompleted: OnApiCallCompleted<User>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<User?> =
                ApiClient.getRestApiMethods(BASE_URL).register(
                    signUpRequest
                )

            call.enqueue(object : Callback<User?> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: User? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleList(
        onApiCallCompleted: OnApiCallCompleted<GetVehicleListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehicleListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehicleList()

            call.enqueue(object : Callback<GetVehicleListResponse?> {
                override fun onResponse(
                    call: Call<GetVehicleListResponse?>,
                    response: Response<GetVehicleListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehicleListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehicleListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTransportVehicleList(
        onApiCallCompleted: OnApiCallCompleted<GetTransportVehicleListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetTransportVehicleListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getTransportVehicleList()

            call.enqueue(object : Callback<GetTransportVehicleListResponse?> {
                override fun onResponse(
                    call: Call<GetTransportVehicleListResponse?>,
                    response: Response<GetTransportVehicleListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetTransportVehicleListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetTransportVehicleListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getReminderList(
        onApiCallCompleted: OnApiCallCompleted<MutableList<RemindersItem>>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<MutableList<RemindersItem>?> =
                ApiClient.getRestApiMethods(BASE_URL).getReminderList()

            call.enqueue(object : Callback<MutableList<RemindersItem>?> {
                override fun onResponse(
                    call: Call<MutableList<RemindersItem>?>,
                    response: Response<MutableList<RemindersItem>?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: MutableList<RemindersItem>? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<MutableList<RemindersItem>?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO Personal_Documents
    fun getPersonalDocumentList(
        onApiCallCompleted: OnApiCallCompleted<GetPersonalDocumentListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetPersonalDocumentListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getPersonalDocumentList()

            call.enqueue(object : Callback<GetPersonalDocumentListResponse?> {
                override fun onResponse(
                    call: Call<GetPersonalDocumentListResponse?>,
                    response: Response<GetPersonalDocumentListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetPersonalDocumentListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetPersonalDocumentListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPersonalDocumentTypesList(
        onApiCallCompleted: OnApiCallCompleted<GetPersonalDocumentTypeListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetPersonalDocumentTypeListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getPersonalDocumentTypesList()

            call.enqueue(object : Callback<GetPersonalDocumentTypeListResponse?> {
                override fun onResponse(
                    call: Call<GetPersonalDocumentTypeListResponse?>,
                    response: Response<GetPersonalDocumentTypeListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetPersonalDocumentTypeListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetPersonalDocumentTypeListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTransportVehicleTypes(
        onApiCallCompleted: OnApiCallCompleted<TransportVehicalDropdownsResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<TransportVehicalDropdownsResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getTransportVehicleTypes()

            call.enqueue(object : Callback<TransportVehicalDropdownsResponse?> {
                override fun onResponse(
                    call: Call<TransportVehicalDropdownsResponse?>,
                    response: Response<TransportVehicalDropdownsResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: TransportVehicalDropdownsResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<TransportVehicalDropdownsResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deletePersonalDocument(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).deletePersonalDocument(id)

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updatePersonalDocuments(
        id: String,
        personal_document_type_id: String,
        name: String,
        start_date: String,
        end_date: String,
        address: String,
        description: String,
        fee: String,
        status: String,
        company_name: String,
        sum_assurd: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL)
                .updatePersonalDocuments(
                    id,
                    personal_document_type_id,
                    name,
                    start_date,
                    end_date,
                    address,
                    description,
                    fee,
                    company_name,
                    sum_assurd,
                    status
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO Home Appliances
    fun getHomeAppliancesList(
        onApiCallCompleted: OnApiCallCompleted<GetHomeAppliancesListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetHomeAppliancesListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getHomeAppliancesList()

            call.enqueue(object : Callback<GetHomeAppliancesListResponse?> {
                override fun onResponse(
                    call: Call<GetHomeAppliancesListResponse?>,
                    response: Response<GetHomeAppliancesListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetHomeAppliancesListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetHomeAppliancesListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getHomeAppliancesTypesList(
        onApiCallCompleted: OnApiCallCompleted<GetHomeAppliancesTypeListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetHomeAppliancesTypeListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getHomeAppliancesTypesList()

            call.enqueue(object : Callback<GetHomeAppliancesTypeListResponse?> {
                override fun onResponse(
                    call: Call<GetHomeAppliancesTypeListResponse?>,
                    response: Response<GetHomeAppliancesTypeListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetHomeAppliancesTypeListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetHomeAppliancesTypeListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteHomeAppliances(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).deleteHomeAppliances(id)

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateHomeAppliances(
        id: String,
        home_appliance_type_id: String,
        home_appliance_brand_id: String,
        name: String,
        purchase_from: String,
        purchase_date: String,
        warrenty_period: String,
        warrenty_expired_date: String,
        description: String,
        amount: String,
        status: String,
        purchaseName: String,
        purchaseAddress: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL)
                    .updateHomeAppliances(
                        id,
                        home_appliance_type_id,
                        home_appliance_brand_id,
                        name,
                        purchase_from,
                        purchase_date,
                        warrenty_period,
                        warrenty_expired_date,
                        description,
                        amount,
                        status,
                        purchaseName,
                        purchaseAddress
                    )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO Industries
    fun getIndustrialList(
        onApiCallCompleted: OnApiCallCompleted<GetIndustrialListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetIndustrialListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getIndustrialList()

            call.enqueue(object : Callback<GetIndustrialListResponse?> {
                override fun onResponse(
                    call: Call<GetIndustrialListResponse?>,
                    response: Response<GetIndustrialListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetIndustrialListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetIndustrialListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getIndustryTypesList(
        onApiCallCompleted: OnApiCallCompleted<GetIndustryTypeListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetIndustryTypeListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getIndustryTypesList()

            call.enqueue(object : Callback<GetIndustryTypeListResponse?> {
                override fun onResponse(
                    call: Call<GetIndustryTypeListResponse?>,
                    response: Response<GetIndustryTypeListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetIndustryTypeListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetIndustryTypeListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteIndustries(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).deleteIndustries(id)

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateIndustries(
        id: String,
        industryTypeId: String,
        purchaseDate: String,
        expiryDate: String,
        amount: String,
        description: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL)
                    .updateIndustries(
                        id,
                        industryTypeId,
                        purchaseDate,
                        expiryDate,
                        amount,
                        description
                    )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleTypeList(
        onApiCallCompleted: OnApiCallCompleted<GetVehicleTypeListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehicleTypeListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehicleTypeList()

            call.enqueue(object : Callback<GetVehicleTypeListResponse?> {
                override fun onResponse(
                    call: Call<GetVehicleTypeListResponse?>,
                    response: Response<GetVehicleTypeListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehicleTypeListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehicleTypeListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateVehical(
        vehicle_id: String,
        vehicle_type_id: String,
        vehicle_brand_id: String,
        name: String,
        owner_name: String,
        vehicle_condition: String,
        build_year: String,
        registration_number: String,
        fuel_type: String,
        tank_capicity: String,
        purchase_date: String,
        purchase_price: String,
        km_reading: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateVehicle(
                    vehicle_id,
                    vehicle_type_id,
                    vehicle_brand_id,
                    name,
                    owner_name,
                    vehicle_condition,
                    build_year,
                    registration_number,
                    fuel_type,
                    tank_capicity,
                    purchase_date,
                    purchase_price,
                    km_reading
                )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // TODO NEW Added (Replace JSONObject)
    fun getVehicleFuels(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<GetVehicleReFuelListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehicleReFuelListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehicleFuels(vehicleId)

            call.enqueue(object : Callback<GetVehicleReFuelListResponse?> {
                override fun onResponse(
                    call: Call<GetVehicleReFuelListResponse?>,
                    response: Response<GetVehicleReFuelListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehicleReFuelListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehicleReFuelListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleServices(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<GetVehicleServicesListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehicleServicesListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehicleServices(vehicleId)

            call.enqueue(object : Callback<GetVehicleServicesListResponse?> {
                override fun onResponse(
                    call: Call<GetVehicleServicesListResponse?>,
                    response: Response<GetVehicleServicesListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehicleServicesListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehicleServicesListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleExpenses(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<GetVehicleExpensesListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehicleExpensesListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehicleExpenses(vehicleId)

            call.enqueue(object : Callback<GetVehicleExpensesListResponse?> {
                override fun onResponse(
                    call: Call<GetVehicleExpensesListResponse?>,
                    response: Response<GetVehicleExpensesListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehicleExpensesListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehicleExpensesListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleInsurances(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<GetVehicleInsuranceListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehicleInsuranceListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehicleInsurances(vehicleId)

            call.enqueue(object : Callback<GetVehicleInsuranceListResponse?> {
                override fun onResponse(
                    call: Call<GetVehicleInsuranceListResponse?>,
                    response: Response<GetVehicleInsuranceListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehicleInsuranceListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehicleInsuranceListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehiclePermits(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<GetVehiclePermitListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehiclePermitListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehiclePermits(vehicleId)

            call.enqueue(object : Callback<GetVehiclePermitListResponse?> {
                override fun onResponse(
                    call: Call<GetVehiclePermitListResponse?>,
                    response: Response<GetVehiclePermitListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehiclePermitListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehiclePermitListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehiclePucs(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<GetVehiclePucListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehiclePucListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehiclePucs(vehicleId)

            call.enqueue(object : Callback<GetVehiclePucListResponse?> {
                override fun onResponse(
                    call: Call<GetVehiclePucListResponse?>,
                    response: Response<GetVehiclePucListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehiclePucListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehiclePucListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getOthers(
        onApiCallCompleted: OnApiCallCompleted<GetOthersListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetOthersListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getOthers()

            call.enqueue(object : Callback<GetOthersListResponse?> {
                override fun onResponse(
                    call: Call<GetOthersListResponse?>,
                    response: Response<GetOthersListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetOthersListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetOthersListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleAccidents(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<GetVehicleAccidentListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehicleAccidentListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehicleAccidents(vehicleId)

            call.enqueue(object : Callback<GetVehicleAccidentListResponse?> {
                override fun onResponse(
                    call: Call<GetVehicleAccidentListResponse?>,
                    response: Response<GetVehicleAccidentListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehicleAccidentListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehicleAccidentListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleSummery(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<GetVehicleSummeryResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetVehicleSummeryResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getVehicleSummery(vehicleId)

            call.enqueue(object : Callback<GetVehicleSummeryResponse?> {
                override fun onResponse(
                    call: Call<GetVehicleSummeryResponse?>,
                    response: Response<GetVehicleSummeryResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetVehicleSummeryResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetVehicleSummeryResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addVehiclePUC(
        vehicleId: String,
        puc_number: String,
        issue_date: String,
        expiry_date: String,
        amount: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val puc_numberRequestBody: RequestBody = puc_number.toRequestBody(contentType)
            val issue_dateRequestBody: RequestBody = issue_date.toRequestBody(contentType)
            val expiry_dateRequestBody: RequestBody = expiry_date.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addVehiclePUC(
                    vehicleId,
                    puc_numberRequestBody,
                    issue_dateRequestBody,
                    expiry_dateRequestBody,
                    amountRequestBody,
                    descriptionRequestBody,
                    fileList
                )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateVehiclePUC(
        vehicleId: String,
        toolsId: String,
        puc_number: String,
        issue_date: String,
        expiry_date: String,
        amount: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val puc_numberRequestBody: RequestBody = puc_number.toRequestBody(contentType)
            val issue_dateRequestBody: RequestBody = issue_date.toRequestBody(contentType)
            val expiry_dateRequestBody: RequestBody = expiry_date.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateVehiclePUC(
                    vehicleId,
                    toolsId,
                    puc_numberRequestBody,
                    issue_dateRequestBody,
                    expiry_dateRequestBody,
                    amountRequestBody,
                    descriptionRequestBody,
                    fileList
                )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateOthers(
        othersId: String,
        title: String,
        start_date: String,
        end_date: String,
        description: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateOthers(
                    othersId,
                    title,
                    start_date,
                    end_date,
                    description
                )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addVehicleAccident(
        vehicleId: String,
        accident_date: String,
        accident_time: String,
        driver_name: String,
        km_reading: String,
        amount: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)

            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val accident_dateRequestBody: RequestBody = accident_date.toRequestBody(contentType)
            val accident_timeRequestBody: RequestBody = accident_time.toRequestBody(contentType)
            val driver_nameRequestBody: RequestBody = driver_name.toRequestBody(contentType)
            val km_readingRequestBody: RequestBody = km_reading.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addVehicleAccident(
                    vehicleId,
                    accident_dateRequestBody,
                    accident_timeRequestBody,
                    driver_nameRequestBody,
                    km_readingRequestBody,
                    amountRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateVehicleAccident(
        vehicleId: String,
        toolId: String,
        accident_date: String,
        accident_time: String,
        driver_name: String,
        km_reading: String,
        amount: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)

            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val accident_dateRequestBody: RequestBody = accident_date.toRequestBody(contentType)
            val accident_timeRequestBody: RequestBody = accident_time.toRequestBody(contentType)
            val driver_nameRequestBody: RequestBody = driver_name.toRequestBody(contentType)
            val km_readingRequestBody: RequestBody = km_reading.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateVehicleAccident(
                    vehicleId,
                    toolId,
                    accident_dateRequestBody,
                    accident_timeRequestBody,
                    driver_nameRequestBody,
                    km_readingRequestBody,
                    amountRequestBody,
                    descriptionRequestBody,
                    fileList
                )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addVehicleFuel(
        vehicleId: String,
        date: String,
        fuel_type: String,
        amount: String,
        fuel_price: String,
        fuel_station: String,
        km_reading: String,
        quantity: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val vehicleIdBody: RequestBody = vehicleId.toRequestBody()
            val dateRequestBody: RequestBody = date.toRequestBody(contentType)
            val fuel_typeRequestBody: RequestBody = fuel_type.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val fuel_priceRequestBody: RequestBody = fuel_price.toRequestBody(contentType)
            val fuel_stationRequestBody: RequestBody = fuel_station.toRequestBody(contentType)
            val km_readingRequestBody: RequestBody = km_reading.toRequestBody(contentType)
            val quantityRequestBody: RequestBody = quantity.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addVehicleFuel(
                    vehicleId,
                    dateRequestBody,
                    fuel_typeRequestBody,
                    amountRequestBody,
                    fuel_priceRequestBody,
                    fuel_stationRequestBody,
                    km_readingRequestBody,
                    quantityRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateVehicleFuel(
        vehicleId: String,
        toolsId: String,
        date: String,
        fuel_type: String,
        amount: String,
        fuel_price: String,
        fuel_station: String,
        km_reading: String,
        quantity: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val dateRequestBody: RequestBody = date.toRequestBody(contentType)
            val fuel_typeRequestBody: RequestBody = fuel_type.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val fuel_priceRequestBody: RequestBody = fuel_price.toRequestBody(contentType)
            val fuel_stationRequestBody: RequestBody = fuel_station.toRequestBody(contentType)
            val km_readingRequestBody: RequestBody = km_reading.toRequestBody(contentType)
            val quantityRequestBody: RequestBody = quantity.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateVehicleFuel(
                    vehicleId,
                    toolsId,
                    dateRequestBody,
                    fuel_typeRequestBody,
                    amountRequestBody,
                    fuel_priceRequestBody,
                    fuel_stationRequestBody,
                    km_readingRequestBody,
                    quantityRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addVehicleExpense(
        vehicleId: String,
        expense_date: String,
        expense_type: String,
        amount: String,
        km_reading: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val expense_dateRequestBody: RequestBody = expense_date.toRequestBody(contentType)
            val expense_typeRequestBody: RequestBody = expense_type.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val km_readingRequestBody: RequestBody = km_reading.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addVehicleExpense(
                    vehicleId,
                    expense_dateRequestBody,
                    expense_typeRequestBody,
                    amountRequestBody,
                    km_readingRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateVehicleExpense(
        vehicleId: String,
        toolsId: String,
        expense_date: String,
        expense_type: String,
        amount: String,
        km_reading: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val expense_dateRequestBody: RequestBody = expense_date.toRequestBody(contentType)
            val expense_typeRequestBody: RequestBody = expense_type.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val km_readingRequestBody: RequestBody = km_reading.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateVehicleExpense(
                    vehicleId,
                    toolsId,
                    expense_dateRequestBody,
                    expense_typeRequestBody,
                    amountRequestBody,
                    km_readingRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO Added by Jaimit 12/02/2020
    // Permits
    fun addVehiclePermits(
        vehicleId: String,
        permit_type: String,
        permit_number: String,
        issue_date: String,
        expiry_date: String,
        amount: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val permit_typeRequestBody: RequestBody = permit_type.toRequestBody(contentType)
            val permit_numberRequestBody: RequestBody = permit_number.toRequestBody(contentType)
            val issue_dateRequestBody: RequestBody = issue_date.toRequestBody(contentType)
            val expiry_dateRequestBody: RequestBody = expiry_date.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addVehiclePermits(
                    vehicleId,
                    permit_typeRequestBody,
                    permit_numberRequestBody,
                    issue_dateRequestBody,
                    expiry_dateRequestBody,
                    amountRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateVehiclePermits(
        vehicleId: String,
        toolsId: String,
        permit_type: String,
        permit_number: String,
        issue_date: String,
        expiry_date: String,
        amount: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val permit_typeRequestBody: RequestBody = permit_type.toRequestBody(contentType)
            val permit_numberRequestBody: RequestBody = permit_number.toRequestBody(contentType)
            val issue_dateRequestBody: RequestBody = issue_date.toRequestBody(contentType)
            val expiry_dateRequestBody: RequestBody = expiry_date.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateVehiclePermits(
                    vehicleId,
                    toolsId,
                    permit_typeRequestBody,
                    permit_numberRequestBody,
                    issue_dateRequestBody,
                    expiry_dateRequestBody,
                    amountRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO Services
    fun addVehicleServices(
        vehicleId: String,
        service_type: String,
        date: String,
        garage: String,
        contact_no: String,
        amount: String,
        km_reading: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val dateRequestBody: RequestBody = date.toRequestBody(contentType)
            val garageRequestBody: RequestBody = garage.toRequestBody(contentType)
            val contact_noRequestBody: RequestBody = contact_no.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val km_readingRequestBody: RequestBody = km_reading.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addVehicleServices(
                    vehicleId,
                    service_type,
                    dateRequestBody,
                    garageRequestBody,
                    contact_noRequestBody,
                    amountRequestBody,
                    km_readingRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateVehicleServices(
        vehicleId: String,
        toolsId: String,
        service_type: String,
        date: String,
        garage: String,
        contact_no: String,
        amount: String,
        km_reading: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val dateRequestBody: RequestBody = date.toRequestBody(contentType)
            val garageRequestBody: RequestBody = garage.toRequestBody(contentType)
            val contact_noRequestBody: RequestBody = contact_no.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val km_readingRequestBody: RequestBody = km_reading.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateVehicleServices(
                    vehicleId,
                    toolsId,
                    service_type,
                    dateRequestBody,
                    garageRequestBody,
                    contact_noRequestBody,
                    amountRequestBody,
                    km_readingRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO Insurances
    fun addVehicleInsurances(
        vehicleId: String,
        company_name: String,
        policy_type: String,
        policy_number: String,
        issue_date: String,
        expiry_date: String,
        payment_mode: String,
        amount: String,
        premium: String,
        agent_name: String,
        agent_contact_no: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val company_nameRequestBody: RequestBody = company_name.toRequestBody(contentType)
            val policy_typeRequestBody: RequestBody = policy_type.toRequestBody(contentType)
            val policy_numberRequestBody: RequestBody = policy_number.toRequestBody(contentType)
            val issue_dateRequestBody: RequestBody = issue_date.toRequestBody(contentType)
            val expiry_dateRequestBody: RequestBody = expiry_date.toRequestBody(contentType)
            val payment_modeRequestBody: RequestBody = payment_mode.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val premiumRequestBody: RequestBody = premium.toRequestBody(contentType)
            val agent_nameRequestBody: RequestBody = agent_name.toRequestBody(contentType)
            val agent_contact_noRequestBody: RequestBody = agent_contact_no.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addVehicleInsurances(
                    vehicleId,
                    company_nameRequestBody,
                    policy_typeRequestBody,
                    policy_numberRequestBody,
                    issue_dateRequestBody,
                    expiry_dateRequestBody,
                    payment_modeRequestBody,
                    amountRequestBody,
                    premiumRequestBody,
                    agent_nameRequestBody,
                    agent_contact_noRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateVehicleInsurances(
        vehicleId: String,
        toolsId: String,
        company_name: String,
        policy_type: String,
        policy_number: String,
        issue_date: String,
        expiry_date: String,
        payment_mode: String,
        amount: String,
        premium: String,
        agent_name: String,
        agent_contact_no: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            val company_nameRequestBody: RequestBody = company_name.toRequestBody(contentType)
            val policy_typeRequestBody: RequestBody = policy_type.toRequestBody(contentType)
            val policy_numberRequestBody: RequestBody = policy_number.toRequestBody(contentType)
            val issue_dateRequestBody: RequestBody = issue_date.toRequestBody(contentType)
            val expiry_dateRequestBody: RequestBody = expiry_date.toRequestBody(contentType)
            val payment_modeRequestBody: RequestBody = payment_mode.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val premiumRequestBody: RequestBody = premium.toRequestBody(contentType)
            val agent_nameRequestBody: RequestBody = agent_name.toRequestBody(contentType)
            val agent_contact_noRequestBody: RequestBody = agent_contact_no.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateVehicleInsurances(
                    vehicleId,
                    toolsId,
                    company_nameRequestBody,
                    policy_typeRequestBody,
                    policy_numberRequestBody,
                    issue_dateRequestBody,
                    expiry_dateRequestBody,
                    payment_modeRequestBody,
                    amountRequestBody,
                    premiumRequestBody,
                    agent_nameRequestBody,
                    agent_contact_noRequestBody,
                    descriptionRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // TODO Store Reminder
    fun storeVehicleReminder(
        vehicleId: String,
        reminder_date: String,
        onApiCallCompleted: OnApiCallCompleted<Boolean>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL)
                .storeVehicleReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(
                    call: Call<Boolean?>,
                    response: Response<Boolean?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storePersonalDocumentsReminder(
        vehicleId: String,
        reminder_date: String,
        onApiCallCompleted: OnApiCallCompleted<Boolean>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL)
                .storePersonalDocumentsReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(
                    call: Call<Boolean?>,
                    response: Response<Boolean?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeHomeAppliancesReminder(
        vehicleId: String,
        reminder_date: String,
        onApiCallCompleted: OnApiCallCompleted<Boolean>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL)
                .storeHomeAppliancesReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(
                    call: Call<Boolean?>,
                    response: Response<Boolean?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storePropertyReminder(vehicleId: String, reminder_date: String, onApiCallCompleted: OnApiCallCompleted<Boolean>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL).storePropertyReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeIndustrialReminder(vehicleId: String, reminder_date: String, onApiCallCompleted: OnApiCallCompleted<Boolean>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL).storeIndustrialReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeOthersReminder(vehicleId: String, reminder_date: String, onApiCallCompleted: OnApiCallCompleted<Boolean>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL).storeOthersReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeVehicleRefuelReminder(vehicleId: String, reminder_date: String, onApiCallCompleted: OnApiCallCompleted<Boolean>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL).storeVehicleRefuelReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun transportVehiclReminder(vehicleId: String, reminder_date: String, onApiCallCompleted: OnApiCallCompleted<Boolean>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL).transportVehiclReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeVehicleExpensesReminder(vehicleId: String, reminder_date: String, onApiCallCompleted: OnApiCallCompleted<Boolean>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL).storeVehicleExpensesReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeVehicleInsuranceReminder(vehicleId: String, reminder_date: String, onApiCallCompleted: OnApiCallCompleted<Boolean>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL).storeVehicleInsuranceReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeVehicleAccidentReminder(vehicleId: String, reminder_date: String, onApiCallCompleted: OnApiCallCompleted<Boolean>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL).storeVehicleAccidentReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeVehiclePermitsReminder(
        vehicleId: String,
        reminder_date: String,
        onApiCallCompleted: OnApiCallCompleted<Boolean>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL)
                .storeVehiclePermitsReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(
                    call: Call<Boolean?>,
                    response: Response<Boolean?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeVehiclePucReminder(
        vehicleId: String,
        reminder_date: String,
        onApiCallCompleted: OnApiCallCompleted<Boolean>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL)
                .storeVehiclePucReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(
                    call: Call<Boolean?>,
                    response: Response<Boolean?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun storeVehicleServicesReminder(
        vehicleId: String,
        reminder_date: String,
        onApiCallCompleted: OnApiCallCompleted<Boolean>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<Boolean?> = ApiClient.getRestApiMethods(BASE_URL)
                .storeVehicleServicesReminder(vehicleId, reminder_date)
            call.enqueue(object : Callback<Boolean?> {
                override fun onResponse(
                    call: Call<Boolean?>,
                    response: Response<Boolean?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: Boolean? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // TODO Delete
    fun deleteVehicle(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deleteVehicle(vehicleId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteTransportVehicle(
        vehicleId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deleteTransportVehicle(vehicleId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO Transport Certificate related
    fun getTransportCertificate(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<ClsTransportCertificateResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<ClsTransportCertificateResponse?> = ApiClient.getRestApiMethods(BASE_URL).getTransportCertificate(id)
            call.enqueue(object : Callback<ClsTransportCertificateResponse?> {
                override fun onResponse(
                    call: Call<ClsTransportCertificateResponse?>,
                    response: Response<ClsTransportCertificateResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: ClsTransportCertificateResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<ClsTransportCertificateResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addFitnessCertificate(
        id: String,
        startDate: String,
        endDate: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<ClsAddFitnessCertificateResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val startDateRequestBody: RequestBody = startDate.toRequestBody(contentType)
            val endDateRequestBody: RequestBody = endDate.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<ClsAddFitnessCertificateResponse?> = ApiClient.getRestApiMethods(BASE_URL)
                .addFitnessCertificate(
                    id,
                    startDateRequestBody,
                    endDateRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<ClsAddFitnessCertificateResponse?> {
                override fun onResponse(
                    call: Call<ClsAddFitnessCertificateResponse?>,
                    response: Response<ClsAddFitnessCertificateResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: ClsAddFitnessCertificateResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<ClsAddFitnessCertificateResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addRoadTax(
        id: String,
        startDate: String,
        endDate: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<ClsAddFitnessCertificateResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val startDateRequestBody: RequestBody = startDate.toRequestBody(contentType)
            val endDateRequestBody: RequestBody = endDate.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<ClsAddFitnessCertificateResponse?> = ApiClient.getRestApiMethods(BASE_URL)
                .addRoadTax(
                    id,
                    startDateRequestBody,
                    endDateRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<ClsAddFitnessCertificateResponse?> {
                override fun onResponse(
                    call: Call<ClsAddFitnessCertificateResponse?>,
                    response: Response<ClsAddFitnessCertificateResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: ClsAddFitnessCertificateResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<ClsAddFitnessCertificateResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addPermit(
        id: String,
        startDate: String,
        endDate: String,
        permitType: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<ClsAddFitnessCertificateResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val startDateRequestBody: RequestBody = startDate.toRequestBody(contentType)
            val endDateRequestBody: RequestBody = endDate.toRequestBody(contentType)
            val permitTypeRequestBody: RequestBody = permitType.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<ClsAddFitnessCertificateResponse?> = ApiClient.getRestApiMethods(BASE_URL)
                .addPermit(
                    id,
                    startDateRequestBody,
                    endDateRequestBody,
                    permitTypeRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<ClsAddFitnessCertificateResponse?> {
                override fun onResponse(
                    call: Call<ClsAddFitnessCertificateResponse?>,
                    response: Response<ClsAddFitnessCertificateResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: ClsAddFitnessCertificateResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<ClsAddFitnessCertificateResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addSpeedGoverner(
        id: String,
        startDate: String,
        endDate: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<ClsAddFitnessCertificateResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val startDateRequestBody: RequestBody = startDate.toRequestBody(contentType)
            val endDateRequestBody: RequestBody = endDate.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<ClsAddFitnessCertificateResponse?> = ApiClient.getRestApiMethods(BASE_URL)
                .addSpeedGoverner(
                    id,
                    startDateRequestBody,
                    endDateRequestBody,
                    fileList
                )
            call.enqueue(object : Callback<ClsAddFitnessCertificateResponse?> {
                override fun onResponse(
                    call: Call<ClsAddFitnessCertificateResponse?>,
                    response: Response<ClsAddFitnessCertificateResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: ClsAddFitnessCertificateResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<ClsAddFitnessCertificateResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteFuels(
        vehicleId: String,
        toolId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deleteFuels(vehicleId, toolId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteService(
        vehicleId: String,
        toolId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deleteService(vehicleId, toolId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteExpenses(
        vehicleId: String,
        toolId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deleteExpenses(vehicleId, toolId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteInsurances(
        vehicleId: String,
        toolId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deleteInsurances(vehicleId, toolId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deletePermits(
        vehicleId: String,
        toolId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deletePermits(vehicleId, toolId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deletePUC(
        vehicleId: String,
        toolId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deletePUC(vehicleId, toolId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteOthers(
        othersId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deleteOthers(othersId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteAccident(
        vehicleId: String,
        toolId: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL).deleteAccident(vehicleId, toolId)
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //ToDO Property
    fun getPropertyList(
        onApiCallCompleted: OnApiCallCompleted<GetPropertyListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetPropertyListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getPropertyList()

            call.enqueue(object : Callback<GetPropertyListResponse?> {
                override fun onResponse(
                    call: Call<GetPropertyListResponse?>,
                    response: Response<GetPropertyListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetPropertyListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetPropertyListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPropertyTypesList(
        onApiCallCompleted: OnApiCallCompleted<GetPropertyTypeListResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<GetPropertyTypeListResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).getPropertyTypesList()

            call.enqueue(object : Callback<GetPropertyTypeListResponse?> {
                override fun onResponse(
                    call: Call<GetPropertyTypeListResponse?>,
                    response: Response<GetPropertyTypeListResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: GetPropertyTypeListResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<GetPropertyTypeListResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteProperty(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).deleteProperty(id)

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addProperty(
        property_type: String,
        ownership_status: String,
        tenant_name: String,
        tenant_number: String,
        agree_start_date: String,
        agree_end_date: String,
        rent_amount: String,
        rent_collection_date: String,
        status: String,
        location: String,
        city_name: String,
        property_address: String,
        purchase_date: String,
        tenant_permenent_address: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val property_typeRequestBody: RequestBody = property_type.toRequestBody(contentType)
            val ownership_statusRequestBody: RequestBody = ownership_status.toRequestBody(contentType)
            val tenant_nameRequestBody: RequestBody = tenant_name.toRequestBody(contentType)
            val tenant_numberRequestBody: RequestBody = tenant_number.toRequestBody(contentType)
            val agree_start_dateRequestBody: RequestBody = agree_start_date.toRequestBody(contentType)
            val agree_end_dateRequestBody: RequestBody = agree_end_date.toRequestBody(contentType)
            val rent_amountRequestBody: RequestBody = rent_amount.toRequestBody(contentType)
            val rent_collection_dateRequestBody: RequestBody = rent_collection_date.toRequestBody(contentType)
            val statusRequestBody: RequestBody = status.toRequestBody(contentType)
            val locationRequestBody: RequestBody = location.toRequestBody(contentType)
            val city_nameRequestBody: RequestBody = city_name.toRequestBody(contentType)
            val property_addressRequestBody: RequestBody = property_address.toRequestBody(contentType)
            val purchase_datetypeRequestBody: RequestBody = purchase_date.toRequestBody(contentType)
            val tenant_permenent_addressRequestBody: RequestBody = tenant_permenent_address.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }

            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL)
                .addProperty(
                    property_typeRequestBody,
                    ownership_statusRequestBody,
                    tenant_nameRequestBody,
                    tenant_numberRequestBody,
                    agree_start_dateRequestBody,
                    agree_end_dateRequestBody,
                    rent_amountRequestBody,
                    rent_collection_dateRequestBody,
                    statusRequestBody,
                    locationRequestBody,
                    city_nameRequestBody,
                    property_addressRequestBody,
                    purchase_datetypeRequestBody,
                    tenant_permenent_addressRequestBody,
                    if (fileList != null && fileList.size > 0) fileList else null
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateProperty(
        id: String,
        property_type: String,
        ownership_status: String,
        tenant_name: String,
        tenant_number: String,
        agree_start_date: String,
        agree_end_date: String,
        rent_amount: String,
        rent_collection_date: String,
        status: String,
        location: String,
        city_name: String,
        property_address: String,
        purchase_date: String,
        tenant_permenent_address: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val property_typeRequestBody: RequestBody = property_type.toRequestBody(contentType)
            val ownership_statusRequestBody: RequestBody = ownership_status.toRequestBody(contentType)
            val tenant_nameRequestBody: RequestBody = tenant_name.toRequestBody(contentType)
            val tenant_numberRequestBody: RequestBody = tenant_number.toRequestBody(contentType)
            val agree_start_dateRequestBody: RequestBody = agree_start_date.toRequestBody(contentType)
            val agree_end_dateRequestBody: RequestBody = agree_end_date.toRequestBody(contentType)
            val rent_amountRequestBody: RequestBody = rent_amount.toRequestBody(contentType)
            val rent_collection_dateRequestBody: RequestBody = rent_collection_date.toRequestBody(contentType)
            val statusRequestBody: RequestBody = status.toRequestBody(contentType)
            val locationRequestBody: RequestBody = location.toRequestBody(contentType)
            val city_nameRequestBody: RequestBody = city_name.toRequestBody(contentType)
            val property_addressRequestBody: RequestBody = property_address.toRequestBody(contentType)
            val purchase_datetypeRequestBody: RequestBody = purchase_date.toRequestBody(contentType)
            val tenant_permenent_addressRequestBody: RequestBody = tenant_permenent_address.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> = ApiClient.getRestApiMethods(BASE_URL)
                .updateProperty(
                    id,
                    property_typeRequestBody,
                    ownership_statusRequestBody,
                    tenant_nameRequestBody,
                    tenant_numberRequestBody,
                    agree_start_dateRequestBody,
                    agree_end_dateRequestBody,
                    rent_amountRequestBody,
                    rent_collection_dateRequestBody,
                    statusRequestBody,
                    locationRequestBody,
                    city_nameRequestBody,
                    property_addressRequestBody,
                    purchase_datetypeRequestBody,
                    tenant_permenent_addressRequestBody,
                    if (fileList != null && fileList.size > 0) fileList else null
                )
            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO Added by Jaimit 08/03/2020
    fun getVehicleDetails(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<EditVehicleResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditVehicleResponse?> = ApiClient.getRestApiMethods(BASE_URL).getVehicleDetails(id)

            call.enqueue(object : Callback<EditVehicleResponse?> {
                override fun onResponse(
                    call: Call<EditVehicleResponse?>,
                    response: Response<EditVehicleResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditVehicleResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditVehicleResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTransportVehicleDetails(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<EditTransportVehicleResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditTransportVehicleResponse?> = ApiClient.getRestApiMethods(BASE_URL).getTransportVehicleDetails(id)

            call.enqueue(object : Callback<EditTransportVehicleResponse?> {
                override fun onResponse(
                    call: Call<EditTransportVehicleResponse?>,
                    response: Response<EditTransportVehicleResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditTransportVehicleResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditTransportVehicleResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPersonalDocumentsDetails(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<EditPersonalDocumentsResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditPersonalDocumentsResponse?> = ApiClient.getRestApiMethods(BASE_URL).getPersonalDocumentsDetails(id)

            call.enqueue(object : Callback<EditPersonalDocumentsResponse?> {
                override fun onResponse(
                    call: Call<EditPersonalDocumentsResponse?>,
                    response: Response<EditPersonalDocumentsResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditPersonalDocumentsResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditPersonalDocumentsResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getHomeAppliancesDetails(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<EditHomeAppliancesResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditHomeAppliancesResponse?> = ApiClient.getRestApiMethods(BASE_URL).getHomeAppliancesDetails(id)

            call.enqueue(object : Callback<EditHomeAppliancesResponse?> {
                override fun onResponse(
                    call: Call<EditHomeAppliancesResponse?>,
                    response: Response<EditHomeAppliancesResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditHomeAppliancesResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditHomeAppliancesResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getIndustriesDetails(
        id: String,
        onApiCallCompleted: OnApiCallCompleted<EditIndustriesResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditIndustriesResponse?> = ApiClient.getRestApiMethods(BASE_URL).getIndustriesDetails(id)

            call.enqueue(object : Callback<EditIndustriesResponse?> {
                override fun onResponse(
                    call: Call<EditIndustriesResponse?>,
                    response: Response<EditIndustriesResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditIndustriesResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditIndustriesResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getOthersDetails(id: String, onApiCallCompleted: OnApiCallCompleted<EditOthersResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditOthersResponse?> = ApiClient.getRestApiMethods(BASE_URL).getOthersDetails(id)

            call.enqueue(object : Callback<EditOthersResponse?> {
                override fun onResponse(call: Call<EditOthersResponse?>, response: Response<EditOthersResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditOthersResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditOthersResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPropertyDetails(id: String, onApiCallCompleted: OnApiCallCompleted<EditPropertyResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditPropertyResponse?> = ApiClient.getRestApiMethods(BASE_URL).getPropertyDetails(id)

            call.enqueue(object : Callback<EditPropertyResponse?> {
                override fun onResponse(call: Call<EditPropertyResponse?>, response: Response<EditPropertyResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditPropertyResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditPropertyResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleAccidentDetails(vehicleId: String, toolId: String, onApiCallCompleted: OnApiCallCompleted<EditVehicleAccidentResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditVehicleAccidentResponse?> = ApiClient.getRestApiMethods(BASE_URL).getVehicleAccidentDetails(vehicleId, toolId)

            call.enqueue(object : Callback<EditVehicleAccidentResponse?> {
                override fun onResponse(call: Call<EditVehicleAccidentResponse?>, response: Response<EditVehicleAccidentResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditVehicleAccidentResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditVehicleAccidentResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleExpenseDetails(vehicleId: String, toolId: String, onApiCallCompleted: OnApiCallCompleted<EditVehicleExpenseResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditVehicleExpenseResponse?> = ApiClient.getRestApiMethods(BASE_URL).getVehicleExpenseDetails(vehicleId, toolId)

            call.enqueue(object : Callback<EditVehicleExpenseResponse?> {
                override fun onResponse(call: Call<EditVehicleExpenseResponse?>, response: Response<EditVehicleExpenseResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditVehicleExpenseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditVehicleExpenseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleInsuranceDetails(vehicleId: String, toolId: String, onApiCallCompleted: OnApiCallCompleted<EditVehicleInsuranceResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditVehicleInsuranceResponse?> = ApiClient.getRestApiMethods(BASE_URL).getVehicleInsuranceDetails(vehicleId, toolId)

            call.enqueue(object : Callback<EditVehicleInsuranceResponse?> {
                override fun onResponse(call: Call<EditVehicleInsuranceResponse?>, response: Response<EditVehicleInsuranceResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditVehicleInsuranceResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditVehicleInsuranceResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehiclePermitDetails(vehicleId: String, toolId: String, onApiCallCompleted: OnApiCallCompleted<EditVehiclePermitResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditVehiclePermitResponse?> = ApiClient.getRestApiMethods(BASE_URL).getVehiclePermitDetails(vehicleId, toolId)

            call.enqueue(object : Callback<EditVehiclePermitResponse?> {
                override fun onResponse(call: Call<EditVehiclePermitResponse?>, response: Response<EditVehiclePermitResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditVehiclePermitResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditVehiclePermitResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehiclePucDetails(vehicleId: String, toolId: String, onApiCallCompleted: OnApiCallCompleted<EditVehiclePucResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditVehiclePucResponse?> = ApiClient.getRestApiMethods(BASE_URL).getVehiclePucDetails(vehicleId, toolId)

            call.enqueue(object : Callback<EditVehiclePucResponse?> {
                override fun onResponse(call: Call<EditVehiclePucResponse?>, response: Response<EditVehiclePucResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditVehiclePucResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditVehiclePucResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleRefuelDetails(vehicleId: String, toolId: String, onApiCallCompleted: OnApiCallCompleted<EditVehicleRefuelResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditVehicleRefuelResponse?> = ApiClient.getRestApiMethods(BASE_URL).getVehicleRefuelDetails(vehicleId, toolId)

            call.enqueue(object : Callback<EditVehicleRefuelResponse?> {
                override fun onResponse(call: Call<EditVehicleRefuelResponse?>, response: Response<EditVehicleRefuelResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditVehicleRefuelResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditVehicleRefuelResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVehicleServiceDetails(vehicleId: String, toolId: String, onApiCallCompleted: OnApiCallCompleted<EditVehicleServiceResponse>) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val call: Call<EditVehicleServiceResponse?> = ApiClient.getRestApiMethods(BASE_URL).getVehicleServiceDetails(vehicleId, toolId)

            call.enqueue(object : Callback<EditVehicleServiceResponse?> {
                override fun onResponse(call: Call<EditVehicleServiceResponse?>, response: Response<EditVehicleServiceResponse?>) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: EditVehicleServiceResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<EditVehicleServiceResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Multipart
    @Suppress("LocalVariableName", "RemoveRedundantCallsOfConversionMethods", "UNNECESSARY_SAFE_CALL", "UNNECESSARY_NOT_NULL_ASSERTION")
    fun addVehical(
        vehicle_type_id: String,
        vehicle_brand_id: String,
        name: String,
        owner_name: String,
        vehicle_condition: String,
        build_year: String,
        registration_number: String,
        fuel_type: String,
        tank_capicity: String,
        purchase_date: String,
        purchase_price: String,
        km_reading: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)

            val vehicle_type_idRequestBody: RequestBody =
                vehicle_type_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val vehicle_brand_idRequestBody: RequestBody =
                vehicle_brand_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val nameRequestBody: RequestBody =
                name.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val owner_nameRequestBody: RequestBody =
                owner_name?.toRequestBody("text/plain".toMediaTypeOrNull())!!
            val vehicle_conditionRequestBody: RequestBody =
                vehicle_condition?.toRequestBody("text/plain".toMediaTypeOrNull())!!
            val build_yearRequestBody: RequestBody =
                build_year?.toRequestBody("text/plain".toMediaTypeOrNull())!!
            val registration_numberRequestBody: RequestBody =
                registration_number.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val fuel_typeRequestBody: RequestBody =
                fuel_type.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val tank_capicityRequestBody: RequestBody =
                tank_capicity.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val purchase_dateRequestBody: RequestBody =
                purchase_date?.toRequestBody("text/plain".toMediaTypeOrNull())!!
            val purchase_priceRequestBody: RequestBody =
                purchase_price?.toRequestBody("text/plain".toMediaTypeOrNull())!!
            val km_readingRequestBody: RequestBody =
                km_reading?.toRequestBody("text/plain".toMediaTypeOrNull())!!
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addVehical(
                    vehicle_type_idRequestBody,
                    vehicle_brand_idRequestBody,
                    nameRequestBody,
                    owner_nameRequestBody,
                    vehicle_conditionRequestBody,
                    build_yearRequestBody,
                    registration_numberRequestBody,
                    fuel_typeRequestBody,
                    tank_capicityRequestBody,
                    purchase_dateRequestBody,
                    purchase_priceRequestBody,
                    km_readingRequestBody,
                    if (fileList != null && fileList.size > 0) fileList else null
                )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("LocalVariableName", "RemoveRedundantCallsOfConversionMethods", "UNNECESSARY_SAFE_CALL", "UNNECESSARY_NOT_NULL_ASSERTION")
    fun addTransportVehical(
        vehicle_type_id: String,
        vehicle_brand_id: String,
        vehicle_category_id: String,
        register_no: String,
        driver_name: String,
        driver_address: String,
        driver_phone_no: String,
        driver_license_no: String,
        driver_license_expiry_date: String,
        driver_license_type: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)

            val vehicle_type_idRequestBody: RequestBody = vehicle_type_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val vehicle_brand_idRequestBody: RequestBody = vehicle_brand_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val vehicle_category_idRequestBody: RequestBody = vehicle_category_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val register_noRequestBody: RequestBody = register_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_nameRequestBody: RequestBody = driver_name.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_addressRequestBody: RequestBody = driver_address.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_phone_noRequestBody: RequestBody = driver_phone_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_license_noRequestBody: RequestBody = driver_license_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_license_expiry_dateRequestBody: RequestBody = driver_license_expiry_date.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_license_typeRequestBody: RequestBody = driver_license_type.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).addTransportVehical(
                    vehicle_type_idRequestBody,
                    vehicle_brand_idRequestBody,
                    vehicle_category_idRequestBody,
                    register_noRequestBody,
                    driver_nameRequestBody,
                    driver_addressRequestBody,
                    driver_phone_noRequestBody,
                    driver_license_noRequestBody,
                    driver_license_expiry_dateRequestBody,
                    driver_license_typeRequestBody,
                    if (fileList != null && fileList.size > 0) fileList else null
                )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("LocalVariableName", "RemoveRedundantCallsOfConversionMethods", "UNNECESSARY_SAFE_CALL", "UNNECESSARY_NOT_NULL_ASSERTION")
    fun updateTransportVehical(
        id: String,
        vehicle_type_id: String,
        vehicle_brand_id: String,
        vehicle_category_id: String,
        register_no: String,
        driver_name: String,
        driver_address: String,
        driver_phone_no: String,
        driver_license_no: String,
        driver_license_expiry_date: String,
        driver_license_type: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)

            val vehicle_type_idRequestBody: RequestBody = vehicle_type_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val vehicle_brand_idRequestBody: RequestBody = vehicle_brand_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val vehicle_category_idRequestBody: RequestBody = vehicle_category_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val register_noRequestBody: RequestBody = register_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_nameRequestBody: RequestBody = driver_name.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_addressRequestBody: RequestBody = driver_address.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_phone_noRequestBody: RequestBody = driver_phone_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_license_noRequestBody: RequestBody = driver_license_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_license_expiry_dateRequestBody: RequestBody = driver_license_expiry_date.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val driver_license_typeRequestBody: RequestBody = driver_license_type.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL).updateTransportVehical(
                    id,
                    vehicle_type_idRequestBody,
                    vehicle_brand_idRequestBody,
                    vehicle_category_idRequestBody,
                    register_noRequestBody,
                    driver_nameRequestBody,
                    driver_addressRequestBody,
                    driver_phone_noRequestBody,
                    driver_license_noRequestBody,
                    driver_license_expiry_dateRequestBody,
                    driver_license_typeRequestBody,
                    if (fileList != null && fileList.size > 0) fileList else null
                )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addPersonalDocument(
        //        userID: String,
        personalDocumentType: String,
        name: String,
        startDate: String,
        endDate: String,
        address: String,
        description: String,
        fee: String,
        status: String,
        companyName: String,
        sumAssurd: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()
            //            val userIdRequestBody: RequestBody = userID.toRequestBody()
            val personalDocumentTypeRequestBody: RequestBody = personalDocumentType.toRequestBody()
            val nameRequestBody: RequestBody = name.toRequestBody(contentType)
            val startDateRequestBody: RequestBody = startDate.toRequestBody(contentType)
            val endDateRequestBody: RequestBody = endDate.toRequestBody(contentType)
            val addressRequestBody: RequestBody = address.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val feeRequestBody = fee.toRequestBody(contentType)
            val statusRequestBody: RequestBody = status.toRequestBody(contentType)
            val companyNameRequestBody: RequestBody = companyName.toRequestBody(contentType)
            val sumAssurdRequestBody: RequestBody = sumAssurd.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL)
                    .addPersonalDocument(
                        //                        userIdRequestBody,
                        personalDocumentTypeRequestBody,
                        nameRequestBody,
                        startDateRequestBody,
                        endDateRequestBody,
                        addressRequestBody,
                        descriptionRequestBody,
                        feeRequestBody,
                        statusRequestBody,
                        companyNameRequestBody,
                        sumAssurdRequestBody,
                        if (fileList != null && fileList.size > 0) fileList else null
                    )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun addIndustries(
        industryTypeId: String,
        purchaseDate: String,
        expiryDate: String,
        amount: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val industryTypeIdRequestBody: RequestBody = industryTypeId.toRequestBody()
            val purchaseDateRequestBody: RequestBody = purchaseDate.toRequestBody(contentType)
            val expiryDateRequestBody: RequestBody = expiryDate.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL)
                    .addIndustries(
                        industryTypeIdRequestBody,
                        purchaseDateRequestBody,
                        expiryDateRequestBody,
                        amountRequestBody,
                        descriptionRequestBody,
                        if (fileList != null && fileList.size > 0) fileList else null
                    )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addOthers(
        title: String,
        startDate: String,
        endDate: String,
        description: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val titleRequestBody: RequestBody = title.toRequestBody()
            val startDateRequestBody: RequestBody = startDate.toRequestBody(contentType)
            val endDateRequestBody: RequestBody = endDate.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL)
                    .addOthers(
                        titleRequestBody,
                        startDateRequestBody,
                        endDateRequestBody,
                        descriptionRequestBody,
                        if (fileList != null && fileList.size > 0) fileList else null
                    )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addHomeAppliances(
        home_appliance_type_id: String,
        home_appliance_brand_id: String,
        name: String,
        purchase_from: String,
        purchase_date: String,
        warrenty_period: String,
        warrenty_expired_date: String,
        description: String,
        amount: String,
        status: String,
        purchaseName: String,
        purchaseAddress: String,
        selectedImageList: ArrayList<Uri?>,
        onApiCallCompleted: OnApiCallCompleted<BaseResponse>
    ) {
        try {
            if (!Utility.isNetworkConnected(mContext)) {
                onApiCallCompleted.apiFailure(mContext.getString(R.string.msg_network_error))
                return
            }
            showProgress(mContext)
            val contentType: MediaType? = "text/plain".toMediaTypeOrNull()

            val home_appliance_type_idRequestBody: RequestBody = home_appliance_type_id.toRequestBody()
            val home_appliance_brand_idRequestBody: RequestBody = home_appliance_brand_id.toRequestBody(contentType)
            val nameRequestBody: RequestBody = name.toRequestBody(contentType)
            val purchase_fromRequestBody: RequestBody = purchase_from.toRequestBody(contentType)
            val purchase_dateRequestBody: RequestBody = purchase_date.toRequestBody(contentType)
            val warrenty_periodRequestBody: RequestBody = warrenty_period.toRequestBody()
            val warrenty_expired_dateRequestBody: RequestBody = warrenty_expired_date.toRequestBody(contentType)
            val descriptionRequestBody: RequestBody = description.toRequestBody(contentType)
            val amountRequestBody: RequestBody = amount.toRequestBody(contentType)
            val statusRequestBody: RequestBody = status.toRequestBody(contentType)
            val purchaseNameRequestBody: RequestBody = purchaseName.toRequestBody(contentType)
            val purchaseAddressRequestBody: RequestBody = purchaseAddress.toRequestBody(contentType)
            val fileList: ArrayList<MultipartBody.Part?> = ArrayList()
            for (i in selectedImageList.indices) {
                if (selectedImageList[i] != null) {
                    fileList.add(createMultipartBodyRequest(selectedImageList[i]))
                }
            }
            val call: Call<BaseResponse?> =
                ApiClient.getRestApiMethods(BASE_URL)
                    .addHomeAppliances(
                        home_appliance_type_idRequestBody,
                        home_appliance_brand_idRequestBody,
                        nameRequestBody,
                        purchase_fromRequestBody,
                        purchase_dateRequestBody,
                        warrenty_periodRequestBody,
                        warrenty_expired_dateRequestBody,
                        descriptionRequestBody,
                        amountRequestBody,
                        statusRequestBody,
                        purchaseNameRequestBody,
                        purchaseAddressRequestBody,
                        if (fileList != null && fileList.size > 0) fileList else null
                    )

            call.enqueue(object : Callback<BaseResponse?> {
                override fun onResponse(
                    call: Call<BaseResponse?>,
                    response: Response<BaseResponse?>
                ) {
                    cancelProgress()
                    if (checkResponseCodes(response, onApiCallCompleted)) {
                        val body: BaseResponse? = response.body()
                        onApiCallCompleted.apiSuccess(body)
                    }
                }

                override fun onFailure(call: Call<BaseResponse?>, t: Throwable) {
                    cancelProgress()
                    onApiCallCompleted.apiFailure(t.message!!)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}