package com.riders.thelab.core.data.local.model.weather

data class ProcessedWeather(
    val city: String,
    val country: String,
    val temperature: Int,
    val weatherIconUrl: String
)