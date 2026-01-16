package com.riders.thelab.feature.flightaware.data.local.model

import android.location.Location
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.flight.AirportModel

@Stable
@Immutable
data class LocationModel(val latitude: Double, val longitude: Double) {

    @Stable
    val location: Location
        get() = Location("").apply {
            latitude = this@LocationModel.latitude
            longitude = this@LocationModel.longitude
        }
}


fun AirportModel.toLocationModel() = LocationModel(latitude = latitude!!, longitude = longitude!!)