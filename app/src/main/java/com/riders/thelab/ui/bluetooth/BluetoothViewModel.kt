package com.riders.thelab.ui.bluetooth

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class BluetoothViewModel : ViewModel() {

    private val isBluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData()


    fun getBluetoothEnabled(): LiveData<Boolean> {
        return isBluetoothEnabled
    }

    fun setBluetooth(context: Context, enable: Boolean) {

        /* Source : https://stackoverflow.com/questions/69122978/what-do-i-use-now-that-bluetoothadapter-getdefaultadapter-is-deprecated*/
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val isEnabled = bluetoothManager.adapter.isEnabled
        if (enable && !isEnabled) {
            isBluetoothEnabled.value = bluetoothManager.adapter.enable()
        } else if (!enable && isEnabled) {
            isBluetoothEnabled.value = bluetoothManager.adapter.disable()
        }
        // No need to change bluetooth state
        // isBluetoothEnabled.value = true
    }

    fun getBluetoothState(context: Context): Boolean {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bluetoothManager.adapter.isEnabled
    }

    fun startDiscovery(context: Context) {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        if (!bluetoothManager.adapter.isDiscovering) {
            bluetoothManager.adapter.startDiscovery()
        }
    }

    fun stopDiscovery(context: Context) {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter.cancelDiscovery()
    }
}