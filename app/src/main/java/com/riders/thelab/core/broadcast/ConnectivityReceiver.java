package com.riders.thelab.core.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.riders.thelab.core.utils.LabNetworkManager;


public class ConnectivityReceiver extends BroadcastReceiver {

    private static ConnectivityReceiver mInstance;

    private final ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        connectivityReceiverListener = null;
    }

    public ConnectivityReceiver(ConnectivityReceiverListener connectivityReceiverListener) {
        super();
        this.connectivityReceiverListener = connectivityReceiverListener;
    }

    public static ConnectivityReceiver getInstance() {
        if (null == mInstance) {
            mInstance = new ConnectivityReceiver();
        }

        return mInstance;
    }

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent arg1) {

        if (LabCompatibilityManager.isLollipop()) {

            boolean isConnected = LabNetworkManager.isConnected(context);

            if (connectivityReceiverListener != null) {
                connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
            }
        }
    }

    public ConnectivityReceiverListener getConnectivityReceiverListener() {
        return connectivityReceiverListener;
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
