package com.riders.thelab.core.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.riders.thelab.core.bus.LocationFetchedEvent
import org.greenrobot.eventbus.EventBus
import timber.log.Timber


class LabLocationManager constructor(
    private var mContext: Context
) : Service(), LocationListener {

    companion object {
        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute
    }

    // Declaring a Location Manager
    private var locationManager: LocationManager =
        mContext.getSystemService(LOCATION_SERVICE) as LocationManager

    // flag for GPS status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS status
    var canGetLocation = false

    // location
    var location: Location? = null

    var latitude = 0.0// latitude
    var longitude = 0.0// longitude
    private var mLocationListener: LocationListener? = null

    init {
        locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
        mLocationListener = this
    }

    constructor(
        context: Context,
        locationListener: LocationListener
    ) : this(context) {
        mLocationListener = this
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        Timber.d("onLocationChanged : $location")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Timber.d("onStatusChanged : $provider, $status")
    }

    override fun onProviderDisabled(provider: String) {
        Timber.e("onProviderDisabled")
    }

    override fun onProviderEnabled(provider: String) {
        Timber.d("onProviderEnabled")
    }

    fun setLocationListener() {
        mLocationListener = this
    }

    @WorkerThread
    @JvmName("getLocation1")
    fun getLocation(): Location? {
        Timber.d("getLocation1()")

        if (!canGetLocation()) {
            // no network provider is enabled
            Timber.e("no network provider is enabled")
        } else {

            try {
                // if Network Enabled get lat/long using Network
                if (isNetworkEnabled) {
                    getLocationViaNetwork()
                }
                EventBus.getDefault().post(location?.let { LocationFetchedEvent(it) })

            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    getLocationViaGPS()
                }
                EventBus.getDefault().post(location?.let { LocationFetchedEvent(it) })

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // return location object
        return this.location
    }

    @SuppressLint("MissingPermission")
    private fun getLocationViaNetwork() {
        ContextCompat.getMainExecutor(mContext).execute {
            // This is where your UI code goes.
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                mLocationListener!!
            )
        }


        Timber.d("Network Enabled")
        this.location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
        latitude = this.location!!.latitude
        longitude = this.location!!.longitude
    }


    @SuppressLint("MissingPermission")
    private fun getLocationViaGPS() {
        ContextCompat.getMainExecutor(mContext).execute {
            // This is where your UI code goes.
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                mLocationListener!!

            )
        }

        Timber.d("GPS Enabled")
        this.location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        latitude = this.location!!.latitude
        longitude = this.location!!.longitude
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    fun stopUsingGPS() {
        locationManager.removeUpdates(this@LabLocationManager)
    }

    /**
     * Function to get latitude
     */
    @JvmName("getLatitude1")
    fun getLatitude(): Double {
        latitude = this.location?.latitude!!

        return latitude
    }

    /**
     * Function to get longitude
     */
    @JvmName("getLongitude1")
    fun getLongitude(): Double {
        longitude = this.location?.longitude!!

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
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
        try {
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
        canGetLocation = isGPSEnabled && isNetworkEnabled
        return canGetLocation
    }

    fun getLocationObject(): Location? {
        return this.location
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
        alertDialog.setPositiveButton(
            "Settings"
        ) { _: DialogInterface?, _: Int ->
            val intent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            mContext.startActivity(intent)
        }

        // on pressing cancel button
        alertDialog.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }

        // Showing Alert Message
        alertDialog.show()
    }
}

