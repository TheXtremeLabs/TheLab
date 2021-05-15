package com.riders.thelab.data.remote.dto.weather;

import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class City {

    private int id;
    private String name;
    private String state;
    private String country;

    @Json(name = "coord")
    private Coordinates coordinates;
}
