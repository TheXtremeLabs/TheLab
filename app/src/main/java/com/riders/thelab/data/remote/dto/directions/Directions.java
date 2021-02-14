package com.riders.thelab.data.remote.dto.directions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Directions {

    @Expose
    ArrayList<Route> route;

    @Expose
    String status;

    @SerializedName("overview_polyline")
    String overviewPolyline;
}
