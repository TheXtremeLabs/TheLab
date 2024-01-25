package com.riders.thelab.core.common.network

enum class NetworkType(val type:Byte) {
    VPN(0),
    WIFI(1),
    MOBILE(2),
    ETHERNET(3),
    NONE(-1)
}