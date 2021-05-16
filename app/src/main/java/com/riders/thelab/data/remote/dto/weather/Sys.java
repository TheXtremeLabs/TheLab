package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Sys {
    @Json(name = "type")
    int type;

    @Json(name = "id")
    int id;

    @Json(name = "country")
    String country;

    @Json(name = "sunrise")
    long sunrise;

    @Json(name = "sunset")
    long sunset;
}
