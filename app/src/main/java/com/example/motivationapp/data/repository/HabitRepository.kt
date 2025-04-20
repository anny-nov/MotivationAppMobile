package com.example.motivationapp.data.repository

import android.util.Log
import com.example.motivationapp.data.models.ForecastResponse
import com.example.motivationapp.data.models.Habit
import com.example.motivationapp.data.network.ApiService

class HabitRepository(private val apiService: ApiService) {

    // Получить привычки по userId
    suspend fun getHabitsByUserId(userId: Int): List<Habit> {
        val response = apiService.getHabitsByUserId(userId)
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Создать новую привычку
    suspend fun createHabit(habit: Habit): Habit? {
        val response = apiService.createHabit(
            name = habit.name,
            description = habit.description ?: "",
            userId = habit.userId,
            repeat = habit.repeat,
            difficulty = habit.difficulty
        )
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    // Получить прогноз по привычке
    suspend fun getForecast(habitId: Int): ForecastResponse? {
        val response = apiService.getForecast(habitId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    // Удалить привычку
    suspend fun deleteHabit(habitId: Int) {
        apiService.deleteHabit(habitId)
    }
}

