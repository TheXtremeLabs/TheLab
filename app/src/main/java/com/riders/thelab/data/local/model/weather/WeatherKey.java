package com.riders.thelab.data.local.model.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class WeatherKey {

    @Json(name = "appid")
    private String appID;
}
