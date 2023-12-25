package com.riders.thelab.core.data.local.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class DeviceInformation(
    val name: String,
    val brand: String,
    val model: String,
    val serial: String,
    val fingerPrint: String,
    val hardware: String,
    val imei: String,
    val id: String,
    val screenWidth: Int = 0,
    val screenHeight: Int = 0,

    val androidVersionName: String,
    val sdkVersion: Int = 0,
    val androidRelease: String,
    val rooted: Boolean = false
)
