package com.riders.thelab.data.local.model.weather;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Coordinates {

    @SerializedName("lon")
    double longitude;

    @SerializedName("lat")
    double latitude;
}
