package com.example.smartnote.core.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private val BASE_URL = "10.0.2.2:3000"  //
    private val HTTP_URL = "http://$BASE_URL/"
    val WS_URL = "ws://$BASE_URL/"

    private var gson = GsonBuilder().create()

    val tokenInterceptor = TokenInterceptor()

    val retrofit = Retrofit.Builder()
        .baseUrl(HTTP_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val okHttpClient = OkHttpClient.Builder()
        .build()
}
