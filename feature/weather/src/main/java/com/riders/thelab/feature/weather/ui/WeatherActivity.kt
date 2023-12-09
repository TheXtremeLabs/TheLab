package com.riders.thelab.feature.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.WorkInfo
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.riders.thelab.core.common.utils.LabLocationManager
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.data.local.model.compose.WeatherUIState
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.data.SnackBarType
import com.riders.thelab.core.ui.utils.UIManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WeatherActivity : ComponentActivity(), LocationListener {

    private val mWeatherViewModel: WeatherViewModel by viewModels()

    private var labLocationManager: LabLocationManager? = null

    private val mGpsSwitchStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {

            if (intent?.action != null && intent.action?.equals(LOCATION_PROVIDERS_ACTION) == true) {
                // Make an action or refresh an already managed state.
                Timber.d("CHANGED")
            }
        }
    }

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        checkLocationPermissions()
    }

    override fun onPause() {
        Timber.d("onPause()")
        super.onPause()

        unregisterReceiver(mGpsSwitchStateReceiver)

        labLocationManager?.stopUsingGPS()
    }

    public override fun onResume() {
        super.onResume()
        Timber.d("onResume()")

        registerReceiver(mGpsSwitchStateReceiver, IntentFilter(LOCATION_PROVIDERS_ACTION))

        registerLabLocationManager()

        // mWeatherViewModel.fetchCities(this@WeatherActivity)

        labLocationManager?.let {
            updateLocationIcon(it.canGetLocation())

            if (!it.canGetLocation()) {
                Timber.e("!it.canGetLocation() | WeatherUIState.Error()")
                mWeatherViewModel.updateUIState(WeatherUIState.Error())
                return
            } else {
                mWeatherViewModel.fetchCities(this@WeatherActivity)
            }
        }
    }

    override fun onDestroy() {
        Timber.e("onDestroy()")
        super.onDestroy()
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun checkLocationPermissions() {
        Timber.d("checkLocationPermissions()")
        // run dexter permission
        Dexter
            .withContext(this)
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

                    initViewModelObservers()

                    // Start a coroutine in the lifecycle scope
                    lifecycleScope.launch {
                        // repeatOnLifecycle launches the block in a new coroutine every time the
                        // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            setContent {
                                TheLabTheme {
                                    // A surface container using the 'background' color from the theme
                                    Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.background
                                    ) {
                                        WeatherContent(mWeatherViewModel, labLocationManager!!)
                                    }
                                }
                            }
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
                UIManager.showActionInToast(this, "Error occurred! $error")
            }
            .onSameThread()
            .check()
    }

    @SuppressLint("NewApi")
    fun initViewModelObservers() {
        Timber.d("initViewModelObservers()")

        mWeatherViewModel.getWorkerStatus().observe(this) {
            when (it) {
                WorkInfo.State.SUCCEEDED -> {
                    Timber.d("getWorkerStatus().observe | Succeed")
                }

                WorkInfo.State.FAILED -> {
                    Timber.e("getWorkerStatus().observe | Failed")
                }

                else -> {
                    Timber.e("getWorkerStatus().observe | else branch")
                }
            }
        }
    }

    private fun registerLabLocationManager() {
        Timber.d("registerLabLocationManager()")

        if (null == labLocationManager) {
            labLocationManager = LabLocationManager(
                activity = this@WeatherActivity,
                locationListener = this@WeatherActivity
            )
        }

        if (!labLocationManager!!.canGetLocation()) {
            Timber.e("Cannot get location please enable position")

            // TODO : Should show alert with compose dialog
            // labLocationManager?.showSettingsAlert()
        } else {
            labLocationManager?.setLocationListener()
            labLocationManager?.getCurrentLocation()
        }
    }

    fun fetchCurrentLocation() {
        Timber.d("fetchCurrentLocation()")
        if (null == labLocationManager || labLocationManager?.canGetLocation() == false) {
            UIManager.showActionInSnackBar(
                this,
                "Cannot get location please enable device's position setting.",
                SnackBarType.ALERT,
                getString(com.riders.thelab.core.ui.R.string.action_ok)
            ) { }

            return
        }

        val location = labLocationManager?.getCurrentLocation() ?: return
        mWeatherViewModel.fetchWeather((location.latitude to location.longitude).toLocation())
    }

    private fun updateLocationIcon(iconState: Boolean) {
        Timber.d("updateLocationIcon() | state: $iconState")

        if (!iconState) {
            mWeatherViewModel.updateIconState(false)
            mWeatherViewModel.updateUIState(WeatherUIState.Error())
        } else {
            mWeatherViewModel.updateIconState(true)
            mWeatherViewModel.fetchCities(this@WeatherActivity)
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

    override fun onProviderDisabled(provider: String) {
        Timber.e("onProviderDisabled() | provider: $provider")/*
        updateLocationIcon(false)
        lifecycleScope.launch {
            LocationProviderChangedEvent().triggerEvent(false)
        }*/
    }


    override fun onProviderEnabled(provider: String) {
        Timber.d("onProviderEnabled() | provider: $provider")/*
        updateLocationIcon(true)
        lifecycleScope.launch {
            LocationProviderChangedEvent().triggerEvent(true)
        }*/
    }

    companion object {
        private const val LOCATION_PROVIDERS_ACTION: String =
            "android.location.LocationManager.PROVIDERS_CHANGED_ACTION"
    }
}