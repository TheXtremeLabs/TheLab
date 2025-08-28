package com.riders.thelab.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import androidx.core.location.LocationManagerCompat
import timber.log.Timber


class LabLocationManager(private val context: Context) {

    private val mLocationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


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

    companion object {
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