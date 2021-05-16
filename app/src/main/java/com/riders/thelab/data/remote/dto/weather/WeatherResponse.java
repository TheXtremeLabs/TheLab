package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherResponse {

    @Json(name = "coord")
    Coordinates coordinates;

    @Json(name = "weather")
    List<Weather> weather;

    @Json(name = "base")
    String base;

    @Json(name = "main")
    Main main;

    @Json(name = "visibility")
    int visibility;

    @Json(name = "rain")
    Rain rain;

    @Json(name = "wind")
    Wind wind;

    @Json(name = "clouds")
    Clouds clouds;

    @Json(name = "dt")
    long dt;

    @Json(name = "sys")
    Sys system;

    @Json(name = "timezone")
    int timezone;

    @Json(name = "id")
    int id;

    @Json(name = "name")
    String name;

    @Json(name = "cod")
    int code;
}
