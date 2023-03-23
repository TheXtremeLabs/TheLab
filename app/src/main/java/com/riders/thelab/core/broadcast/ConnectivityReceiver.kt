package com.riders.thelab.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI

class ConnectivityReceiver(
    private val mConnectivityReceiverListener: ConnectivityReceiverListener?
) : BroadcastReceiver() {

    companion object {
        private var mInstance: ConnectivityReceiver? = null

        fun getInstance(): ConnectivityReceiver {
            if (null == mInstance) {
                this.mInstance = ConnectivityReceiver()
            }
            return this.mInstance as ConnectivityReceiver
        }
    }

    constructor() : this(null)

    override fun onReceive(context: Context?, intent: Intent?) {
        var isConnected = false
        if (null != context) {
            isConnected = LabNetworkManagerNewAPI.getInstance(context).isOnline()
        }
        mConnectivityReceiverListener?.onNetworkConnectionChanged(isConnected)
    }

    fun getConnectivityReceiverListener(): ConnectivityReceiverListener? {
        return mConnectivityReceiverListener
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}