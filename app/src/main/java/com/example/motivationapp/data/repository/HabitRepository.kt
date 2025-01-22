package com.example.motivationapp.data.repository

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
            repeat = habit.repeat
        )
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
