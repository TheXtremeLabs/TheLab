package com.riders.thelab.core.domain.model.weather

import android.location.Address
import kotlinx.serialization.Contextual
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Weather(
    val city: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val country: String? = null,
    val mainWeather: String? = null,
    val description: String? = null,
    var weatherIconUrl: String? = null,

    val dateTimeUTC: Long = 0L,
    val timezone: String? = null,
    val timezoneOffset: Int = 0,

    val sunrise: Long = 0L,
    val sunset: Long = 0L,
    val moonrise: Long = 0L,
    val moonset: Long = 0L,
    val moonPhase: Double = 0.0,

    val temperature: Temperature? = null,
    val feelsLike: FeelsLike? = null,

    val pressure: Int = 0,
    val humidity: Int = 0,

    val dewPoint: Double? = null,
    val uvIndex: Double? = null,

    val clouds: Int = 0,
    // Average visibility, metres
    val visibility: Int = 0,

    val windSpeed: Double = 0.0,
    val windDegree: Int = 0,
    val windGust: Double = 0.0,

    val hourlyWeather: List<Weather>? = null,
    val dailyWeather: List<Weather>? = null,

    // Rain
    val rain: Double? = 0.0,
    val rainLastHour: Double? = null,
    val rainLastThreeHour: Double? = null,

    // Snow
    val snow: Double? = 0.0,
    val snowLastHour: Double? = null,
    val snowLastThreeHour: Double? = null,

    var probabilityOfPrecipitation: Double? = null
) : Serializable {

    var sunriseAsString: String? = null
    var sunsetAsString: String? = null

    @Contextual
    var address: Address? = null

    companion object {
        val mockWeather: Weather = Weather(
            city = "Paris",
            country = "France",
            dateTimeUTC = 1684160601L,
            timezone = "Europe",
            latitude = 48.8534,
            longitude = 2.3486,
            sunrise = 1684123772L,
            sunset = 1684178672,
            temperature = Temperature(
                temperature = 15.75,
                realFeels = 15.39,
                day = 16.0,
                min = 3.0,
                max = 23.0
            ),
            mainWeather = "Rain",
            description = "light rain",
            weatherIconUrl = "10d",
            rain = 2.0,
            pressure = 22,
            humidity = 2,
            clouds = 0,
            windSpeed = 30.0,
            windDegree = 24,
            hourlyWeather = listOf(
                Weather(
                    mainWeather = "Rain",
                    description = "light rain",
                    weatherIconUrl = "10d",
                    temperature = Temperature(
                        temperature = 9.75,
                        realFeels = 15.39,
                        day = 16.0,
                        min = 3.0,
                        max = 23.0
                    ),
                    pressure = 22,
                    humidity = 2,
                    clouds = 0,
                    windSpeed = 11.0,
                    windDegree = 2,
                ),
                Weather(
                    mainWeather = "Rain",
                    description = "light rain",
                    weatherIconUrl = "10d",
                    temperature = Temperature(
                        temperature = 5.75,
                        realFeels = 15.39,
                        day = 16.0,
                        min = 3.0,
                        max = 23.0
                    ),
                    pressure = 22,
                    humidity = 2,
                    clouds = 0,
                    windSpeed = 26.0,
                    windDegree = 45,
                ),
                Weather(
                    mainWeather = "Rain",
                    description = "light rain",
                    weatherIconUrl = "10d",
                    temperature = Temperature(
                        temperature = 15.75,
                        realFeels = 15.39,
                        day = 16.0,
                        min = 3.0,
                        max = 23.0
                    ),
                    pressure = 22,
                    humidity = 2,
                    clouds = 0,
                    windSpeed = 30.0,
                    windDegree = 180,
                ),
            ),
            dailyWeather = listOf(
                Weather(
                    mainWeather = "Rain",
                    description = "light rain",
                    weatherIconUrl = "10d",
                    temperature = Temperature(
                        temperature = 9.75,
                        realFeels = 15.39,
                        day = 16.0,
                        min = 3.0,
                        max = 23.0
                    ),
                    pressure = 22,
                    humidity = 2,
                    clouds = 0,
                    windSpeed = 0.0,
                    windDegree = 0,
                ),
                Weather(
                    mainWeather = "Rain",
                    description = "light rain",
                    weatherIconUrl = "10d",
                    temperature = Temperature(
                        temperature = 5.75,
                        realFeels = 15.39,
                        day = 16.0,
                        min = 3.0,
                        max = 23.0
                    ),
                    pressure = 22,
                    humidity = 2,
                    clouds = 0,
                    windSpeed = 5.0,
                    windDegree = 11,
                ),
                Weather(
                    mainWeather = "Rain",
                    description = "light rain",
                    weatherIconUrl = "10d",
                    temperature = Temperature(
                        temperature = 15.75,
                        realFeels = 15.39,
                        day = 16.0,
                        min = 3.0,
                        max = 23.0
                    ),
                    pressure = 22,
                    humidity = 2,
                    clouds = 1,
                    windSpeed = 97.0,
                    windDegree = 4,
                ),
            ),
        )
    }

}