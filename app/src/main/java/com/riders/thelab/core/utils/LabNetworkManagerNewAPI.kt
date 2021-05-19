package com.riders.thelab.core.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import com.riders.thelab.core.interfaces.ConnectivityListener
import timber.log.Timber

class LabNetworkManagerNewAPI constructor(
        val listener: ConnectivityListener
) : NetworkCallback() {

    companion object {
        private var isConnected = false
    }

    init {
        val mListener: ConnectivityListener = listener
    }

    /**
     * Check the Internet connection
     *
     * @param context
     * @return
     */
    fun isConnected(context: Context): Boolean {
        if (!LabCompatibilityManager.isLollipop()) {
            val connMgr = context.getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            isConnected = networkInfo != null && networkInfo.isConnected
        }
        return isConnected
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.d("onAvailable()")
        isConnected = true
        listener.onConnected()
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        super.onBlockedStatusChanged(network, blocked)
        Timber.e("onBlockedStatusChanged()")
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Timber.e("onLosing()")
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Timber.e("onLost()")
        isConnected = false
        listener.onLostConnection()
    }

    override fun onUnavailable() {
        super.onUnavailable()
        Timber.e("onUnavailable()")
        isConnected = false
    }
}