package com.riders.thelab.core.bus

import android.location.Location

class LocationFetchedEvent(private val location: Location) {

    fun getLocation(): Location {
        return location
    }
}