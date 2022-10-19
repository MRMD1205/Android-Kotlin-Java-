package com.dynasty.webservice


object APIs {

//    private val DOMAIN = "http://106.201.238.169:7094"
    private val DOMAIN = "http://5.77.39.57:50056" //new local

    val BASE_URL = "$DOMAIN/api/"
    val BASE_IMAGE_PATH = DOMAIN + "storage/"


    val API_GET_CATEGORY_LIST = "Category/GetCategoryList"
    val API_GET_TAG_LIST = "/api/Tag/GetTagList"
    val API_GET_BUSINESS_LIST = "Business/GetBusinessList/"
    val API_SEARCH_BUSINESS_LIST = "Business/SearchBusiness/"
    val API_GET_BUSINESS_DETAILS = "Business/GetBusinessbyId/"

    val API_REFRESH_TOKEN = "auth/update/device-token"

}