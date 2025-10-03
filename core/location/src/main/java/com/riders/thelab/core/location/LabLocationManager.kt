package com.riders.thelab.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber


class LabLocationManager private constructor(private val context: Context) {

    private val mLocationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // flag for GPS status
    private var isGPSEnabled = false

    // flag for network status
    private var isNetworkEnabled = false

    // flag for GPS status
    private var canGetLocation = false

    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Timber.i("locationListener | location: ${location.toString()}")
        }

        override fun onProviderEnabled(provider: String) {
            Timber.d("locationListener | onProviderEnabled() | provider: $provider")
        }

        override fun onProviderDisabled(provider: String) {
            Timber.e("locationListener | onProviderDisabled() | provider: $provider")
        }
    }

    val fusedLocationListener = object : LocationListener {
        override fun onLocationChanged(fusedLocation: Location) {
            Timber.i("fusedLocationListener | location: ${fusedLocation.toString()}")
        }

        override fun onProviderEnabled(provider: String) {
            Timber.d("locationListener | onProviderEnabled() | provider: $provider")
        }

        override fun onProviderDisabled(provider: String) {
            Timber.e("fusedLocationListener | onProviderDisabled() | provider: $provider")
        }
    }

    /////////////////////////////////////
    //
    // Composable states
    //
    /////////////////////////////////////
    val _gpsProvidersModel: MutableStateFlow<GPSProvidersResultModel?> = MutableStateFlow(null)
    val gpsProvidersModel: StateFlow<GPSProvidersResultModel?> = _gpsProvidersModel
    val lastKnownLocationFlow: Flow<Location?>
        get() = callbackFlow {
            Timber.i("lastKnownLocationFlow | callbackFlow | attempting to get location...")
            trySend(getCurrentLocation())

            awaitClose { Timber.e("lastKnownLocationFlow | awaitClose") }
        }
            .flowOn(Dispatchers.IO)
            .catch { exception -> }
            .distinctUntilChanged()

    fun updateGpsProvidersModel(isGps: Boolean? = null, isNetwork: Boolean? = null) {
        val maybeNewIsGPS: Boolean =
            if (null != isGps || _gpsProvidersModel.value?.isGPS != isGps) isGps!! else _gpsProvidersModel.value?.isGPS
                ?: false
        val maybeNewIsNetwork: Boolean =
            if (null != isNetwork || _gpsProvidersModel.value?.isNetwork != isNetwork) isNetwork!! else _gpsProvidersModel.value?.isNetwork
                ?: false
        this._gpsProvidersModel.value?.copy(isGPS = maybeNewIsGPS, isNetwork = maybeNewIsNetwork)
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    fun isNetworkProviderEnabled(): Boolean = runCatching {
        mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
        .onFailure { Timber.e("isNetworkProviderEnabled() | Exception: $it") }
        .getOrDefault(false)

    fun isGpsProviderEnabled(): Boolean =
        runCatching { mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) }
            .onFailure { Timber.e("isGpsProviderEnabled() | Exception: $it") }
            .getOrDefault(false)

    // Returns true even if the location services are disabled. Do not use this method to detect location services are enabled.
    private fun isPassiveProviderEnabled(): Boolean =
        runCatching { mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER) }
            .onFailure { Timber.e("isPassiveProviderEnabled() | Exception: $it") }
            .getOrDefault(false)

    @Throws(Exception::class)
    fun isLocationModeOn(): Boolean {
        val locationMode = Settings.Secure.getInt(
            context.contentResolver,
            Settings.Secure.LOCATION_MODE
        )
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    fun isLocationServicesEnabled(): Boolean =
        isGpsProviderEnabled() || isNetworkProviderEnabled() || isLocationModeOn()


    fun isLocationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Timber.d("isLocationEnabled() | new method provided in API 28")
            // This is a new method provided in API 28
            LocationManagerCompat.isLocationEnabled(mLocationManager)
        } else {
            Timber.d("isLocationEnabled() | old method provided before API 28")
            // This is an old method provided before API 28

            var locationMode: Int = 0
            val locationProviders: String?
            try {
                @Suppress("DEPRECATION")
                locationMode = Settings.Secure.getInt(
                    context.contentResolver,
                    Settings.Secure.LOCATION_MODE
                )
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF
        }
    }


    @WorkerThread
    fun getCurrentLocation(): Location? {
        Timber.d("getCurrentLocation()")

        var location: Location? = null

        return if (!canGetLocation()) {
            // no network provider is enabled
            Timber.e("no network provider is enabled")
            null
        } else {
            try {
                // if Network Enabled get lat/long using Network
                if (isNetworkEnabled) {
                    location = getLocationViaNetwork()
                }

                // return location object
                return location
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    location = getLocationViaGPS()
                }

                // return location object
                return location
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    @Throws(
        IllegalArgumentException::class,
        IllegalArgumentException::class,
        RuntimeException::class,
        SecurityException::class
    )
    private fun getLocationViaNetwork(): Location? {
        ContextCompat.getMainExecutor(context).execute {
            // This is where your UI code goes.
            mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                locationListener
            )
        }

        Timber.d("getLocationViaNetwork() | Network Enabled")

        return mLocationManager
            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            ?.let { location ->
                updateGpsProvidersModel(isNetwork = true)
                location
            } ?: run {
            Timber.e("Unable to get location via network provider")
            null
        }
    }

    @Throws(
        IllegalArgumentException::class,
        IllegalArgumentException::class,
        RuntimeException::class,
        SecurityException::class
    )
    private fun getLocationViaGPS(): Location? {
        ContextCompat.getMainExecutor(context).execute {
            // This is where your UI code goes.
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                locationListener
            )
        }

        Timber.d("getLocationViaGPS() | GPS Enabled")

        return mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?.let { location ->
                updateGpsProvidersModel(isGps = true)
                location
            } ?: run {
            Timber.e("Unable to get location via gps provider")
            null
        }
    }


    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    fun stopUsingGPS() {
        mLocationManager.removeUpdates(locationListener)
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     *
     * @return boolean
     */
    fun canGetLocation(): Boolean {
        try {
            Timber.d("canGetLocation() | isProviderEnabled(LocationManager.GPS_PROVIDER)")
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Timber.e("isGPSEnabled | exception message: ${ex.message}")
        }
        try {
            Timber.d("canGetLocation() | isProviderEnabled(LocationManager.NETWORK_PROVIDER)")
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Timber.e("isNetworkEnabled | exception message: ${ex.message}")
        }

        canGetLocation = isNetworkEnabled || isGPSEnabled

        return canGetLocation
    }

    /////////////////////////////////////
    //
    // COMPANION / INNER CLASSES
    //
    /////////////////////////////////////
    companion object {
        // The minimum distance to change Updates in meters
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        const val MIN_TIME_BETWEEN_UPDATES = (1000 * 60 * 1).toLong() // 1 minute

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: LabLocationManager? = null

        fun getInstance(context: Context): LabLocationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LabLocationManager(context).also { INSTANCE = it }
            }
        }
    }
}