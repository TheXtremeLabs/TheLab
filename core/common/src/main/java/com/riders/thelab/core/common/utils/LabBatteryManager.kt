package com.riders.thelab.core.common.utils

import android.content.Context
import android.os.BatteryManager

object LabBatteryManager {
    /////////////////////////////////////////
    // Battery
    /////////////////////////////////////////
    // Call battery manager service
    private fun getBatteryManager(context: Context): BatteryManager =
        context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    // Get the battery percentage and store it in a INT variable
    fun getBatteryLevel(context: Context): Int =
        getBatteryManager(context).getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
}