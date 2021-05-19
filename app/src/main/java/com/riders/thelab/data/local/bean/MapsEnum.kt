package com.riders.thelab.data.local.bean

enum class MapsEnum(val distance: Float) {
    WORLD(1.0f),
    LANDMASS(5.0f),
    CITY(10.0f),
    STREETS(15.0f),
    BUILDINGS(20.0f),

    DEFAULT_MAX_ZOOM(19.0f);
}