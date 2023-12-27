package com.riders.thelab.feature.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.ui.compose.utils.findActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class LabBluetoothReceiver : BroadcastReceiver() {
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NewApi", "MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive() | context: $context, intent: $intent")

        if (null == context) {
            Timber.e("context is null")
        } else {
            context?.let {
                when (intent?.action.toString()) {
                    BluetoothDevice.ACTION_FOUND -> {
                        Timber.e("BluetoothDevice.ACTION_FOUND")
                        // Discovery has found a device. Get the BluetoothDevice
                        // object and its info from the Intent.
                        val bluetoothDevice: BluetoothDevice? =
                            if (!LabCompatibilityManager.isTiramisu()) {
                                intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                            } else {
                                intent?.getParcelableExtra(
                                    BluetoothDevice.EXTRA_DEVICE,
                                    BluetoothDevice::class.java
                                )
                            }

                        bluetoothDevice?.let { device ->
                            val deviceName = device.name
                            val deviceHardwareAddress = device.address // MAC address

                            Timber.e("name : $deviceName, hardware : $deviceHardwareAddress")

                            /*(it.findActivity() as BluetoothActivity).addNewBluetoothAvailableDevices(
                                device
                            )*/

                            GlobalScope.launch {
                                BluetoothDeviceFoundEvent.triggerEvent(device)
                            }

                        } ?: run { Timber.e("Device object is null") }
                    }

                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        when (
                            intent?.getIntExtra(
                                BluetoothAdapter.EXTRA_STATE,
                                BluetoothAdapter.ERROR
                            )
                        ) {
                            BluetoothAdapter.STATE_OFF -> {
                                // Bluetooth has been turned off;
                                Timber.e("BluetoothAdapter.STATE_OFF")
                                (it.findActivity() as BluetoothActivity).updateBluetoothEnabled(
                                    false
                                )
                            }

                            BluetoothAdapter.STATE_TURNING_OFF -> {
                                // Bluetooth is turning off;
                                Timber.e("BluetoothAdapter.STATE_TURNING_OFF")
                                (it.findActivity() as BluetoothActivity).updateBluetoothSearchProgress(
                                    false
                                )
                            }

                            BluetoothAdapter.STATE_ON -> {
                                // Bluetooth is on
                                Timber.e("BluetoothAdapter.STATE_ON")
                                (it.findActivity() as BluetoothActivity).updateBluetoothEnabled(
                                    true
                                )
                            }

                            BluetoothAdapter.STATE_TURNING_ON -> {
                                // Bluetooth is turning on
                                Timber.e("BluetoothAdapter.STATE_TURNING_ON")
                            }

                            else -> {
                                Timber.e("BluetoothAdapter.ACTION_STATE_CHANGED | intent extra : BluetoothAdapter.EXTRA_STATE | else branch")
                            }
                        }
                    }

                    // Discoverability changes
                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                        Timber.e("BluetoothAdapter.ACTION_DISCOVERY_STARTED")
                        (it.findActivity() as BluetoothActivity).updateIsBluetoothSearching(true)
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        Timber.e("BluetoothAdapter.ACTION_DISCOVERY_FINISHED")

                        (it.findActivity() as BluetoothActivity).updateBluetoothSearchProgress(
                            false
                        )
                        (it.findActivity() as BluetoothActivity).updateIsBluetoothSearching(false)
                    }

                    BluetoothAdapter.ACTION_SCAN_MODE_CHANGED -> {
                        when (
                            intent?.getIntExtra(
                                BluetoothAdapter.EXTRA_SCAN_MODE,
                                BluetoothAdapter.ERROR
                            )
                        ) {
                            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> {
                                Timber.e("BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE")
                            }

                            BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                                Timber.e("BluetoothAdapter.SCAN_MODE_CONNECTABLE")
                            }

                            BluetoothAdapter.SCAN_MODE_NONE -> {
                                Timber.e("BluetoothAdapter.SCAN_MODE_NONE")
                            }

                            else -> {
                                Timber.e("BluetoothAdapter.ACTION_SCAN_MODE_CHANGED | else branch")
                            }
                        }
                    }

                    BluetoothDevice.ACTION_ACL_CONNECTED -> {
                        Timber.e("BluetoothDevice.ACTION_ACL_CONNECTED")
                    }

                    BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                        Timber.e("BluetoothDevice.ACTION_ACL_DISCONNECTED")
                    }

                    else -> {
                        Timber.e("else branch")
                    }
                }
            }
        }
    }
}