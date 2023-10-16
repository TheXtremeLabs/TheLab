package com.riders.thelab.core.data.local.model.weather

import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse


data class ProcessedWeather(
    var city: String,
    var country: String,
    var temperature: Int,
    var weatherIconUrl: String
) {
    constructor(
        city: String,
        country: String,
        oneCallWeatherResponse: OneCallWeatherResponse
    ) : this("", "", 0, "") {
        this.city = city
        this.country = country
        /*this.temperature = oneCallWeatherResponse.currentWeather?.temperature?.toInt()!!
        this.weatherIconUrl = oneCallWeatherResponse.currentWeather.weather?.get(0)?.icon!!*/
        oneCallWeatherResponse.currentWeather?.temperature?.toInt()?.let { this.temperature = it }
        oneCallWeatherResponse.currentWeather?.weather?.get(0)?.icon?.let {
            this.weatherIconUrl = it
        }
    }
}
