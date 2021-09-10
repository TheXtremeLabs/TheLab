package com.riders.thelab.data.local.model.weather

import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse


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
        this.temperature = oneCallWeatherResponse.currentWeather.temperature.toInt()
        this.weatherIconUrl = oneCallWeatherResponse.currentWeather.weather[0].icon
    }


    override fun toString(): String {
        return "ProcessedWeather(city='$city', country='$country', temperature=$temperature, weatherIconUrl='$weatherIconUrl')"
    }
}
