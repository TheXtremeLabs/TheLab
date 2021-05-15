package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
/*
 * https://stackoverflow.com/questions/58501918/whats-the-use-of-moshis-kotlin-codegen
 * Right. It indicates to Moshi that there is a generated adapter that should be used.
 * It’s also used by the Moshi code gen annotation processor to indicate
 * which classes should have an adapter generated for them.
 */
//@JsonClass(generateAdapter = true)
public class OneCallWeatherResponse {

    @Json(name = "lat")
    private double latitude;

    @Json(name = "lon")
    private double longitude;

    @Json(name = "timezone")
    private String timezone;

    @Json(name = "timezone_offset")
    private int timezoneOffset;

    @Json(name = "current")
    private CurrentWeather currentWeather;

    @Json(name = "hourly")
    private List<CurrentWeather> hourlyWeather;

    @Json(name = "daily")
    private List<DailyWeather> dailyWeather;
}
