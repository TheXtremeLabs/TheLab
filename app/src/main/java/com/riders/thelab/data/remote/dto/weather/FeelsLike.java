package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeelsLike {
    @Json(name = "day")
    private double day;
    @Json(name = "night")
    private double night;
    @Json(name = "eve")
    private double evening;
    @Json(name = "morn")
    private double morning;
}
