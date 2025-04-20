package com.example.motivationapp.data.repository

import android.util.Log
import com.example.motivationapp.data.models.Event
import com.example.motivationapp.data.network.ApiService
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class EventRepository(private val apiService: ApiService) {

    // Создать новое событие для привычки
    suspend fun createEvent(event: Event): Event? {
        // Форматируем дату в ISO 8601
        val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        iso8601Format.timeZone = TimeZone.getTimeZone("UTC") // Убедимся, что формат UTC
        val executionTimeIso = iso8601Format.format(event.executionTime)

        val response = apiService.createEvent(
            habitId = event.habitId,
            executionTime = executionTimeIso
        )
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    // Получить события по habitId
    suspend fun getEventsByHabitId(habitId: Int): List<Event> {
        val response = apiService.getEventsByHabitId(habitId)
        Log.d("EventRepository", "Response body: ${response.body()}")
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
}

