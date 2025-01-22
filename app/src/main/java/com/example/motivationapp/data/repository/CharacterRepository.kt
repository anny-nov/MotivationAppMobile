package com.example.motivationapp.data.repository

import com.example.motivationapp.data.models.Character

class CharacterRepository {

    fun getDefaultCharacter(): Character {
        // Возвращаем персонажа по умолчанию
        return Character(
            id = 1,
            name = "Default",
            level = 1,
            experience = 0,
            health = 100,
            maxHealth = 100,
            imageUrl = null
        )
    }

    fun getCharacterById(characterId: Int): Character? {
        // Пока возвращаем null, так как серверный функционал не реализован
        return null
    }
}
