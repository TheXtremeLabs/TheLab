package com.riders.thelab.feature.schedule.bus

import com.riders.thelab.core.common.bus.KotlinBus
import kotlinx.coroutines.DelicateCoroutinesApi

class AlarmEvent {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun triggerEvent() {
        KotlinBus.publish("Alarm data to publish")
    }
}