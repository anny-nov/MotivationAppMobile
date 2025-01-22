package com.example.motivationapp.data.models

data class Character(
    val id: Int, // Уникальный идентификатор персонажа
    val name: String, // Имя персонажа
    val level: Int, // Уровень персонажа
    val experience: Int, // Опыт персонажа
    val health: Int, // Здоровье
    val maxHealth: Int, // Максимальное здоровье
    val imageUrl: String? = null // Ссылка на изображение аватара
)