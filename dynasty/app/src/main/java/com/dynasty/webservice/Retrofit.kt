package com.dynasty.webservice

import android.content.Context
import com.dynasty.util.Logger
import com.dynasty.util.PreferenceData
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class Retrofit(private val context: Context) {

    private var baseURL: String? = null
    private var endPoint: String? = null
    private var endPointExtra = ""

    private var params = HashMap<String, String>()
    private var headerMap = HashMap<String, String>()

    private var bodyRequest: RequestBody? = null
    private var files: MutableList<Part> = ArrayList()
    private var mUploadCallbacks: ProgressRequestBody.UploadCallbacks? = null

    /**
     * @param baseUrl
     * @return Instance
     * set Base Url for temporary
     * optional method if you set default Base URL in APIs class
     */
    fun setUrl(baseUrl: String): Retrofit {
        this.baseURL = baseUrl
        this.endPoint = baseUrl
        return this
    }

    /**
     * @param endPoint
     * @return Instance
     * set Endpoint when call every time
     */
    fun setAPI(endPoint: String): Retrofit {
        this.endPoint = endPoint
        Logger.e("URL", APIs.BASE_URL + endPoint)
        return this
    }

    /**
     * @param token
     * @return Instance
     * set Header when call every time
     */
    fun setHeader(token: String): Retrofit {
        headerMap["Authorization"] = token
        Logger.e("header", token)
        return this
    }


    /**
     * @param headerMap
     * @return Instance
     * set Header when call every time
     */
    fun setHeader(headerMap: HashMap<String, String>): Retrofit {
        this.headerMap = headerMap
        for ((key, value) in headerMap) {
            Logger.e("header", key + "\t" + value)
        }
        return this
    }

    /**
     * @param mListener
     * @return Instance
     * set Endpoint when call every time
     */
    fun setMediaFileUploadListener(mListener: ProgressRequestBody.UploadCallbacks): Retrofit {
        this.mUploadCallbacks = mListener
        return this
    }

    /**
     * @param params
     * @return Call
     * to set request parameter
     */
    fun setGetParameters(params: HashMap<String, String>?): Retrofit {
        if (params != null && !params.isEmpty()) {
            for ((key, value) in params) {
                Logger.e("params", key + "\t" + value)
                endPointExtra =
                    endPointExtra + (if (endPointExtra.contains("?")) "&" else "?") + key + "=" + value
            }
            Logger.e("EndpointExtra: ", endPointExtra)
        }
        return this
    }

    fun setParameters(params: String?): Retrofit {
        if (params != null && !params.isEmpty()) {
            endPointExtra = endPointExtra + params
            Logger.e("EndpointExtra: ", endPointExtra)
        }
        return this
    }

    fun setCustomGetParameters(params: HashMap<String, String>?): Retrofit {
        if (params != null && !params.isEmpty()) {
            endPointExtra += "json"
            for ((key, value) in params) {
                Logger.e("params", key + "\t" + value)
                endPointExtra =
                    endPointExtra + (if (endPointExtra.contains("?")) "&" else "?") + key + "=" + value
            }
            Logger.e("EndpointExtra: ", endPointExtra)
        }
        return this
    }

    /**
     * @param params
     * @return Call
     * to set request parameter
     */
    fun setParameters(params: HashMap<String, String>): Retrofit {
        this.params = params
        for ((key, value) in params) {
            Logger.e("params", key + "\t" + value)
        }
        return this
    }

    fun setParameters(jsonObject: JSONObject): Retrofit {
        bodyRequest =
            RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
        return this
    }


    private fun getAPIInterface(client: OkHttpClient): ApiInterface {
        return retrofit2.Retrofit.Builder()
            .baseUrl(if (baseURL != null) baseURL!! else APIs.BASE_URL).client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build().create<ApiInterface>(
                ApiInterface::class.java!!
            )
    }

    fun setCallBackListener(listener: JSONCallback) {
        makeCall().enqueue(listener)
    }

    fun setCustomCallBackListener(listener: JSONCallback) {
        makeCustomCall().enqueue(listener)
    }


    private fun makeCall(): Call<ResponseBody> {
        val call: Call<ResponseBody>

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                public override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val request: Request
                    if (PreferenceData.isUserLoggedIn()) {
                        request = chain.request().newBuilder().header(
                            "Authorization",
                            "Bearer " + PreferenceData.getDeviceToken()
                        )
                            .method(original.method, original.body).build()
                    } else {
                        request =
                            chain.request().newBuilder().method(original.method, original.body)
                                .build()
                    }
                    return chain.proceed(request)
                }
            }).build()

        val APIInterface = retrofit2.Retrofit.Builder()
            .baseUrl(APIs.BASE_URL)
            .client(client).addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create<ApiInterface>(ApiInterface::class.java)

        if (bodyRequest != null) {
            call = APIInterface.callPostMethod(endPoint!!, bodyRequest!!)
        } else if (params.size > 0) {
            call = APIInterface.callPostMethod(endPoint!!, params)
        } else {
            call = APIInterface.callGetMethod(endPoint + endPointExtra)
        }
        return call
    }

    private fun makeCustomCall(): Call<ResponseBody> {
        val call: Call<ResponseBody>
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                public override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val request: Request
                    if (PreferenceData.isUserLoggedIn()) {
                        request = chain.request().newBuilder().header(
                            "Authorization",
                            "Bearer " + PreferenceData.getDeviceToken()
                        ).method(original.method, original.body).build()
                    } else {
                        request =
                            chain.request().newBuilder().method(original.method, original.body)
                                .build()
                    }
                    return chain.proceed(request)
                }
            })
            .build()
        val APIInterface =
            retrofit2.Retrofit.Builder().baseUrl(if (baseURL != null) baseURL!! else "")
                .client(client).addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build().create<ApiInterface>(
                    ApiInterface::class.java!!
                )

        if (bodyRequest != null) {
            call = APIInterface.callPostMethod(baseURL!!, bodyRequest!!)
        } else if (params.size > 0) {
            call = APIInterface.callPostMethod(baseURL!!, params)
        } else {
            call = APIInterface.callGetMethod(baseURL + endPointExtra)
        }
        return call
    }

    /*  public void setCallBackListenerMultipart(JSONCallbackMultipart listener) {
           setCallBackListenerMultipart(listener, "");
       }*/

    fun setCallBackListenerMultipart(listener: JSONCallbackMultipart, tag: String) {
        makeCallMultipart(tag).enqueue(listener)
    }

    fun setCallBackListenerMultipart(listener: JSONCallbackMultipart) {
        makeCallMultipart().enqueue(listener)
    }

    private fun makeCallMultipart(tag: String): Call<ResponseBody> {
        Logger.e("Retrofit Tag: ", "==> $tag")
        val call: Call<ResponseBody>

        val client = OkHttpClient.Builder()
            .connectTimeout(1200, TimeUnit.SECONDS)
            .readTimeout(1200, TimeUnit.SECONDS)
            .writeTimeout(1200, TimeUnit.SECONDS)
            //                .addInterceptor(provideHttpLoggingInterceptor())
            /*.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request;
                    if (new SessionManager(context).isLoggedIn()) {
                        request = chain.request().newBuilder().header("Authorization", "Bearer " + new SessionManager(context).getUserDetail().getData().getToken()).method(original.method(), original.body()).tag(tag).build();
                    } else {
                        request = chain.request().newBuilder().method(original.method(), original.body()).tag(tag).build();
                    }
                    return chain.proceed(request);
                }
            })*/.build()

        val APIInterface = retrofit2.Retrofit.Builder()
            .baseUrl(if (baseURL != null) baseURL!! else APIs.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonStringConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create<ApiInterface>(ApiInterface::class.java)

        call = APIInterface.callPostMethodMultipart(endPoint!!, params, files)
        return call
    }

    /**
     * @param fileParams
     * @return Call
     */
    fun setFileParameters(fileParams: HashMap<String, File>): Retrofit {
        files = ArrayList<Part>()

        Logger.e((fileParams.size).toString())

        var body: MultipartBody.Part
        for (entry in fileParams.entries) {
            val fileName = entry.key
            Logger.e("FileName", fileName)
            Logger.e("FileKey", entry.key)
            Logger.e("FileType Image", entry.value.toString())
            body = MultipartBody.Part.createFormData(
                entry.key,
                fileName,
                RequestBody.create("image/*".toMediaTypeOrNull(), entry.value)
            )
            files.add(body)
        }
        return this

    }

    fun setFileParameters(
        params: HashMap<String, String>,
        fileParams: HashMap<String, File>
    ): Retrofit {
        files = ArrayList<Part>()
        this.params = params
        //        this.params.put("languageCode", new SessionManager(context).getDataByKey(SessionManager.KEY_LANGUAGE, AppConstants.EN).toUpperCase());

        Logger.e(("" + params.size + "---" + params.toString()))
        Logger.e((fileParams.size).toString())

        var body: MultipartBody.Part
        for (entry in fileParams.entries) {
            val fileName = entry.key
            Logger.e("FileName", fileName)
            Logger.e("FileKey", entry.key)
            Logger.e("FileType Image", entry.value.toString())
            body = MultipartBody.Part.createFormData(
                entry.key,
                entry.value.getPath().substring(entry.value.getPath().lastIndexOf("/") + 1),
                RequestBody.create("image/*".toMediaTypeOrNull(), entry.value)
            )
            files.add(body)
        }
        return this
    }


    private fun makeCallMultipart(): Call<ResponseBody> {
        val call: Call<ResponseBody>

        val client = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).addInterceptor(
                provideHttpLoggingInterceptor()
            ).addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                public override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val request: Request
                    if (PreferenceData.isUserLoggedIn()) {
                        request = chain.request().newBuilder().header(
                            "Authorization",
                            ("Bearer " + PreferenceData.getDeviceToken())
                        ).method(original.method, original.body).build()
                    } else {
                        request =
                            chain.request().newBuilder().method(original.method, original.body)
                                .build()
                    }
                    return chain.proceed(request)
                }
            }).build()

        /*        ApiInterface APIInterface = new retrofit2.Retrofit.Builder().baseUrl(baseURL != null ? baseURL : APIs.BASE_URL).client(client)
                       .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                       .addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class);*/

        val APIInterface =
            retrofit2.Retrofit.Builder().baseUrl(if (baseURL != null) baseURL!! else APIs.BASE_URL)
                .addConverterFactory(GsonStringConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create<ApiInterface>(ApiInterface::class.java)

        call = APIInterface.callPostMethodMultipart(endPoint!!, params, files)
        return call
    }

    companion object {

        /**
         * @param context
         * @return Instance of this class
         * create instance of this class
         */
        fun with(context: Context): Retrofit {
            return Retrofit(context)
        }

        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            /*HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                       @Override
                       public void log(String message) {
                           Log.d("Log", message);
                       }
                   });
                   httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                   return httpLoggingInterceptor;*/
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return interceptor
        }
    }


    /* fun getUnsafeOkHttpClient(): OkHttpClient.Builder? {
         return try { // Create a trust manager that does not validate certificate chains
             val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                 object : X509TrustManager() {
                     @Throws(CertificateException::class)
                     fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                     }

                     @Throws(CertificateException::class)
                     fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                     }

                     val acceptedIssuers: Array<X509Certificate?>?
                         get() = arrayOf()
                 }
             )
             // Install the all-trusting trust manager
             val sslContext: SSLContext = SSLContext.getInstance("SSL")
             sslContext.init(null, trustAllCerts, SecureRandom())
             // Create an ssl socket factory with our all-trusting manager
             val sslSocketFactory: SSLSocketFactory = sslContext.getSocketFactory()
             val builder = OkHttpClient.Builder()
             builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
             builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
             builder
         } catch (e: Exception) {
             throw RuntimeException(e)
         }
     }*/
}
