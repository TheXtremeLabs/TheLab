package com.riders.thelab.core.common.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import timber.log.Timber
import java.io.IOException
import java.util.Locale

fun Pair<Double, Double>.toLocation(): Location = Location("").apply {
    latitude = first
    longitude = second
}

object LabLocationUtils {

    fun buildTargetLocationObject(latitude: Double, longitude: Double): Location {
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        return location
    }


    fun getDeviceLocationToString(
        geocoder: Geocoder,
        location: Location
    ): String {
        Timber.i("getDeviceLocationToString")
        var finalAddress = "" //This is the complete address.
        val latitude = location.latitude
        val longitude = location.longitude

        //get the address
        val addressStringBuilder = StringBuilder()
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            Timber.e("addresses : %s", addresses)
            val address = addresses?.get(0)
            val street = address?.featureName + ", " + address?.thoroughfare
            val locality = address?.locality
            val postalCode = address?.postalCode
            val departmentName = address?.subAdminArea
            val regionName = address?.adminArea
            val countryName = address?.countryName
            addressStringBuilder
                .append(street).append(" - ")
                .append(locality).append(" - ")
                .append(postalCode).append(" - ")
                .append(departmentName).append(" - ")
                .append(regionName).append(" - ")
                .append(countryName)
            finalAddress = addressStringBuilder.toString() //This is the complete address.
            Timber.e("Address : %s", finalAddress) //This will display the final address.
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return finalAddress
    }

    fun getDeviceLocationWithRX(location: Location, context: Context): String? {
        val latitude = location.latitude
        val longitude = location.longitude
        val finalCity = arrayOfNulls<String>(1) //This is the complete address.
        //get the address
        val geoCoder = Geocoder(context, Locale.getDefault())
        val addressStringBuilder = StringBuilder()
        val addresses = getRXAddress(geoCoder, latitude, longitude)

        addresses?.let {

            for (element in it) {
                Timber.e("element : %s", element.toString())
            }

            val address = it[0]
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

            finalCity[0] = address.locality //This is the complete address.

            return if (finalCity[0]?.isNotEmpty() == true) {
                finalCity[0]
            } else {
                Timber.e("value are empty")
                ""
            }
        }
        return null
    }

    private fun getRXAddress(
        geoCoder: Geocoder,
        latitude: Double,
        longitude: Double
    ): List<Address>? {
        val addressList = geoCoder.getFromLocation(latitude, longitude, 1)
        return if (addressList?.isNotEmpty() == true) {
            addressList
        } else {
            null
        }
    }
}