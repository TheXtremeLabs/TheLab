package com.riders.thelab.data.remote.dto.directions;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Coordinates {

    @SerializedName("lng")
    double longitude;

    @SerializedName("lat")
    double latitude;
}
