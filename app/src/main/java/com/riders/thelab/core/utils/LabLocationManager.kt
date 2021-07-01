package com.riders.thelab.core.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.riders.thelab.core.bus.LocationFetchedEvent
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.util.*


class LabLocationManager constructor(
    private var mActivity: Activity,
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
        mActivity.getSystemService(LOCATION_SERVICE) as LocationManager

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
        locationManager = mActivity.getSystemService(LOCATION_SERVICE) as LocationManager
        mLocationListener = this
//        getLocation()
    }

    constructor(
        activity: Activity,
        context: Context,
        locationListener: LocationListener
    ) : this(activity, context) {
        this.mActivity = activity
        this.mContext = context
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

    @JvmName("getLocation1")
    fun getLocation(): Location? {

        // run dexter permission
        Dexter
            .withContext(mActivity)
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

                            EventBus.getDefault().post(location?.let { LocationFetchedEvent(it) })
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener { error: DexterError ->
                UIManager.showActionInToast(mContext, "Error occurred! $error")
            }
            .onSameThread()
            .check()


        // return location object
        return this.location
    }

    @SuppressLint("MissingPermission")
    private fun getLocationViaNetwork() {
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
            mLocationListener!!
        )
        Timber.d("Network Enabled")
        this.location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
        latitude = this.location!!.latitude
        longitude = this.location!!.longitude
    }


    @SuppressLint("MissingPermission")
    private fun getLocationViaGPS() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
            mLocationListener!!
        )
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
        ) { dialog: DialogInterface?, which: Int ->
            val intent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            mContext.startActivity(intent)
        }

        // on pressing cancel button
        alertDialog.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }

        // Showing Alert Message
        alertDialog.show()
    }
}

