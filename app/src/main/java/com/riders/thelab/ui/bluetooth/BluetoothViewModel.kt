package com.riders.thelab.ui.bluetooth

import android.bluetooth.BluetoothManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber


class BluetoothViewModel : ViewModel() {

    private val isBluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData()
    private val boundedDevices: MutableLiveData<ArrayList<String>> = MutableLiveData()

    fun getBluetoothEnabled(): LiveData<Boolean> {
        return isBluetoothEnabled
    }

    fun getBoundedDevices(): LiveData<ArrayList<String>> {
        return boundedDevices
    }

    fun setBluetooth(bluetoothManager: BluetoothManager, enable: Boolean) {

        val isEnabled = bluetoothManager.adapter.isEnabled
        if (enable && !isEnabled) {
            isBluetoothEnabled.value = bluetoothManager.adapter.enable()
        } else if (!enable && isEnabled) {
            isBluetoothEnabled.value = bluetoothManager.adapter.disable()
        }
        // No need to change bluetooth state
        // isBluetoothEnabled.value = true
    }

    fun getBluetoothState(bluetoothManager: BluetoothManager): Boolean {
        return bluetoothManager.adapter.isEnabled
    }

    fun startDiscovery(bluetoothManager: BluetoothManager) {
        if (!bluetoothManager.adapter.isDiscovering) {
            bluetoothManager.adapter.startDiscovery()
        }
    }

    fun stopDiscovery(bluetoothManager: BluetoothManager) {
        if (bluetoothManager.adapter.isDiscovering)
            bluetoothManager.adapter.cancelDiscovery()
    }

    fun fetchBoundedDevices(bluetoothManager: BluetoothManager) {
        val pairedDevices = bluetoothManager.adapter.bondedDevices
        if (pairedDevices.size > 0) {

            val boundedDevicesToReturn: ArrayList<String> =
                mutableListOf<String>() as ArrayList<String>

            for (d in pairedDevices) {
                val deviceName = d.name
                val macAddress = d.address
                Timber.i("paired device: $deviceName at $macAddress")
                // do what you need/want this these list items

                boundedDevicesToReturn.add(deviceName)
            }

            boundedDevices.value = boundedDevicesToReturn
        }
    }
}