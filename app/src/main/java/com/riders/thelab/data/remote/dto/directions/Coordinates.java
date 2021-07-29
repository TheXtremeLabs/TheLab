package com.riders.thelab.data.remote.dto.directions;

import com.google.gson.annotations.SerializedName;


public class Coordinates {

    @SerializedName("lng")
    double longitude;

    @SerializedName("lat")
    double latitude;
}
