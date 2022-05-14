package com.bytedance.jstu.homework

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetrofit(): Retrofit {

    return Retrofit.Builder()
        .baseUrl("https://bd-open-lesson.bytedance.com/api/invoke/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}