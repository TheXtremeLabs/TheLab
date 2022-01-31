package com.riders.thelab.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

@SuppressLint("NewApi")
class LabNetworkManagerNewAPI constructor(
    val context: Context
//    val listener: ConnectivityListener
) : NetworkCallback() {

//    val mListener: ConnectivityListener get() = listener

    private lateinit var connectivityManager: ConnectivityManager
    var currentNetwork: Network? = null

    var caps: NetworkCapabilities? = null
    var linkProperties: LinkProperties? = null

    var isConnected: Boolean = false
    var isWifiConn: Boolean = false
    var isMobileConn: Boolean = false

    private val connectionState: MutableLiveData<Boolean> = MutableLiveData()

    init {
        Timber.d("init")

        connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        currentNetwork = connectivityManager.activeNetwork
        caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        linkProperties = connectivityManager.getLinkProperties(currentNetwork)

        if (LabCompatibilityManager.isNougat()) {
            connectivityManager.registerDefaultNetworkCallback(this)
        }

        @Suppress("DEPRECATION")
        connectivityManager.allNetworks.forEach { network ->
            connectivityManager.getNetworkInfo(network)?.apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }

        Timber.d("Wifi connected: $isWifiConn")
        Timber.d("Mobile connected: $isMobileConn")
    }

    /**
     * Check the Internet connection
     *
     * @param
     * @return
     */
    fun getConnectionState(): LiveData<Boolean> {
        return connectionState
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.d("onAvailable()")
        Timber.e("The default network is now: $network")

        (context as Activity).runOnUiThread {
            connectionState.value = true
        }

        /*isConnected = true
        mListener.onConnected()*/
    }

    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        Timber.e("The default network changed capabilities: $networkCapabilities")
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        Timber.e("The default network changed link properties: $linkProperties")
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
        Timber.e("The application no longer has a default network. The last default network was $network")

        (context as Activity).runOnUiThread {
            connectionState.value = false
        }
    }

    override fun onUnavailable() {
        super.onUnavailable()
        Timber.e("onUnavailable()")

        (context as Activity).runOnUiThread {
            connectionState.value = false
        }
    }

    fun isOnline(): Boolean {
        Timber.d("isOnline()")

        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }


    @SuppressLint("NewApi")
    private fun getConnectionInfo() {
        Timber.d("setListeners()")

        if (null == connectivityManager) {
            connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        }

        currentNetwork = connectivityManager.activeNetwork
        caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        linkProperties = connectivityManager.getLinkProperties(currentNetwork)

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Timber.e("The default network is now: $network")
            }

            override fun onLost(network: Network) {
                Timber.e("The application no longer has a default network. The last default network was $network")
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                Timber.e("The default network changed capabilities: $networkCapabilities")
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                Timber.e("The default network changed link properties: $linkProperties")
            }
        })

        connectivityManager.allNetworks.forEach { network ->
            connectivityManager.getNetworkInfo(network)?.apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }

        Timber.d("Wifi connected: $isWifiConn")
        Timber.d("Mobile connected: $isMobileConn")
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


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: LabNetworkManagerNewAPI? = null

        fun getInstance(context: Context): LabNetworkManagerNewAPI {
            Timber.d("getInstance()")
            if (null == INSTANCE) {
                Timber.d("create a new PraeterNetworkManagerNewAPI instance")
                INSTANCE = LabNetworkManagerNewAPI(context)
            }

            return INSTANCE as LabNetworkManagerNewAPI
        }
    }
}