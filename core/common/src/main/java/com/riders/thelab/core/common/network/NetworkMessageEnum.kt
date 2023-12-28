package com.riders.thelab.core.common.network

enum class NetworkMessageEnum(val message: String) {
    AVAILABLE("You're connected to the internet"),
    LOST("Connection Lost..."),
    UNAVAILABLE("Unable to connect. Please verify your internet/wifi settings");
}