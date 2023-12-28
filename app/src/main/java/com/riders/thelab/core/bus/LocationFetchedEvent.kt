package com.riders.thelab.core.bus

import android.location.Location
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
class LocationProviderChangedEvent {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun triggerEvent(enabled: Boolean) {
        Timber.d("triggerEvent() | enabled: $enabled")
//        KotlinBus.getInstance().publish(enabled)
        KotlinBus.publish(enabled)
    }
}

class LocationFetchedEvent(val location: Location) {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun triggerEvent() {
        Timber.d("triggerEvent() | location: $location")
//        KotlinBus.getInstance().publish(location)
        KotlinBus.publish(location)
    }
}