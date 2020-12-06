package com.riders.thelab.data.remote.dto.weather;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class City {

    int id;
    String name;
    String state;
    String country;
    @SerializedName("coord")
    Coordinates coordinates;
}
