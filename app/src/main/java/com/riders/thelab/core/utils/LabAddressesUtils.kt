package com.riders.thelab.core.utils

import android.location.Address
import android.location.Geocoder
import android.location.Location
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.util.*

class LabAddressesUtils private constructor() {
    companion object {

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
                return addresses[0]
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            return null
        }


        fun getRXAddress(
            geoCoder: Geocoder,
            latitude: Double,
            longitude: Double
        ): Single<List<Address>> {
            return object : Single<List<Address>>() {
                override fun subscribeActual(observer: SingleObserver<in List<Address>>) {
                    var addressList: List<Address> = ArrayList()
                    try {
                        addressList = geoCoder.getFromLocation(latitude, longitude, 1)
                    } catch (exception: Exception) {
                        Timber.e(exception)
                    }
                    if (addressList.isEmpty()) {
                        observer.onSuccess(addressList)
                    } else {
                        observer.onError(Throwable())
                    }
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        fun buildAddress(address: Address): String? {
            val finalCity = ""
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
            Timber.d(addressStringBuilder.toString())
            return locality
        }
    }
}