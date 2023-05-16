package com.riders.thelab.core.bus

import android.location.Location
import timber.log.Timber

class LocationFetchedEvent(val location: Location) {
    suspend fun triggerEvent() {
        Timber.d("triggerEvent() | location: $location")
        KotlinBus.getInstance().publish(location)
    }
}