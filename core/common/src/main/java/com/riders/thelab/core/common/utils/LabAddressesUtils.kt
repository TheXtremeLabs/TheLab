package com.riders.thelab.core.common.utils

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber
import java.io.IOException

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
        return try {
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
            Timber.e("addresses : %s", addresses)

            //get the address
            addresses?.get(0)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: NullPointerException) {
            e.printStackTrace()
            null
        }
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
            Timber.e("addresses : %s", address)

            callback(address)
        }
    }

    fun buildAddress(address: Address): String? {
        val addressStringBuilder = StringBuilder()
        val street = address.featureName + ", " + address.thoroughfare
        val locality = address.locality
        val postalCode = address.postalCode
        val departmentName = address.subAdminArea
        val regionName = address.adminArea
        val countryName = address.countryName
        addressStringBuilder
            .append(street).append(" - ")
            .append(locality).append(" - ")
            .append(postalCode).append(" - ")
            .append(departmentName).append(" - ")
            .append(regionName).append(" - ")
            .append(countryName)
        val finalAddress = addressStringBuilder.toString()
        Timber.d(finalAddress)
        return locality
    }
}
