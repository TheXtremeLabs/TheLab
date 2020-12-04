package com.riders.thelab.data.local.bean;


/**
 * Distance values using camera zoom for the map.
 * According to Android Maps SDK Website : https://developers.google.com/maps/documentation/android-sdk/views
 */
public enum MapsEnum {

    WORLD(1.0f),
    LANDMASS(5.0f),
    CITY(10.0f),
    STREETS(15.0f),
    BUILDINGS(20.0f),

    DEFAULT_MAX_ZOOM(19.0f);

    private final float distance;

    MapsEnum(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }
}
