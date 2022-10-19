package com.example.pagination.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("v2/list")
    fun apiCall(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<String>
}