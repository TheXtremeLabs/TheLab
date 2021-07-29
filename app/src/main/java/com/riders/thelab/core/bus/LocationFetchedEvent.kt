package com.riders.thelab.core.bus

import android.location.Location

class LocationFetchedEvent(val location: Location) {

    @JvmName("getLocationFromFetchedEvent")
    fun getLocation(): Location {
        return location
    }
}