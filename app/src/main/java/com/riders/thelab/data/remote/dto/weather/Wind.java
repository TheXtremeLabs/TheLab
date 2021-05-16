package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Wind {
    @Json(name = "1h")
    double speed;

    @Json(name = "deg")
    int degree;

    @Json(name = "gust")
    String metrics;
}
