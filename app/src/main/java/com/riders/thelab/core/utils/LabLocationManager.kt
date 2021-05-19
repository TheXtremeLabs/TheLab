package com.riders.thelab.core.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.*
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.riders.thelab.core.bus.LocationFetchedEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import lombok.SneakyThrows
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.io.IOException
import java.util.*

class LabLocationManager constructor(private val mContext: Context) : Service(), LocationListener {

    companion object {
        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute

        fun getDeviceLocationToString(geocoder: Geocoder, location: Location, context: Context): String? {
            Timber.i("getDeviceLocationToString")
            var finalAddress = "" //This is the complete address.
            val finalCity = "" //This is the complete address.
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

        fun getRXAddress(geoCoder: Geocoder, latitude: Double, longitude: Double): Single<List<Address>> {
            return object : Single<List<Address>>() {
                @SneakyThrows
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

    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    // flag for GPS status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS status
    var canGetLocation = false
    lateinit var location: Location // location

    var latitude = 0.0// latitude
    var longitude = 0.0// longitude
    private var mActivity: Activity? = null
    private var mLocationListener: LocationListener? = null

    init {
        locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    constructor(activity: Activity) : this(activity as Context) {
        mActivity = activity
        mLocationListener = this
        getLocation()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        Timber.d("onLocationChanged : $location")
    }

    override fun onProviderDisabled(provider: String) {
        Timber.e("onProviderDisabled")
    }

    override fun onProviderEnabled(provider: String) {
        Timber.d("onProviderEnabled")
    }

    fun setActivity(activity: Activity?) {
        mActivity = activity
    }

    fun setLocationListener() {
        mLocationListener = this
    }

    @JvmName("getLocation1")
    fun getLocation(): Location {
        locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

        // Check device API in order to run dexter permission or not
        if (!LabCompatibilityManager.isMarshmallow()) {
            if (!canGetLocation()) {
                // no network provider is enabled
                Timber.e("no network provider is enabled")
            } else {
                try {

                    // if Network Enabled get lat/long using Network
                    if (isNetworkEnabled) {
                        getLocationViaNetwork()
                    }

                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        getLocationViaGPS()
                    }
                    EventBus.getDefault().post(LocationFetchedEvent(location))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Dexter.withContext(mActivity)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(object : MultiplePermissionsListener {
                        @SuppressLint("MissingPermission")
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // do you work now
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied) {
                                // permission is denied permanently, navigate user to app settings
                            }
                            if (!canGetLocation()) {
                                // no network provider is enabled
                                Timber.e("no network provider is enabled")
                            } else {
                                try {

                                    // if Network Enabled get lat/long using Network
                                    if (isNetworkEnabled) {
                                        getLocationViaNetwork()
                                    }

                                    // if GPS Enabled get lat/long using GPS Services
                                    if (isGPSEnabled) {
                                        getLocationViaGPS()
                                    }
                                    EventBus.getDefault().post(LocationFetchedEvent(location))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                            token.continuePermissionRequest()
                        }
                    })
                    .withErrorListener { error: DexterError -> Toast.makeText(mActivity, "Error occurred! $error", Toast.LENGTH_SHORT).show() }
                    .onSameThread()
                    .check()
        }

        // return location object
        return location
    }

    @SuppressLint("MissingPermission")
    private fun getLocationViaNetwork() {
        locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                mLocationListener!!)
        Timber.d("Network Enabled")
        if (locationManager != null) {
            location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationViaGPS() {
        locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                mLocationListener!!)
        Timber.d("GPS Enabled")
        if (locationManager != null) {
            location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
            latitude = location.latitude
            longitude = location.longitude
        }

    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@LabLocationManager)
        }
    }

    /**
     * Function to get latitude
     */
    @JvmName("getLatitude1")
    fun getLatitude(): Double {
        latitude = location.latitude

        return latitude
    }

    /**
     * Function to get longitude
     */
    @JvmName("getLongitude1")
    fun getLongitude(): Double {
        longitude = location.longitude

        return longitude
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    fun canGetLocation(): Boolean {
        Timber.d("canGetLocation()")
        try {
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
        try {
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
        canGetLocation = isGPSEnabled && isNetworkEnabled
        return canGetLocation
    }

    fun getLocationObject(): Location? {
        return location
    }


    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings")

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings"
        ) { dialog: DialogInterface?, which: Int ->
            val intent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }

        // Showing Alert Message
        alertDialog.show()
    }
}