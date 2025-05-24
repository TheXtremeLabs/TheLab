package com.riders.thelab.feature.weather.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.weather.WeatherDataState
import com.riders.thelab.core.data.local.model.compose.weather.WeatherUIState
import com.riders.thelab.core.data.local.model.weather.TemperatureModel
import com.riders.thelab.core.data.local.model.weather.WeatherModel

private val mockWeather: WeatherModel = WeatherModel(
    city = "Paris",
    country = "France",
    dateTimeUTC = 1684160601L,
    timezone = "Europe",
    latitude = 48.8534,
    longitude = 2.3486,
    sunrise = 1684123772L,
    sunset = 1684178672,
    temperature = TemperatureModel(
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
        WeatherModel(
            mainWeather = "Rain",
            description = "light rain",
            weatherIconUrl = "10d",
            temperature = TemperatureModel(
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
        WeatherModel(
            mainWeather = "Rain",
            description = "light rain",
            weatherIconUrl = "10d",
            temperature = TemperatureModel(
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
        WeatherModel(
            mainWeather = "Rain",
            description = "light rain",
            weatherIconUrl = "10d",
            temperature = TemperatureModel(
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
        WeatherModel(
            mainWeather = "Rain",
            description = "light rain",
            weatherIconUrl = "10d",
            temperature = TemperatureModel(
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
        WeatherModel(
            mainWeather = "Rain",
            description = "light rain",
            weatherIconUrl = "10d",
            temperature = TemperatureModel(
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
        WeatherModel(
            mainWeather = "Rain",
            description = "light rain",
            weatherIconUrl = "10d",
            temperature = TemperatureModel(
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


class PreviewProviderWeather : PreviewParameterProvider<WeatherModel> {
    override val values: Sequence<WeatherModel> get() = sequenceOf(mockWeather)
}

class PreviewProviderWeatherDataState : PreviewParameterProvider<WeatherDataState> {
    override val values: Sequence<WeatherDataState>
        get() = sequenceOf(
            WeatherDataState.Loading,
            WeatherDataState.Error(Exception("Error while downloading weather data")),
            WeatherDataState.SuccessWeatherData(true),
            WeatherDataState.SuccessWeatherData(false),
        )
}

class PreviewProviderWeatherUIState : PreviewParameterProvider<WeatherUIState> {
    override val values: Sequence<WeatherUIState>
        get() = sequenceOf(
            WeatherUIState.None,
            WeatherUIState.Success(mockWeather),
            WeatherUIState.Error(),
        )
}