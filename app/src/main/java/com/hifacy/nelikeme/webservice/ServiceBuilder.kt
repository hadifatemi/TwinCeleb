package com.hifacy.nelikeme.webservice

import android.content.Context
import com.google.gson.GsonBuilder
import com.hifacy.nelikeme.database.SharedPreference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
//import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class ServiceBuilder {

    var BASE_URL = "https://api.hifacy.com"


    var retrofit: Retrofit? = null
    private var sJsonApplicationInterceptor: Interceptor? = null
    var token: String? = null

    fun getRetrofit(needToken: Boolean,context: Context): Retrofit? {

//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient: OkHttpClient
        if (needToken) {
            readToken(context)
            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(getJsonApplicationInterceptor(needToken)!!)
                .build()
        } else {
            token = null
            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
        }

        val gson = GsonBuilder().setLenient().create()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit
    }

    //add token to header
    private fun getJsonApplicationInterceptor(needToken: Boolean): Interceptor? {
        if (sJsonApplicationInterceptor == null) {
            sJsonApplicationInterceptor = object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val CONTENT_TYPE = "Content-Type"
                    val ACCEPT = "Accept"
                    val APPLICATION_JSON = "application/json"
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .header(ACCEPT, APPLICATION_JSON)
//                        .header("Connection", "close")
                        .header("Authorization", /*.ASPXAUTH=*/"Bearer $token")
                        .method(original.method, original.body)
                        .build()
                    return chain.proceed(request)
                }
            }
        }
        return sJsonApplicationInterceptor
    }

    var sharedPreference : SharedPreference? = null

    private fun readToken(context: Context): String? {
//        val sh = context.getSharedPreferences("chanchan", Context.MODE_PRIVATE)
        sharedPreference = SharedPreference(context)
        token = sharedPreference!!.getValueString("token") /*sh.getString("token", null)*/
        return token
    }


}