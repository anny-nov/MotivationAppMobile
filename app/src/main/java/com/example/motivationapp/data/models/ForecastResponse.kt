package com.example.motivationapp.data.models

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("habit_id") val habitId: Int,
    @SerializedName("declining") val declining: Boolean,
    @SerializedName("alternative_1") val alternative1: String,
    @SerializedName("alternative_2") val alternative2: String,
    @SerializedName("alternative_3") val alternative3: String
)