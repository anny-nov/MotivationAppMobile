package com.example.motivationapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8000/"

    // Настройка Gson с обработкой даты
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // Формат ISO-8601
        .create()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Используем настроенный Gson
            .build()
            .create(ApiService::class.java)
    }
}
