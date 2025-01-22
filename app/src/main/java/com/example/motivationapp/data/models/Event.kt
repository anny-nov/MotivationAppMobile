package com.example.motivationapp.data.models

import java.util.*

data class Event(
    val id: Int? = null,
    val habitId: Int,
    val executionTime: Date
)