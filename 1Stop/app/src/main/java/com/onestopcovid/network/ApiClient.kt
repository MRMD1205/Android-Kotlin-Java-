package com.onestopcovid.network

import com.google.gson.Gson
import com.onestopcovid.OneStopApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofitApi: Retrofit? = null
        private var okHttpClient: OkHttpClient? = null
        private var gsonConverterFactory: GsonConverterFactory? = null
        private val retrofits: HashMap<String, Retrofit> = HashMap()

        fun getRestApiMethods(baseUrl: String): ApiMethods {
            return createRetrofitBase(baseUrl)!!.create(ApiMethods::class.java)
        }

        private fun createRetrofitBase(baseUrl: String): Retrofit? {
            var retrofitApi: Retrofit? = retrofits[baseUrl]
            if (retrofitApi == null) {
                retrofitApi = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(getGsonConverter())
                    .client(getOkHttpClient())
                    .build()
            }
            return retrofitApi
        }

        fun getOkHttpClient(): OkHttpClient {
            if (okHttpClient == null) {
                val httpClient = OkHttpClient.Builder()
                httpClient.connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                val logging = HttpLoggingInterceptor()
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                // add logging as last interceptor
                // Todo to active default header active below code
                httpClient.addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val original: Request = chain.request()
                        val request: Request =
                            chain.request().newBuilder()
                                .header("Authorization", "Bearer "  /*+ OneStopApplication.instance.preferenceData?.getValueFromKey(PREF_DEVICE_TOKEN)!!*/)
                                .header("Accept", "application/json")
                                .method(original.method, original.body).build()
                        return chain.proceed(request)
                    }
                })
                // TODO ask for pankti
                //                httpClient.hostnameVerifier(HostnameVerifier { hostname, session ->
                //                    val hv: HostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
                //                    hv.verify("kevimart.com", session)
                //                })
                httpClient.addInterceptor(logging)
                okHttpClient = httpClient.build()
            }
            return okHttpClient!!
        }

        private fun getGsonConverter(): GsonConverterFactory {
            if (gsonConverterFactory == null) {
                val gson: Gson = OneStopApplication.instance.getGson()
                gsonConverterFactory = GsonConverterFactory.create(gson)
            }
            return gsonConverterFactory!!
        }
    }
}