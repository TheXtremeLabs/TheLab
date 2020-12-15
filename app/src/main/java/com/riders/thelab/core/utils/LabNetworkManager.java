package com.riders.thelab.core.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.riders.thelab.core.interfaces.ConnectivityListener;

import timber.log.Timber;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LabNetworkManager extends ConnectivityManager.NetworkCallback {

    ConnectivityListener listener;

    private static boolean isConnected = false;

    public LabNetworkManager(ConnectivityListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        Timber.d("onAvailable()");

        isConnected = true;

        listener.onConnected();
    }

    @Override
    public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
        super.onBlockedStatusChanged(network, blocked);
        Timber.e("onBlockedStatusChanged()");
    }

    @Override
    public void onLosing(@NonNull Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        Timber.e("onLosing()");
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        Timber.e("onLost()");

        isConnected = false;

        listener.onLostConnection();
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
        Timber.e("onUnavailable()");

        isConnected = false;
    }


    /**
     * Check the Internet connection
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return isConnected;
    }
}
