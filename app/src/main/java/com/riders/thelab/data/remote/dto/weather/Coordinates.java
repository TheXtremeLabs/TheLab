package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Coordinates {

    @Json(name = "lon")
    double longitude;

    @Json(name = "lat")
    double latitude;
}
