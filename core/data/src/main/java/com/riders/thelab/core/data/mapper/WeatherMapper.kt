package com.riders.thelab.core.data.mapper

import com.riders.thelab.core.data.local.model.weather.TemperatureModel
import com.riders.thelab.core.data.local.model.weather.WeatherModel
import com.riders.thelab.core.data.remote.dto.weather.CurrentWeather
import com.riders.thelab.core.data.remote.dto.weather.DailyWeather
import com.riders.thelab.core.data.remote.dto.weather.FeelsLike
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.core.data.remote.dto.weather.Temperature
import com.riders.thelab.core.domain.model.weather.Weather


////////////////////////////////////////////////
// --- Model
////////////////////////////////////////////////
fun Temperature.toModel(): TemperatureModel = TemperatureModel(
    day = this.day,
    min = this.min,
    max = this.max,
    night = this.night,
    evening = this.evening,
    morning = this.morning,
)

fun CurrentWeather.toModel(): WeatherModel = WeatherModel(
    // Current Weather
    dateTimeUTC = this.dateTimeUTC,
    sunrise = this.sunrise,
    sunset = this.sunset,
    moonrise = this.moonrise,
    moonset = this.moonset,
    moonPhase = this.moonPhase,
    temperature = TemperatureModel(temperature = this.temperature),
    feelsLike = FeelsLike(day = this.feelsLike),
    pressure = this.pressure,
    humidity = this.humidity,
    dewPoint = this.dewPoint,
    uvIndex = this.uvIndex,
    clouds = this.clouds,
    visibility = this.visibility,
    windSpeed = this.windSpeed,
    windDegree = this.windDegree,
    windGust = this.windGust,
    mainWeather = this.weather?.get(0)?.main!!,
    description = this.weather[0].description,
    weatherIconUrl = this.weather[0].icon,
    rainLastHour = this.rain?.lastHour,
    rainLastThreeHour = this.rain?.lastThreeHour,
    snowLastHour = this.snow?.lastHour,
    snowLastThreeHour = this.snow?.lastThreeHour,
    probabilityOfPrecipitation = this.pop
)

fun DailyWeather.toModel(): WeatherModel = WeatherModel(
    // Current Weather
    dateTimeUTC = this.dateTimeUTC,
    sunrise = this.sunrise,
    sunset = this.sunset,
    moonrise = this.moonrise,
    moonset = this.moonset,
    moonPhase = this.moonPhase,
    temperature = this.temperature.toModel(),
    feelsLike = this.feelsLike,
    pressure = this.pressure,
    humidity = this.humidity,
    dewPoint = this.dewPoint,
    uvIndex = this.uvIndex,
    clouds = this.clouds,
    visibility = this.visibility,
    windSpeed = this.windSpeed,
    windDegree = this.windDegree,
    windGust = this.windGust,
    mainWeather = this.weather[0].main,
    description = this.weather[0].description,
    weatherIconUrl = this.weather[0].icon,
    rain = this.rain,
    snow = this.snow,
    probabilityOfPrecipitation = this.pop
)

fun OneCallWeatherResponse.toModel(): WeatherModel {
    val hourlies = this.hourlyWeather?.map { hourlyWeatherItem ->
        hourlyWeatherItem.toModel()
    }

    val dailies = this.dailyWeather?.map { dailyWeatherItem ->
        dailyWeatherItem.toModel()
    }

    val currentWeather: WeatherModel = WeatherModel(
        latitude = this.latitude,
        longitude = this.longitude,
        timezone = this.timezone,
        timezoneOffset = this.timezoneOffset,
        // Current Weather
        dateTimeUTC = this.currentWeather?.dateTimeUTC!!,
        sunrise = this.currentWeather.sunrise,
        sunset = this.currentWeather.sunset,
        moonrise = this.currentWeather.moonrise,
        moonset = this.currentWeather.moonset,
        moonPhase = this.currentWeather.moonPhase,
        temperature = TemperatureModel(temperature = this.currentWeather.temperature),
        feelsLike = FeelsLike(day = this.currentWeather.feelsLike),
        pressure = this.currentWeather.pressure,
        humidity = this.currentWeather.humidity,
        dewPoint = this.currentWeather.dewPoint,
        uvIndex = this.currentWeather.uvIndex,
        clouds = this.currentWeather.clouds,
        visibility = this.currentWeather.visibility,
        windSpeed = this.currentWeather.windSpeed,
        windDegree = this.currentWeather.windDegree,
        windGust = this.currentWeather.windGust,
        mainWeather = this.currentWeather.weather?.get(0)?.main!!,
        description = this.currentWeather.weather[0].description,
        weatherIconUrl = this.currentWeather.weather[0].icon,
        hourlyWeather = hourlies,
        dailyWeather = dailies,
        rainLastHour = this.currentWeather.rain?.lastHour,
        rainLastThreeHour = this.currentWeather.rain?.lastThreeHour,
        snowLastHour = this.currentWeather.snow?.lastHour,
        snowLastThreeHour = this.currentWeather.snow?.lastThreeHour,
        probabilityOfPrecipitation = this.currentWeather.pop
    )

    return currentWeather
}

////////////////////////////////////////////////
// --- DTO
////////////////////////////////////////////////


////////////////////////////////////////////////
// --- Domain
////////////////////////////////////////////////
fun Temperature.toTemperatureDomainModel(): com.riders.thelab.core.domain.model.weather.Temperature =
    com.riders.thelab.core.domain.model.weather.Temperature(
        day = this.day,
        min = this.min,
        max = this.max,
        night = this.night,
        evening = this.evening,
        morning = this.morning
    )

fun FeelsLike.toFeelsLikeDomainModel(): com.riders.thelab.core.domain.model.weather.FeelsLike =
    com.riders.thelab.core.domain.model.weather.FeelsLike(
        day = this.day,
        night = this.night,
        evening = this.evening,
        morning = this.morning
    )

fun OneCallWeatherResponse.toDomainModel(): Weather = Weather(
    city = this.timezone,
    latitude = this.latitude,
    longitude = this.longitude,
    country = this.timezone,
    mainWeather = this.currentWeather?.weather?.get(0)?.main!!,
    description = this.currentWeather.weather[0].description,
    weatherIconUrl = this.currentWeather.weather[0].icon,
    dateTimeUTC = this.currentWeather.dateTimeUTC,
    timezone = this.timezone,
    timezoneOffset = this.timezoneOffset,
    sunrise = this.currentWeather.sunrise,
    sunset = this.currentWeather.sunset,
    moonrise = this.currentWeather.moonrise,
    moonset = this.currentWeather.moonset,
    moonPhase = this.currentWeather.moonPhase,
    temperature = com.riders.thelab.core.domain.model.weather.Temperature(temperature = this.currentWeather.temperature),
    feelsLike = com.riders.thelab.core.domain.model.weather.FeelsLike(feelsLike = this.currentWeather.feelsLike),
    clouds = this.currentWeather.clouds,
    visibility = this.currentWeather.visibility,
    pressure = this.currentWeather.pressure,
    humidity = this.currentWeather.humidity,
    windSpeed = this.currentWeather.windSpeed,
    windDegree = this.currentWeather.windDegree,
    windGust = this.currentWeather.windGust,
    rain = this.currentWeather.rain?.lastHour,
    rainLastHour = this.currentWeather.rain?.lastHour,
    rainLastThreeHour = this.currentWeather.rain?.lastThreeHour,
    probabilityOfPrecipitation = this.currentWeather.pop
)

