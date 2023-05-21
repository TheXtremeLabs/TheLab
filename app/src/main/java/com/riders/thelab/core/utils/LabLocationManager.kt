package com.riders.thelab.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.riders.thelab.core.bus.LocationFetchedEvent
import com.riders.thelab.core.bus.LocationProviderChangedEvent
import com.riders.thelab.ui.weather.WeatherActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.ref.WeakReference

class LabLocationManager constructor(context: Context) : LocationListener {

    private var mContext: Context = context

    // Declaring a Location Manager
    private lateinit var locationManager: LocationManager

    // flag for GPS status
    private var isGPSEnabled = false

    // flag for network status
    private var isNetworkEnabled = false

    // flag for GPS status
    private var canGetLocation = false

    // location
    private var location: Location? = null

    private var mLocationListener: LocationListener? = null

    private var mWeakReference: WeakReference<Activity>? = null

    constructor(activity: Activity, locationListener: LocationListener) : this(activity) {
        this.mContext = activity.applicationContext
        this.mWeakReference = WeakReference(activity)
        this.mLocationListener = locationListener
    }

    init {
        Timber.d("LabLocationManager | init")
        locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    fun setLocationListener() {
        mLocationListener = this
    }

    @WorkerThread
    fun getCurrentLocation(): Location? {
        Timber.d("getLocation1()")

        return if (!canGetLocation()) {
            // no network provider is enabled
            Timber.e("no network provider is enabled")
            null
        } else {
            try {
                // if Network Enabled get lat/long using Network
                if (isNetworkEnabled) {
                    getLocationViaNetwork()
                }

                this.location?.let { postLocation(it) }

                // return location object
                return this.location
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    getLocationViaGPS()
                }

                this.location?.let { postLocation(it) }

                // return location object
                return this.location
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationViaNetwork() {
        mContext?.let {
            ContextCompat.getMainExecutor(it).execute {
                // This is where your UI code goes.
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    mLocationListener!!
                )
            }
        }

        Timber.d("Network Enabled")
        this.location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
    }

    @SuppressLint("MissingPermission")
    private fun getLocationViaGPS() {
        mContext?.let {
            ContextCompat.getMainExecutor(it).execute {
                // This is where your UI code goes.
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    mLocationListener!!

                )
            }
        }

        Timber.d("GPS Enabled")
        this.location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
    }

    private fun postLocation(location: Location) {
        Timber.d("postLocation() | location: $location")

        // Post event with cll info object set
        GlobalScope.launch(Main) {
            LocationFetchedEvent(location).triggerEvent()
        }
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    fun stopUsingGPS() {
        locationManager.removeUpdates(this@LabLocationManager)
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

        mWeakReference?.let { ref ->
            /* val activity: Activity? =*/

            // Get activity from weak reference activity object
            ref.get()?.let { activity ->
                when (activity) {
                    is WeatherActivity -> {
                        activity.updateLocationIcon(canGetLocation)
                    }

                    else -> {
                        Timber.e("Else branch")
                    }
                }
            }
        }

        return canGetLocation
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    fun showSettingsAlert() {
        mContext?.let {
            val alertDialog = AlertDialog.Builder(it)

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
                it.startActivity(intent)
            }

            // on pressing cancel button
            alertDialog.setNegativeButton(
                "Cancel"
            ) { dialog: DialogInterface, _: Int -> dialog.cancel() }

            // Showing Alert Message
            alertDialog.show()
        }
    }

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onLocationChanged(location: Location) {
        Timber.d("onLocationChanged : $location")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Timber.d("onStatusChanged : $provider, $status")
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onProviderDisabled(provider: String) {
        Timber.e("onProviderDisabled() | provider: $provider")
        mWeakReference?.let { ref ->

            // Get activity from weak reference activity object
            Timber.e("Get activity from weak reference activity object and call its methods")
            ref.get()?.let { activity ->
                when (activity) {
                    is WeatherActivity -> {
                        Timber.d("call WeatherActivity methods")
                        activity.updateLocationIcon(false)
                        activity.lifecycleScope.launch {
                            LocationProviderChangedEvent().triggerEvent(false)
                        }
                    }

                    else -> {
                        Timber.e("Else branch")
                    }
                }
            }
        }
        /*GlobalScope.launch {
            LocationProviderChangedEvent().triggerEvent(false)
        }*/
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onProviderEnabled(provider: String) {
        Timber.d("onProviderEnabled() | provider: $provider")
        mWeakReference?.let { ref ->

            // Get activity from weak reference activity object
            Timber.d("Get activity from weak reference activity object and call its methods")
            ref.get()?.let { activity ->
                when (activity) {
                    is WeatherActivity -> {
                        Timber.d("call WeatherActivity methods")
                        activity.updateLocationIcon(true)
                        activity.lifecycleScope.launch {
                            LocationProviderChangedEvent().triggerEvent(true)
                        }
                    }

                    else -> {
                        Timber.e("Else branch")
                    }
                }
            }
        }
    }


    /////////////////////////////////////
    //
    // COMPANION / INNER CLASSES
    //
    /////////////////////////////////////
    companion object {
        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute
    }
}

