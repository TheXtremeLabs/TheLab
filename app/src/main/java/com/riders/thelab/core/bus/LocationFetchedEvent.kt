package com.riders.thelab.core.bus

import android.location.Location
import timber.log.Timber

class LocationProviderChangedEvent() {
    suspend fun triggerEvent(enabled: Boolean) {
        Timber.d("triggerEvent() | enabled: $enabled")
        KotlinBus.getInstance().publish(enabled)
    }
}

class LocationFetchedEvent(val location: Location) {
    suspend fun triggerEvent() {
        Timber.d("triggerEvent() | location: $location")
        KotlinBus.getInstance().publish(location)
    }
}