package com.example.motivationapp.data.models

import com.google.gson.annotations.SerializedName

data class Character(
    val id: Int, // Уникальный идентификатор персонажа
    @SerializedName("user_id")
    val userId: Int,
    val name: String, // Имя персонажа
    val level: Int, // Уровень персонажа
    val experience: Int, // Опыт персонажа
    @SerializedName("max_experience")
    val maxExperience: Int,
    @SerializedName("current_health")
    val currentHealth: Int, // Здоровье
    @SerializedName("max_health")
    val maxHealth: Int, // Максимальное здоровье
    val avatar: String? = null // Ссылка на изображение аватара
)