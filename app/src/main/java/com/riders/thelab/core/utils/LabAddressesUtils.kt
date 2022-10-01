package com.riders.thelab.core.utils

import android.location.Address
import android.location.Geocoder
import android.location.Location
import timber.log.Timber
import java.io.IOException

object LabAddressesUtils {

    fun getDeviceAddress(geocoder: Geocoder, location: Location): Address? {
        Timber.i("getDeviceAddress")
        try {
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
            Timber.e("addresses : %s", addresses)

            //get the address
            return addresses?.get(0)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return null
    }


    suspend fun getRXAddress(
        geoCoder: Geocoder,
        latitude: Double,
        longitude: Double
    ): List<Address>? {
        var addressList: List<Address> = ArrayList()
        try {
            addressList = geoCoder.getFromLocation(latitude, longitude, 1)!!
        } catch (exception: Exception) {
            Timber.e(exception)
        }
        return if (addressList.isEmpty()) {
            null
        } else {
            addressList
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
