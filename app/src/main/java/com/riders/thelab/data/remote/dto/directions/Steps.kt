package com.riders.thelab.data.remote.dto.directions;

import com.google.gson.annotations.SerializedName;


public class Steps {

    @SerializedName(value = "travel_mode")
    String travelMode;

    @SerializedName(value = "start_location")
    Coordinates startLocation;

    @SerializedName(value = "end_location")
    Coordinates endLocation;
}
