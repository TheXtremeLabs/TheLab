package com.riders.thelab.data.local.model

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
) {
    override fun toString(): String {
        return "DeviceInformation(name=$name, brand=$brand, model=$model, serial=$serial, fingerPrint=$fingerPrint, hardware=$hardware, IMEI=$IMEI, id=$id, screenWidth=$screenWidth, screenHeight=$screenHeight, androidVersionName=$androidVersionName, sdkVersion=$sdkVersion, androidRelease=$androidRelease, rooted=$rooted)"
    }
}
