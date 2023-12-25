package com.riders.thelab.core.common.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("MissingPermission")
class LabNetworkManager(
    private val context: Context,
    private val lifecycle: Lifecycle
) : ConnectivityManager.NetworkCallback() {

    // Connectivity manager
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private val currentNetwork = connectivityManager.activeNetwork
    val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
    val linkProperties = connectivityManager.getLinkProperties(currentNetwork)

    // State flow
    private var _networkState: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Undefined)
    var networkState: StateFlow<NetworkState> = _networkState

    //////////////////////////////////
    //
    // OVERRIDE
    //
    //////////////////////////////////
    init {
        Timber.d("init method")
        registerLifecycle()
        registerCallback()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.i("onAvailable()")
        updateNetworkState(NetworkState.Available)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Timber.i("onCapabilitiesChanged()")
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
        Timber.w("onLinkPropertiesChanged()")
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
    fun updateNetworkState(newState: NetworkState) {
        _networkState.tryEmit(newState)
        /*CoroutineScope(Dispatchers.IO).launch {
            _networkState.tryEmit(newState)
        }*/
    }

    fun getNetworkState(): Flow<NetworkState> = networkState

    //////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////
    private fun registerLifecycle() {
        Timber.d("registerLifecycle()")

        (context as ComponentActivity).lifecycleScope.launch {
            when (lifecycle.currentState) {
                Lifecycle.State.STARTED -> {
                    Timber.d("Lifecycle.State.STARTED")
                }

                Lifecycle.State.RESUMED -> {
                    Timber.d("Lifecycle.State.RESUMED")
                }

                Lifecycle.State.DESTROYED -> {
                    Timber.e("Lifecycle.State.DESTROYED")
                    connectivityManager.unregisterNetworkCallback(this@LabNetworkManager)
                }

                else -> {
                    Timber.e("else branch")
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun registerCallback() {
        Timber.d("registerLifecycle()")
        if (!LabCompatibilityManager.isNougat()) {
            val networkRequest = NetworkRequest.Builder()
                .apply {
                    addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                }
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, this)
        } else {
            connectivityManager.registerDefaultNetworkCallback(this)
        }
    }

    @ChecksSdkIntAtLeast(Build.VERSION_CODES.Q)
    private fun isAndroid10() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @Suppress("DEPRECATION")
    fun isNetworkAvailable(): Boolean = if (LabCompatibilityManager.isAndroid10()) {
        Timber.d("isNetworkAvailable()")

        val capabilities: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        capabilities?.run {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                true
            } else {
                false
            }
        } ?: run {
            Timber.e("Capabilities is null")
            false
        }
    } else {
        runCatching {
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

            activeNetworkInfo?.run {
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

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mInstance: LabNetworkManager? = null

        fun getInstance(context: Context, lifecycle: Lifecycle): LabNetworkManager =
            mInstance ?: LabNetworkManager(context, lifecycle)
    }
}