package com.riders.thelab.feature.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

@SuppressLint("MissingPermission")
@Composable
fun BluetoothItem(index: Int, bluetoothDevice: BluetoothDevice, totalItemsCount: Int) {

    val deviceName = bluetoothDevice.name
    val macAddress = bluetoothDevice.address
    val type = bluetoothDevice.bluetoothClass.deviceClass

    val shape = when (index) {
        0 -> {
            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
        }

        totalItemsCount - 1 -> {
            RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
        }

        else -> {
            RoundedCornerShape(0.dp)
        }
    }

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(start = 16.dp, end = 16.dp),
            shape = shape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (type) {
                        BluetoothClass.Device.PHONE_SMART -> {
                            Icons.Filled.PhoneAndroid
                        }

                        BluetoothClass.Device.COMPUTER_LAPTOP -> {
                            Icons.Filled.Computer
                        }

                        BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> {
                            Icons.Filled.DirectionsCar
                        }

                        BluetoothClass.Device.WEARABLE_UNCATEGORIZED -> {
                            Icons.Filled.Print
                        }

                        else -> {
                            Icons.Filled.Bluetooth
                        }
                    }, contentDescription = null
                )
                Text(text = deviceName ?: macAddress)
            }
        }
    }
}