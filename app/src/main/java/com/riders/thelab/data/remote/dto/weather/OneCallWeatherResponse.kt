package com.riders.thelab.data.remote.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
 * https://stackoverflow.com/questions/58501918/whats-the-use-of-moshis-kotlin-codegen
 * Right. It indicates to Moshi that there is a generated adapter that should be used.
 * It’s also used by the Moshi code gen annotation processor to indicate
 * which classes should have an adapter generated for them.
 */
@JsonClass(generateAdapter = true)
data class OneCallWeatherResponse constructor(

    @Json(name = "lat")
    val latitude: Double = 0.0,

    @Json(name = "lon")
    val longitude: Double = 0.0,

    @Json(name = "timezone")
    val timezone: String,

    @Json(name = "timezone_offset")
    val timezoneOffset: Int = 0,

    @Json(name = "current")
    val currentWeather: CurrentWeather,

    @Json(name = "hourly")
    val hourlyWeather: List<CurrentWeather>,

    @Json(name = "daily")
    val dailyWeather: List<DailyWeather>,
) {
    override fun toString(): String {
        return "OneCallWeatherResponse(latitude=$latitude, longitude=$longitude, timezone='$timezone', timezoneOffset=$timezoneOffset, currentWeather=$currentWeather, hourlyWeather=$hourlyWeather, dailyWeather=$dailyWeather)"
    }
}
