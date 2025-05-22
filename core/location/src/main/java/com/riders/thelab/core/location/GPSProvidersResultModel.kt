package com.riders.thelab.core.location

import kotlin.run

data class GPSProvidersResultModel(
    val isGPS: Boolean,
    val isNet: Boolean
) {
    val type: String
        get() = run {
            if (isGPS) {
                "Location provided by GPS"
            } else if (isNet) {
                "Location provided by network"
            } else {
                "No Location provided"
            }
        }

    override fun toString(): String {
        return "GPSProvidersResultModel(isGPS=$isGPS, isNet=$isNet, type='$type')"
    }
}