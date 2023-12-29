package com.riders.thelab.core.data.local.model

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi

sealed class Permission(vararg val permissions: String) {

    // Individual permissions
    data object AudioRecord : Permission(Manifest.permission.RECORD_AUDIO)
    data object Camera : Permission(Manifest.permission.CAMERA)
    data object PhoneState : Permission(Manifest.permission.READ_PHONE_STATE)
    data object UserAccounts : Permission(Manifest.permission.GET_ACCOUNTS)

    // Grouped permissions
    data object Bluetooth :
        Permission(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)

    @RequiresApi(Build.VERSION_CODES.S)
    data object BluetoothAndroid12 :
        Permission(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )

    data object Location :
        Permission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    data object Storage : Permission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    data object MediaLocation :
        Permission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_MEDIA_LOCATION
        )

    // Bundled permissions
    data object MandatoryForFeatureOne : Permission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )


    companion object {
        @SuppressLint("NewApi")
        fun from(permission: String) = when (permission) {
            // Individual permissions
            Manifest.permission.RECORD_AUDIO -> AudioRecord
            Manifest.permission.CAMERA -> Camera
            Manifest.permission.READ_PHONE_STATE -> PhoneState
            Manifest.permission.GET_ACCOUNTS -> UserAccounts
            Manifest.permission.ACCESS_MEDIA_LOCATION -> MediaLocation
            // Grouped permissions
            Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN -> Bluetooth
            Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT -> BluetoothAndroid12
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION -> Location
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE -> Storage
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}
