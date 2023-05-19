package com.riders.thelab.core.bus

class AlarmEvent {
    suspend fun triggerEvent() {
        KotlinBus.publish("Alarm data to publish")
    }
}