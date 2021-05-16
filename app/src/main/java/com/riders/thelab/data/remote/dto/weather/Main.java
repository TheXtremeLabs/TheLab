package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Main {

    @Json(name = "temp")
    double temperature;

    @Json(name = "feels_like")
    double feelsLike;

    @Json(name = "temp_min")
    double minTemperature;

    @Json(name = "temp_max")
    double maxTemperature;

    @Json(name = "pressure")
    int pressure;

    @Json(name = "humidity")
    int humidity;
}
