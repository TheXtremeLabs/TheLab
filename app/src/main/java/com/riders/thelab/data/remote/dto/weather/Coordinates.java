package com.riders.thelab.data.remote.dto.weather;

import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Coordinates {

    @SerializedName("lon")
    @Json(name = "lon")
    double longitude;

    @SerializedName("lat")
    @Json(name = "lat")
    double latitude;
}
