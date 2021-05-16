package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Clouds {

    @Json(name = "all")
    int cloudiness;
}
