package com.riders.thelab.feature.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
    val type = bluetoothDevice.type

    val shape = if (0 == index) {
        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    } else if (totalItemsCount - 1 == index) {
        RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
    } else {
        RoundedCornerShape(0.dp)
    }

    TheLabTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            shape = shape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = deviceName ?: macAddress)
            }
        }
    }
}