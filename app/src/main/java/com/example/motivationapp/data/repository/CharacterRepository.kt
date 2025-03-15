package com.example.motivationapp.data.repository

import android.util.Log
import com.example.motivationapp.data.models.Character as AppCharacter
import com.example.motivationapp.data.network.ApiService

class CharacterRepository(private val apiService: ApiService) {

    // Создать персонажа для пользователя
    suspend fun createCharacter(userId: Int, name: String): com.example.motivationapp.data.models.Character? {
        val response = apiService.createCharacter(userId, name)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    // Получить персонажа по userId
    suspend fun getCharacterByUserId(userId: Int): com.example.motivationapp.data.models.Character? {
        val response = apiService.getCharacterByUserId(userId)

        Log.d("CharacterRepository", "Response code: ${response.code()}")
        Log.d("CharacterRepository", "Response body: ${response.body()}")

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}

