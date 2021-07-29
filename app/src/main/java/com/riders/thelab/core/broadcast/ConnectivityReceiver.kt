package com.riders.thelab.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI

class ConnectivityReceiver(
    private val mConnectivityReceiverListener: ConnectivityReceiverListener?
) : BroadcastReceiver() {

    companion object {
        private lateinit var mInstance: ConnectivityReceiver

        fun getInstance(): ConnectivityReceiver {
            if (null == this.mInstance) {
                this.mInstance = ConnectivityReceiver()
            }
            return this.mInstance
        }
    }

    constructor() : this(null) {
    }

    override fun onReceive(context: Context?, arg1: Intent?) {
        val isConnected: Boolean = LabNetworkManagerNewAPI.isConnected
        mConnectivityReceiverListener?.onNetworkConnectionChanged(isConnected)
    }

    fun getConnectivityReceiverListener(): ConnectivityReceiverListener? {
        return mConnectivityReceiverListener
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}