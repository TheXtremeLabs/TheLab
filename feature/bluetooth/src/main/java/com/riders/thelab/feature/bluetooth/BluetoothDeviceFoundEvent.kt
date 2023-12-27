package com.riders.thelab.feature.bluetooth

import android.bluetooth.BluetoothDevice
import com.riders.thelab.core.common.bus.KotlinBus
import kotlinx.coroutines.DelicateCoroutinesApi
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
object BluetoothDeviceFoundEvent {
    suspend fun triggerEvent(device: BluetoothDevice) {
        Timber.d("triggerEvent() | device: $device")
        KotlinBus.publish(device)
    }
}