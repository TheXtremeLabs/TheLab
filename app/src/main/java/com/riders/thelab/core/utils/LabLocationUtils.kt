package com.riders.thelab.core.utils

import android.location.Location

class LabLocationUtils private constructor() {
    companion object {
        fun buildTargetLocationObject(latitude: Double, longitude: Double): Location {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude
            return location
        }
    }
}