package com.riders.thelab.core.interfaces

interface ConnectivityListener {
    fun onConnected()

    fun onLostConnection()
}