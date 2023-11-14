package com.riders.thelab.core.data.local.model

data class DeviceInformation(
    val name: String,
    val brand: String,
    val model: String,
    val serial: String,
    val fingerPrint: String,
    val hardware: String,
    val IMEI: String,
    val id: String,
    val screenWidth: Int = 0,
    val screenHeight: Int = 0,

    val androidVersionName: String,
    val sdkVersion: Int = 0,
    val androidRelease: String,
    val rooted: Boolean = false
)
