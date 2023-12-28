package com.riders.thelab.core.data.local.model.weather

data class CityWeather(
    val cityName: String,
    val cityCountry: String,
    val cityTemperature: Double,
    val weatherIconURL: String,
    val cityWeatherDescription: String
)
