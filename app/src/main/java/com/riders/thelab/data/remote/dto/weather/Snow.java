package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Snow {

    // Rain volume for the last 1 hour, mm
    @Json(name = "1h")
    double lastHour;

    // Rain volume for the last 3 hour, mm
    @Json(name = "3h")
    double lastThreeHour;
}
