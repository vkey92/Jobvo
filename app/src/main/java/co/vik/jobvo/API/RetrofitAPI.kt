package com.example.contractapp.API



import co.vik.jobvo.API.RetrofitAPIInterface
import co.vik.jobvo.common.Common
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitAPI private constructor() {
    companion object {
        private var retrofitAPI: RetrofitAPI? = null
        lateinit var retrofit: Retrofit
        val api: RetrofitAPIInterface
            get() {
                if (retrofitAPI == null) {
                    instance
                }
                return retrofit.create(RetrofitAPIInterface::class.java)
            }
        val instance: RetrofitAPI?
            get() {
                if (retrofitAPI == null) {
                    if (retrofitAPI == null) {
                        retrofitAPI = RetrofitAPI()
                    }
                }
                return retrofitAPI
            }
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(Common.mainurl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}