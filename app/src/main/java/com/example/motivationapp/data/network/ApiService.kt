package com.example.motivationapp.data.network

import com.example.motivationapp.data.models.Event
import com.example.motivationapp.data.models.Habit
import com.example.motivationapp.data.models.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Регистрация пользователя
    @POST("/users/")
    suspend fun register(
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<User>

    // Получить привычки пользователя
    @GET("/users/{userId}/habits")
    suspend fun getHabitsByUserId(@Path("userId") userId: Int): Response<List<Habit>>

    // Создать новую привычку
    @POST("/habits/")
    suspend fun createHabit(
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("user_id") userId: Int,
        @Query("repeat") repeat: String
    ): Response<Habit>

    // Создать событие для привычки
    @POST("/events/")
    suspend fun createEvent(
        @Query("habit_id") habitId: Int,
        @Query("execution_time") executionTime: String
    ): Response<Event>

    // Получить события для привычки
    @GET("/events/{habitId}")
    suspend fun getEventsByHabitId(@Path("habitId") habitId: Int): Response<List<Event>>
}
