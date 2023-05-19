package com.riders.thelab.core.bus

class AlarmEvent {
    suspend fun triggerEvent() {
        KotlinBus.getInstance().publish("Alarm data to publish")
    }
}