package com.riders.thelab.core.common.network

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

@SuppressLint("NewApi", "MissingPermission")
class LabNetworkManagerNewAPI(val context: Context) : NetworkCallback() {

//    val mListener: ConnectivityListener get() = listener

    private var connectivityManager: ConnectivityManager
    var currentNetwork: Network? = null

    var caps: NetworkCapabilities? = null
    var linkProperties: LinkProperties? = null

    var mType: Int = 0
    var isConnected: Boolean = false
    var isWifiConn: Boolean = false
    var isMobileConn: Boolean = false

    private val connectionLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private var _networkConnectionState: MutableStateFlow<NetworkConnectionState> =
        MutableStateFlow(NetworkConnectionState.NONE)
    val networkConnectionState: StateFlow<NetworkConnectionState> = _networkConnectionState


    init {
        Timber.d("init")

        connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        currentNetwork = connectivityManager.activeNetwork
        caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        linkProperties = connectivityManager.getLinkProperties(currentNetwork)

        if (LabCompatibilityManager.isNougat()) {
            connectivityManager.registerDefaultNetworkCallback(this)
        }

        connectivityManager.allNetworks.forEach { network ->
            connectivityManager.getNetworkInfo(network)?.apply {
                mType = type
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

        isConnected = isMobileConn || isWifiConn

        updateIsConnected(isConnected)
    }

    /**
     * Check the Internet connection
     *
     * @param
     * @return
     */
    fun getConnectionState(): LiveData<Boolean> {
        return connectionLiveData
    }

    private fun updateNetworkConnectionState(newState: NetworkConnectionState) {
        (context as Activity).runOnUiThread {
            this._networkConnectionState.value = newState
        }
    }

    fun updateIsConnected(isConnected: Boolean) {
        Timber.d("updateIsConnected() | isConnected: $isConnected")

        if (!isConnected) {
            currentNetwork?.let { NetworkConnectionState.Lost(it) }
                ?.let { updateNetworkConnectionState(it) }
        } else {
            currentNetwork?.let {
                NetworkConnectionState.Connected(
                    ConnectionModel(mType, isConnected),
                    it
                )
            }?.let { updateNetworkConnectionState(it) }
        }
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.d("onAvailable()")
        Timber.e("The default network is now: $network")

        currentNetwork = network

        currentNetwork?.let {
            updateNetworkConnectionState(
                NetworkConnectionState.Connected(ConnectionModel(mType, true), it)
            )
        }

        (context as Activity).runOnUiThread { connectionLiveData.value = true }
    }

    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        Timber.e("The default network changed capabilities: $networkCapabilities")

        currentNetwork = network
        caps = networkCapabilities

        currentNetwork?.let { validNetwork ->
            caps?.let { validCapabilities ->
                updateNetworkConnectionState(
                    NetworkConnectionState.OnCapabilitiesChanged(
                        validNetwork,
                        validCapabilities
                    )
                )
            }
        }
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        // Timber.e("The default network changed link properties: $linkProperties")
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        super.onBlockedStatusChanged(network, blocked)
        Timber.e("onBlockedStatusChanged() | network: $network, blocked: $blocked")
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Timber.e("onLosing()")

        currentNetwork = network

        currentNetwork?.let {
            updateNetworkConnectionState(
                NetworkConnectionState.OnLosing(it, maxMsToLive)
            )
        }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Timber.e("onLost()")
        Timber.e("The application no longer has a default network. The last default network was $network")

        currentNetwork = network

        currentNetwork?.let { updateNetworkConnectionState(NetworkConnectionState.Lost(it)) }

        (context as Activity).runOnUiThread { connectionLiveData.value = false}
    }

    override fun onUnavailable() {
        super.onUnavailable()
        Timber.e("onUnavailable()")

        updateNetworkConnectionState(NetworkConnectionState.Unavailable)
        (context as Activity).runOnUiThread {  connectionLiveData.value = false}
    }

    fun isOnline(): Boolean {
        Timber.d("isOnline()")

        var connected: Boolean = false

        connectivityManager.allNetworks.run {
            this.forEach { network ->
                connectivityManager.getNetworkInfo(network)?.apply {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        isWifiConn = isWifiConn or isConnected
                    }
                    if (type == ConnectivityManager.TYPE_MOBILE) {
                        isMobileConn = isMobileConn or isConnected
                    }

                    connected = isWifiConn || isMobileConn
                }
            }
        }

        updateIsConnected(connected)
        return connected
    }
    /*fun isOnline(): Boolean = connectivityManager.allNetworks.run {
        Timber.d("isOnline()")
        isConnected == true
    } ?: run {
        Timber.e("isOnline() | false")
        false
    }*/

    @SuppressLint("NewApi")
    private fun getConnectionInfo() {
        Timber.d("setListeners()")

        if (null == connectivityManager) {
            connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        }

        currentNetwork = connectivityManager.activeNetwork
        caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        linkProperties = connectivityManager.getLinkProperties(currentNetwork)

        connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
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

            override fun onLinkPropertiesChanged(
                network: Network,
                linkProperties: LinkProperties
            ) {
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

        // UIManager.showActionInToast(applicationContext, "Wifi clicked")

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
                Timber.d("create a new LabNetworkManagerNewAPI instance")
                INSTANCE = LabNetworkManagerNewAPI(context)
            }

            return INSTANCE as LabNetworkManagerNewAPI
        }
    }
}