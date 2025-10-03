package com.riders.thelab.core.location

import kotlin.run

data class GPSProvidersResultModel(
    val isGPS: Boolean = false,
    val isNetwork: Boolean  = false
) {
    val type: String
        get() = run {
            if (isGPS) {
                "Location provided by GPS"
            } else if (isNetwork) {
                "Location provided by network"
            } else {
                "No Location provided"
            }
        }

    override fun toString(): String {
        return "GPSProvidersResultModel(isGPS=$isGPS, isNet=$isNetwork, type='$type')"
    }
}