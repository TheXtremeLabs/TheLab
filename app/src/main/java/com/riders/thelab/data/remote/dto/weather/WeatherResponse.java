package com.riders.thelab.data.remote.dto.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherResponse {

    @SerializedName("coord")
    Coordinates coordinates;

    @Expose
    List<Weather> weather;

    @Expose
    String base;

    @Expose
    Main main;

    @Expose
    int visibility;

    @Expose
    Rain rain;

    @Expose
    Wind wind;

    @Expose
    Clouds clouds;

    @Expose
    long dt;

    @SerializedName("sys")
    Sys system;

    @Expose
    int timezone;

    @Expose
    int id;

    @Expose
    String name;

    @SerializedName("cod")
    @Expose
    int code;
}
