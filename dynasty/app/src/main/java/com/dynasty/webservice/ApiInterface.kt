package com.dynasty.webservice

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface ApiInterface {

    @POST
    fun callPostMethod(@Url url: String, @Body body: HashMap<String, String>): Call<ResponseBody>

    @Multipart
    @POST
    fun callPostMethodMultipart(@Url url: String, @PartMap partMap: Map<String, String>, @Part files: List<MultipartBody.Part>): Call<ResponseBody>

    @POST
    fun callPostMethod(@Url url: String, @Body body: RequestBody): Call<ResponseBody>

    @Multipart
    fun callPostMethod(@Url url: String, @Body body: List<RequestBody>): Call<ResponseBody>

    @GET
    fun callGetMethod(@Url url: String): Call<ResponseBody>
}
