package com.example.motivationapp

import android.app.Application
import com.example.motivationapp.data.network.ApiService
import com.example.motivationapp.data.repository.CharacterRepository
import com.example.motivationapp.data.repository.EventRepository
import com.example.motivationapp.data.repository.HabitRepository
import com.example.motivationapp.data.repository.UserRepository
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Base URL of the backend server
const val BASE_URL = "http://10.0.2.2:8000/" // Адрес для эмулятора Android (localhost)

// App-level dependency container
class HabitApp : Application() {

    lateinit var apiService: ApiService
    lateinit var userRepository: UserRepository
    lateinit var habitRepository: HabitRepository
    lateinit var characterRepository: CharacterRepository
    lateinit var eventRepository: EventRepository

    override fun onCreate() {
        super.onCreate()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS") // формат сервера
            .create()

        // Initialize Retrofit
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // Create API service
        apiService = retrofit.create(ApiService::class.java)

        // Initialize repositories
        userRepository = UserRepository(apiService)
        habitRepository = HabitRepository(apiService)
        eventRepository = EventRepository(apiService)
        characterRepository = CharacterRepository(apiService)
    }
}
