package com.riders.thelab.core.common.network

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("MissingPermission")
class LabNetworkManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ConnectivityManager.NetworkCallback() {

    // Connectivity manager
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private val currentNetwork: Network? = connectivityManager.activeNetwork
    private var capabilities: NetworkCapabilities? =
        connectivityManager.getNetworkCapabilities(currentNetwork)
    val linkProperties = connectivityManager.getLinkProperties(currentNetwork)

    // State flow
    private var _networkState: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Undefined)
    var networkState: StateFlow<NetworkState> = _networkState

    val isConnectedFlow: Flow<Boolean>
        @SuppressLint("NewApi")
        get() = callbackFlow {
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    connectivityManager.getNetworkCapabilities(network)?.let {
                        if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                            trySend(true)
                        }
                    }
                }

                override fun onLost(network: Network) {
                    trySend(false)
                }

                override fun onUnavailable() {
                    trySend(false)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    capabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, capabilities)

                    this@LabNetworkManager.capabilities = capabilities

                    if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        trySend(true)
                    } else {
                        trySend(false)
                    }
                }
            }

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

            if (LabCompatibilityManager.isNougat()) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            } else {
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            }

            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }
            .flowOn(Dispatchers.IO)

    //////////////////////////////////
    //
    // OVERRIDE
    //
    //////////////////////////////////
    init {
        Timber.d("init method")
        // registerLifecycle()
        // registerCallback()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.i("onAvailable()")
        updateNetworkState(NetworkState.Available)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        // Timber.i("onCapabilitiesChanged()")
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        // Timber.w("onLinkPropertiesChanged()")
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Timber.w("onLosing()")
        updateNetworkState(NetworkState.Losing)

    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Timber.e("onLost()")
        updateNetworkState(NetworkState.Lost)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        Timber.e("onUnavailable()")
        updateNetworkState(NetworkState.Unavailable)
    }

    //////////////////////////////////
    //
    // KOTLIN COROUTINES
    //
    //////////////////////////////////
    private fun updateNetworkState(newState: NetworkState) {
        _networkState.tryEmit(newState)
    }

    fun getNetworkState(): Flow<NetworkState> = networkState

    //////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////
    @SuppressLint("NewApi")
    fun isNetworkAvailable(): Boolean = if (LabCompatibilityManager.isMarshmallow()) {
        Timber.d("isNetworkAvailable()")

        capabilities?.isNetworkCapabilitiesValid() ?: run {
            Timber.e("Capabilities is null")
            false
        }
    } else {
        runCatching {
            @Suppress("DEPRECATION")
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

            activeNetworkInfo?.run {
                @Suppress("DEPRECATION")
                val isActive: Boolean = this.isConnected
                Timber.d("Network is available: $isActive")
                isActive
            } ?: run {
                Timber.e("Active network is null")
                false
            }
        }
            .onFailure { Timber.e("onFailure() | Error caught with message ${it.message}") }
            .getOrDefault(false)
    }


    @SuppressLint("WifiManagerPotentialLeak", "InlinedApi")
    fun changeWifiState(context: Context, activity: Activity) {
        Timber.d("changeWifiState()")

        val wifiManager: WifiManager =
            context.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager

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

    private fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean = when {
        this == null -> false
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        //for check internet over Bluetooth
                        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                        //for other device how are able to connect with Ethernet
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) -> true

        else -> false
    }


    @Suppress("DEPRECATION")
    fun getNetworkType(): NetworkType =
        when (connectivityManager.activeNetworkInfo!!.type) {
            ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
            ConnectivityManager.TYPE_MOBILE -> NetworkType.MOBILE
            ConnectivityManager.TYPE_ETHERNET -> NetworkType.ETHERNET
            else -> NetworkType.NONE
        }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mInstance: LabNetworkManager? = null

        fun getInstance(context: Context): LabNetworkManager =
            mInstance ?: synchronized(this) { mInstance ?: LabNetworkManager(context) }
    }
}