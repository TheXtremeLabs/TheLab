package com.riders.thelab.ui.locationonmaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.res.Configuration
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabAddressesUtils
import com.riders.thelab.core.utils.LabLocationManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.bean.MapsEnum
import com.riders.thelab.databinding.ActivityLocationOnMapsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class LocationOnMapsActivity : AppCompatActivity(), OnMapReadyCallback,
    android.location.LocationListener {

    companion object {
        // location updates interval - 10sec
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

        // fastest updates interval - 5 sec
        // location updates will be received if another app is requesting the locations
        // than your app can handle
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
        private const val REQUEST_CHECK_SETTINGS = 100
    }

    private lateinit var viewBinding: ActivityLocationOnMapsBinding


    var mapFragment: MapFragment? = null
    var geocoder: Geocoder? = null
    private lateinit var mMap: GoogleMap
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mCriteria: Criteria? = null
    private var mProvider = ""

    // bunch of location related apis
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null

    // boolean flag to toggle the ui
    private var mRequestingLocationUpdates: Boolean? = null

    // location last updated time
    private var mLastUpdateTime: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        val nightModeFlags: Int = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                val w = window

                w.statusBarColor =
                    Color.TRANSPARENT
            }
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
        }

        super.onCreate(savedInstanceState)
        viewBinding = ActivityLocationOnMapsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.activity_title_location_on_maps)

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Timber.i("All permissions are granted")
                        mRequestingLocationUpdates = true
                        initLocationSettings()
                        setupMap()
                    } else {
                        Timber.e("All permissions are not granted")
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            })
            .withErrorListener { dexterError: DexterError -> Timber.e(dexterError.toString()) }
            .onSameThread()
            .check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.e("onActivityResult()")
        // Check for the integer request code originally supplied to startResolutionForResult().
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                RESULT_OK -> Timber.e("User agreed to make required location settings changes.")
                RESULT_CANCELED -> {
                    Timber.e("User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
                else -> {
                    super.onActivityResult(requestCode, resultCode, data)
                }
            }
        }
    }

    override fun onPause() {
        Timber.e("onPause()")
        if (mRequestingLocationUpdates!!) {
            // pausing location updates
            stopLocationUpdates()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        // Resuming location updates depending on button state and
        // allowed permissions
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates!!) {
            startLocationUpdates()
        }

        updateLocationUI()
    }


    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        Timber.i("onMapReady()")
        mMap = googleMap

        // Used for finding current location with button
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        /*
            source :  https://stackoverflow.com/questions/36785542/how-to-change-the-position-of-my-location-button-in-google-maps-using-android-st
         */
        if (mapFragment!!.view != null) {
            Timber.d("Get the button view")
            // Get the button view
            val locationButton =
                (mapFragment!!.view!!.findViewById<View>("1".toInt()).parent as View)
                    .findViewById<View>("2".toInt())
            Timber.d("and next place it, on bottom right (as Google Maps app)")
            // and next place it, on bottom right (as Google Maps app)
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            Timber.d("position on right bottom")
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 30, 260)
        }

        // Set a preference for minimum and maximum zoom.
        mMap.setMinZoomPreference(MapsEnum.WORLD.distance)
        mMap.setMaxZoomPreference(MapsEnum.DEFAULT_MAX_ZOOM.distance)
        mMap.moveCamera(CameraUpdateFactory.zoomTo(MapsEnum.WORLD.distance))
        Completable.complete()
            .delay(3, TimeUnit.SECONDS)
            .doOnComplete { this@LocationOnMapsActivity.runOnUiThread { this.setLocationSettings() } }
            .doOnError { t: Throwable? ->
                Timber.e(
                    t
                )
            }
            .doAfterTerminate { this@LocationOnMapsActivity.runOnUiThread { this.hideLoading() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onLocationChanged(location: Location) {

        // Remove markers
        mMap.clear()
        Timber.i("onLocationChanged()")
        val latitude = location.latitude
        val longitude = location.longitude
        Timber.e("Lat : %s - Lon : %s", latitude, longitude)
        val latLng = LatLng(latitude, longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(
                    geocoder?.let {
                        LabLocationManager.getDeviceLocationToString(
                            it,
                            location,
                            this
                        )
                    }
                )
        )
        mMap.isMyLocationEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(750f), 2000, null)
        mMap.uiSettings.isScrollGesturesEnabled = true
        viewBinding.tvLocation.text = "Latitude:$latitude\nLongitude:$longitude"
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Timber.d("onStatusChanged()")
    }

    override fun onProviderEnabled(provider: String) {
        Timber.d("onProviderEnabled()")
    }

    override fun onProviderDisabled(provider: String) {
        Timber.d("onProviderDisabled()")
    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun initLocationSettings() {
        Timber.i("initLocationSettings()")

        // Construct a FusedLocationProviderClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationUI()
            }
        }
        mRequestingLocationUpdates = false
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()

        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
        geocoder = Geocoder(this, Locale.getDefault())
    }

    private fun setupMap() {
        Timber.i("setupMap()")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.google_maps_fragment) as MapFragment?
        if (null == mapFragment) {
            mapFragment!!.getMapAsync(this)
        }
    }

    fun setLocationSettings() {
        Timber.i("setLocationSettings()")
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        mCriteria = Criteria()
        mProvider = mLocationManager!!.getBestProvider(mCriteria!!, true)!!
        if (null == mProvider) {
            Timber.e("Cannot get location please enable position")
            val labLocationManager = LabLocationManager(this)
            labLocationManager.showSettingsAlert()
        } else {
            try {
                mLocation = mLocationManager!!.getLastKnownLocation(mProvider)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            mLocation?.let { onLocationChanged(it) }
            try {
                mLocationManager!!.requestLocationUpdates(
                    mProvider,
                    20000,
                    0f,
                    this
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    private fun hideLoading() {
        if (viewBinding.rlMapsLoading.visibility == View.VISIBLE)
            startAnimation(viewBinding.rlMapsLoading)

    }

    fun startAnimation(view: View) {
        Timber.d("startAnimation()")
        val animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                Timber.d("onAnimationStart()")
            }

            override fun onAnimationEnd(animation: Animation) {
                Timber.d("onAnimationEnd()")
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {
                Timber.d("onAnimationRepeat()")
            }
        })
        val mAnimationSet = AnimationSet(true)
        mAnimationSet.interpolator = AccelerateInterpolator()
        mAnimationSet.addAnimation(animFadeOut)
        view.startAnimation(mAnimationSet)
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mSettingsClient
            ?.checkLocationSettings(mLocationSettingsRequest!!)
            ?.addOnSuccessListener(this) { locationSettingsResponse: LocationSettingsResponse? ->

                Timber.i("All location settings are satisfied.")
                UIManager.showActionInToast(this, "Started location updates!")

                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest!!,
                    mLocationCallback!!,
                    Looper.myLooper()!!
                )
                updateLocationUI()
            }
            ?.addOnFailureListener(this) { e: Exception ->
                val statusCode = (e as ApiException).statusCode
                if (statusCode == CommonStatusCodes.RESOLUTION_REQUIRED) {
                    Timber.i("Location settings are not satisfied. Attempting to upgrade location settings ")
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the
                        // result in onActivityResult().
                        val rae = e as ResolvableApiException
                        rae.startResolutionForResult(
                            this,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (sie: SendIntentException) {
                        Timber.i("PendingIntent unable to execute request.")
                    }
                } else if (statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    val errorMessage =
                        "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."
                    UIManager.showActionInToast(this, errorMessage)
                    Timber.e(errorMessage)
                }
                updateLocationUI()
            }
    }

    fun stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
            ?.removeLocationUpdates(mLocationCallback!!)
            ?.addOnCompleteListener(this) { task ->
                UIManager.showActionInToast(this, "Location updates stopped!")
            }
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            Timber.e("Lat: ${mCurrentLocation!!.latitude} , ${mCurrentLocation!!.longitude}")
            geocoder = Geocoder(this, Locale.getDefault())

            try {
                val address = LabAddressesUtils.getDeviceAddress(geocoder!!, mCurrentLocation!!)
                Timber.e("Address : %S", address?.countryName.orEmpty())

            } catch (ioException: IOException) {
                // Catch network or other I/O problems.
//            errorMessage = getString(R.string.service_not_available);
                Timber.e(ioException, "errorMessage")
            } catch (illegalArgumentException: IllegalArgumentException) {
                // Catch invalid latitude or longitude values.
//            errorMessage = getString(R.string.invalid_lat_long_used);
                Timber.e(
                    illegalArgumentException,
                    "errorMessage : Latitude = ${mCurrentLocation!!.latitude}, Longitude : ${mCurrentLocation!!.longitude}"
                )
            }
        }
    }
}