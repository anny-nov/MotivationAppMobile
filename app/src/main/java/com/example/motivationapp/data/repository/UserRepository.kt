package com.example.motivationapp.data.repository

import com.example.motivationapp.data.models.User
import com.example.motivationapp.data.network.ApiService

class UserRepository(private val apiService: ApiService) {

    suspend fun registerUser(user: User): User? {
        val response = apiService.register(
            username = user.username,
            email = user.email,
            password = user.password
        )
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getUserById(userId: Int): User? {
        // Этот метод можно реализовать позже, если сервер поддерживает эндпоинт
        return null
    }
}
