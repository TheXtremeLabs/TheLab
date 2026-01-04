package com.riders.thelab.feature.videocall.data

import kotools.types.text.NotBlankString

data class ConnectState(
    val name: NotBlankString,
    val isConnected: Boolean = false,
    val errorMessage: String? = null
) {

    override fun toString(): String {
        return "ConnectState(name=$name, isConnected=$isConnected, errorMessage=$errorMessage)"
    }
}