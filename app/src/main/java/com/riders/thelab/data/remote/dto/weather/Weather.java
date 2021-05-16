package com.riders.thelab.data.remote.dto.weather;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Weather {
    @Json(name = "id")
    private int id;

    @Json(name = "main")
    private String main;

    @Json(name = "description")
    private String description;

    @Json(name = "icon")
    private String icon;
}
