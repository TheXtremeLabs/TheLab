package com.riders.thelab.data.remote.dto.directions;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;


public class Route {

    @Expose
    ArrayList<Legs> legs;
    @Expose
    String startName = "";
    @Expose
    String endName = "";
    @Expose
    Double startLat;
    @Expose
    Double startLng;
    @Expose
    Double endLat;
    @Expose
    Double endLng;
    @Expose
    String overviewPolyline;


}
