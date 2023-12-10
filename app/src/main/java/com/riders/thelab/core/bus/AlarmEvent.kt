package com.riders.thelab.core.bus

import kotlinx.coroutines.DelicateCoroutinesApi

class AlarmEvent {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun triggerEvent() {
        KotlinBus.publish("Alarm data to publish")
    }
}