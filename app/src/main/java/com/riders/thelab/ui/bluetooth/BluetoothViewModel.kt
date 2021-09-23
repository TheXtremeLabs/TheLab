package com.riders.thelab.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class BluetoothViewModel : ViewModel() {

    private val isBluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData()


    fun getBluetoothEnabled(): LiveData<Boolean> {
        return isBluetoothEnabled
    }

    fun setBluetooth(enable: Boolean) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val isEnabled = bluetoothAdapter.isEnabled
        if (enable && !isEnabled) {
            isBluetoothEnabled.value = bluetoothAdapter.enable()
        } else if (!enable && isEnabled) {
            isBluetoothEnabled.value = bluetoothAdapter.disable()
        }
        // No need to change bluetooth state
        // isBluetoothEnabled.value = true
    }

    fun getBluetoothState(): Boolean {
        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter.isEnabled
    }

    fun startDiscovery() {
        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.startDiscovery()
        }
    }

    fun stopDiscovery() {
        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter.cancelDiscovery()
    }
}