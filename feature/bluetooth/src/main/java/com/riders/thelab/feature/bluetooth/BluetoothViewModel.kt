package com.riders.thelab.feature.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber


@SuppressLint("MissingPermission")
class BluetoothViewModel : BaseViewModel() {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var bluetoothManager: BluetoothManager? = null
    private lateinit var bluetoothDevicesSearchList: ArrayList<String>

    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////
    private var _isBluetoothEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled.asStateFlow()

    private var _boundedDevices: MutableStateFlow<Set<BluetoothDevice>> =
        MutableStateFlow(emptySet())
    val boundedDevices: StateFlow<Set<BluetoothDevice>> = _boundedDevices.asStateFlow()
    private var _availableDevices: MutableStateFlow<Set<BluetoothDevice>> =
        MutableStateFlow(emptySet())
    val availableDevices: StateFlow<Set<BluetoothDevice>> = _availableDevices.asStateFlow()

    var bluetoothSearchProgress: Boolean by mutableStateOf(false)
        private set
    var isSearching: Boolean by mutableStateOf(false)
        private set


    fun updateBluetoothEnabled(enabled: Boolean) {
        this._isBluetoothEnabled.value = enabled
    }

    fun updateBluetoothBoundedDevices(boundedDevices: Set<BluetoothDevice>) {
        this._boundedDevices.value = boundedDevices
    }

    fun updateBluetoothAvailableDevices(availableDevices: Set<BluetoothDevice>) {
        this._availableDevices.value = availableDevices
    }

    fun addNewBluetoothAvailableDevices(newDevice: BluetoothDevice) {
        Timber.d("addNewBluetoothAvailableDevices()")

        val mutableSetBluetoothDevices: MutableSet<BluetoothDevice> =
            this._availableDevices.value.toMutableSet()
        mutableSetBluetoothDevices.add(newDevice)
        this._availableDevices.value = mutableSetBluetoothDevices
    }

    fun updateBluetoothSearchProgress(visible: Boolean) {
        this.bluetoothSearchProgress = visible
    }

    fun updateIsBluetoothSearching(searching: Boolean) {
        this.isSearching = searching
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    init {
        Timber.d("init method")
        bluetoothManager?.let {
            updateBluetoothEnabled(it.adapter.isEnabled)
        } ?: run { Timber.e("Bluetooth Manager is null") }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    fun initBluetoothManager(bluetoothActivity: BluetoothActivity) {
        Timber.d("initBluetoothManager()")
        /* Source : https://stackoverflow.com/questions/69122978/what-do-i-use-now-that-bluetoothadapter-getdefaultadapter-is-deprecated*/
        bluetoothManager =
            bluetoothActivity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        bluetoothManager?.let {
            updateBluetoothEnabled(it.adapter.isEnabled)
        } ?: run { Timber.e("Bluetooth Manager is null") }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    fun setBluetooth(enable: Boolean) {

        bluetoothManager?.let {
            val isEnabled = it.adapter.isEnabled
            if (enable && !isEnabled) {
                updateBluetoothEnabled(it.adapter.enable())
            } else if (!enable && isEnabled) {
                updateBluetoothEnabled(it.adapter.disable())
            }
            // No need to change bluetooth state
            // isBluetoothEnabled.value = true
        }
    }

    fun startDiscovery() {
        Timber.d("startDiscovery()")
        if (bluetoothManager?.adapter?.isDiscovering == false) {

            if (_availableDevices.value.isNotEmpty()) {
                _availableDevices.value = emptySet()
            }

            bluetoothManager?.adapter?.startDiscovery()?.let { updateIsBluetoothSearching(it) }
        }
    }

    fun stopDiscovery() {
        Timber.e("stopDiscovery()")
        if (bluetoothManager?.adapter?.isDiscovering == true)
            bluetoothManager?.adapter?.cancelDiscovery()?.let { updateIsBluetoothSearching(it) }
    }

    fun fetchBoundedDevices() {
        Timber.d("fetchBoundedDevices()")
        bluetoothManager?.let {
            val pairedDevices: Set<BluetoothDevice> = it.adapter.bondedDevices

            if (pairedDevices.isNotEmpty()) {
                val boundedDevicesToReturn: ArrayList<String> =
                    mutableListOf<String>() as ArrayList<String>

                for (d in pairedDevices) {
                    val deviceName = d.name
                    val macAddress = d.address
                    Timber.i("paired device: $deviceName at $macAddress")
                    // do what you need/want this these list items

                    boundedDevicesToReturn.add(deviceName)
                }

                updateBluetoothBoundedDevices(pairedDevices)
            }
        }
    }

}