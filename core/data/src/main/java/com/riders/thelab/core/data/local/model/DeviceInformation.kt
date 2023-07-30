package com.riders.thelab.core.data.local.model

data class DeviceInformation constructor(
    var name: String,
    var brand: String,
    var model: String,
    var serial: String,
    var fingerPrint: String,
    var hardware: String,
    var IMEI: String,
    var id: String,
    var screenWidth: Int = 0,
    var screenHeight: Int = 0,

    var androidVersionName: String,
    var sdkVersion: Int = 0,
    var androidRelease: String,
    var rooted: Boolean = false
)
