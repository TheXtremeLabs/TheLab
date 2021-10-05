package com.riders.thelab.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.core.interfaces.ConnectivityListener
import timber.log.Timber

class LabNetworkManagerNewAPI constructor(
    val listener: ConnectivityListener
) : NetworkCallback() {

    companion object {
        var isConnected = false
    }

    val mListener: ConnectivityListener get() = listener


    /**
     * Check the Internet connection
     *
     * @param context
     * @return
     */
    fun isConnected(): Boolean {
        return isConnected
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.d("onAvailable()")
        isConnected = true
        mListener.onConnected()
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
        mListener.onLostConnection()
    }

    override fun onUnavailable() {
        super.onUnavailable()
        Timber.e("onUnavailable()")
        isConnected = false
    }


    @SuppressLint("WifiManagerPotentialLeak", "InlinedApi")
    fun changeWifiState(applicationContext: Context, activity: Activity) {
        Timber.d("changeWifiState()")

        UIManager.showActionInToast(applicationContext, "Wifi clicked")
        val wifiManager: WifiManager =
            applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        if (!LabCompatibilityManager.isAndroid10()) {
            val isWifi = wifiManager.isWifiEnabled
            @Suppress("DEPRECATION")
            wifiManager.isWifiEnabled = !isWifi
        } else {
            Timber.e("For applications targeting android.os.Build.VERSION_CODES Q or above, this API will always fail and return false")

            /*
                ACTION_INTERNET_CONNECTIVITY Shows settings related to internet connectivity, such as Airplane mode, Wi-Fi, and Mobile Data.
                ACTION_WIFI Shows Wi-Fi settings, but not the other connectivity settings. This is useful for apps that need a Wi-Fi connection to perform large uploads or downloads.
                ACTION_NFC Shows all settings related to near-field communication (NFC).
                ACTION_VOLUME Shows volume settings for all audio streams.
             */
            val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
            activity.startActivityForResult(panelIntent, 955)
        }
    }
}