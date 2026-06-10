package com.riders.thelab.core.data.local.model.weather

import android.location.Address
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.remote.dto.weather.CurrentWeather
import com.riders.thelab.core.data.remote.dto.weather.DailyWeather
import com.riders.thelab.core.data.remote.dto.weather.FeelsLike
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import kotlinx.serialization.Contextual
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
data class WeatherModel(
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

    val temperature: TemperatureModel? = null,
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

    val hourlyWeather: List<WeatherModel>? = null,
    val dailyWeather: List<WeatherModel>? = null,

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
}