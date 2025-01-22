package com.example.motivationapp.data.models
import java.time.LocalDateTime

data class Habit(
    val id: Int? = null,             // Primary Key
    val userId: Int,                 // Foreign Key
    val name: String,                // Название привычки
    val description: String? = null, // Описание (опционально)
    val repeat: String,              // Тип повторения (Enum на сервере, String на клиенте)
    val createAt: String? = null // Время создания (может быть null, если сервер его задает)
)
