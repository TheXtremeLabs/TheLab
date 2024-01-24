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

    @SuppressLint("InlinedApi")
    @RequiresApi(Build.VERSION_CODES.Q)
    data object MediaLocation :
        Permission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_MEDIA_LOCATION
        )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    data object MediaLocationAndroid13 :
        Permission(
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
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
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO -> MediaLocationAndroid13
            // Grouped permissions
            Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT -> BluetoothAndroid12
            Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN -> Bluetooth
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION -> Location
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE -> Storage
            Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE -> MediaLocation
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}
