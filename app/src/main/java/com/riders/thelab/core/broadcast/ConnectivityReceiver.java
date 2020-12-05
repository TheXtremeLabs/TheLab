package com.riders.thelab.core.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.riders.thelab.core.utils.LabNetworkManager;


public class ConnectivityReceiver extends BroadcastReceiver {

    private static ConnectivityReceiver mInstance;

    private final ConnectivityReceiverListener connectivityReceiverListener;

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public static ConnectivityReceiver getInstance() {
        if (null == mInstance) {
            mInstance = new ConnectivityReceiver();
        }

        return mInstance;
    }

    public ConnectivityReceiver() {
        connectivityReceiverListener = null;
    }

    public ConnectivityReceiver(ConnectivityReceiverListener connectivityReceiverListener) {
        super();
        this.connectivityReceiverListener = connectivityReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent arg1) {

        boolean isConnected = LabNetworkManager.isConnected(context);

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public ConnectivityReceiverListener getConnectivityReceiverListener() {
        return connectivityReceiverListener;
    }
}
