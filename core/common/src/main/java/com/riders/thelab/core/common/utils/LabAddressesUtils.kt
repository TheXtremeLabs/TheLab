package com.riders.thelab.core.common.utils

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.riders.thelab.core.common.BuildConfig
import timber.log.Timber

object LabAddressesUtils {

    /**
     * Get address for current location (legacy method for Android SDK < Android 33)
     *
     * Consider using [getDeviceAddressAndroid13(geocoder, location, callback)][getDeviceAddressAndroid13] if your Android SDK equals or is above Android 33
     *
     * @param geocoder Geocoding is the process of transform street address or other description of a location into a (latitude, longitude) coordinate.
     * @param location latitude and longitude coordinates
     *
     * @return Returns null if no address has been found, or [Address] if found
     */
    @Suppress("DEPRECATION")
    fun getDeviceAddressLegacy(geocoder: Geocoder, location: Location): Address? {
        Timber.i("getDeviceAddress() legacy")

        return runCatching {
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )

            if (BuildConfig.DEBUG) {
                addresses?.get(0)?.getAddressToString()
            }

            //get the address
            addresses?.get(0)
        }
            .onFailure { exception ->
                exception.printStackTrace()
                Timber.e("Error caught: ${exception.message}")
            }
            .getOrNull()
    }

    /**
     * Get address for current location (new method for Android SDK >= Android 33)
     *
     * Consider using [getDeviceAddressLegacy(geocoder, location)][getDeviceAddressLegacy] if your Android SDK is below Android 33
     *
     * @param geocoder Geocoding is the process of transform street address or other description of a location into a (latitude, longitude) coordinate.
     * @param location latitude and longitude coordinates
     *
     * @return Returns null if no address has been found, or [Address] if found
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getDeviceAddressAndroid13(
        geocoder: Geocoder,
        location: Location,
        callback: (Address?) -> Unit
    ) {
        Timber.i("getDeviceAddress() for Android 13+")

        geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ) {
            val address = it[0]

            if (BuildConfig.DEBUG) {
                address.getAddressToString()
            }

            // Timber.e("You are located to: ${address.getAddressLine(0)}")
            callback(address)
        }
    }

    fun Address.getAddressToString(): String {
        val addressStringBuilder = StringBuilder()
        val street = this.featureName + ", " + this.thoroughfare
        val locality = this.locality
        val postalCode = this.postalCode
        val departmentName = this.subAdminArea
        val regionName = this.adminArea
        val countryName = this.countryName

        addressStringBuilder
            .append("\n")
            .append(street).append("\n")
            .append(postalCode).append(" - ").append(locality).append("\n")
            .append(departmentName).append(" - ").append(regionName).append("\n")
            .append(countryName)

        val finalAddress = addressStringBuilder.toString()
        Timber.i("buildAndLogAddress() | You are located to: $finalAddress")
        return finalAddress
    }
}
