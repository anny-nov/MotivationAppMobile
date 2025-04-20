package com.example.motivationapp.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Event(
    val id: Int? = null,
    val habitId: Int,
    @SerializedName("execution_time")
    val executionTime: Date
)