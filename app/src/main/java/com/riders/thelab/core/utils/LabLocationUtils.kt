package com.riders.thelab.core.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.util.*

class LabLocationUtils private constructor() {
    companion object {
        fun buildTargetLocationObject(latitude: Double, longitude: Double): Location {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude
            return location
        }


        fun getDeviceLocationToString(
            geocoder: Geocoder,
            location: Location,
            context: Context
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
                val address = addresses[0]
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
                finalAddress = addressStringBuilder.toString() //This is the complete address.
                Timber.e("Address : %s", finalAddress) //This will display the final address.
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            return finalAddress
        }

        fun getDeviceLocationWithRX(location: Location, context: Context): Single<String> {
            val latitude = location.latitude
            val longitude = location.longitude
            val finalCity = arrayOfNulls<String>(1) //This is the complete address.
            //get the address
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addressStringBuilder = StringBuilder()
            return object : Single<String>() {
                override fun subscribeActual(observer: SingleObserver<in String>) {
                    getRXAddress(geoCoder, latitude, longitude)
                        .subscribe(object : DisposableSingleObserver<List<Address>>() {
                            override fun onSuccess(addresses: List<Address>) {
                                for (element in addresses) {
                                    Timber.e("element : %s", element.toString())
                                }
                                val address = addresses[0]
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
                                if (finalCity[0]?.isNotEmpty() == true) {
                                    observer.onSuccess(finalCity[0])
                                } else {
                                    Timber.e("value are empty")
                                }
                            }

                            override fun onError(e: Throwable) {
                                observer.onError(e)
                            }
                        })
                }
            }
        }

        fun getRXAddress(
            geoCoder: Geocoder,
            latitude: Double,
            longitude: Double
        ): Single<List<Address>> {
            return object : Single<List<Address>>() {
                override fun subscribeActual(observer: SingleObserver<in List<Address>>) {
                    val addressList = geoCoder.getFromLocation(latitude, longitude, 1)
                    if (addressList.isNotEmpty()) {
                        observer.onSuccess(addressList)
                    } else {
                        observer.onError(Throwable())
                    }
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

    }
}