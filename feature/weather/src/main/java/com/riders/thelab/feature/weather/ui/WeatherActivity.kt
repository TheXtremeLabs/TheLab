package com.riders.thelab.feature.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

            if (intent?.action != null && intent.action?.equals(LocationManager.PROVIDERS_CHANGED_ACTION) == true) {
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

        if (hasLocationPermissions()) {
            unregisterReceiver(mGpsSwitchStateReceiver)

            labLocationManager?.stopUsingGPS()
        }
    }

    public override fun onResume() {
        super.onResume()
        Timber.d("onResume()")

        if (hasLocationPermissions()) {
            registerReceiver(
                mGpsSwitchStateReceiver,
                IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            )

            registerLabLocationManager()

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
            .withContext(this@WeatherActivity)
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

                                val weatherUiState by mWeatherViewModel.weatherUiState.collectAsStateWithLifecycle()
                                val weatherCityUiState by mWeatherViewModel.weatherUiState.collectAsStateWithLifecycle()

                                TheLabTheme {
                                    // A surface container using the 'background' color from the theme
                                    Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.background
                                    ) {
                                        WeatherContent(mWeatherViewModel)
                                    }
                                }
                            }
                        }
                    }

                    /*if (LabCompatibilityManager.isOreo()) {
                        registerWeatherWidget()
                    }*/
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

    private fun hasLocationPermissions(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

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

        labLocationManager?.let { locationManager ->

            if (!locationManager.canGetLocation()) {
                Timber.e("Cannot get location please enable position")

                // TODO : Should show alert with compose dialog
                // labLocationManager?.showSettingsAlert()
            } else {
                locationManager.setLocationListener()
                locationManager.getCurrentLocation()
            }
        } ?: run { Timber.e("Lab location object is null") }
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

    /**
     * On devices running Android 8.0 (API level 26) and higher,
     * launchers that let users create pinned shortcuts also let them pin widgets onto their home screen. Similar to pinned shortcuts, these pinned widgets give users access to specific tasks in your app and can be added to the home screen directly from the app.
     */
    /*@SuppressLint("NewApi")
    fun registerWeatherWidget() {
        Timber.d("registerWeatherWidget()")
        val appWidgetManager = AppWidgetManager.getInstance(this@WeatherActivity)
        val myProvider = ComponentName(this@WeatherActivity, ExampleAppWidgetProvider::class.java)

        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            Timber.d("appWidgetManager.isRequestPinAppWidgetSupported true")
            // Create the PendingIntent object only if your app needs to be notified
            // that the user allowed the widget to be pinned. Note that, if the pinning
            // operation fails, your app isn't notified. This callback receives the ID
            // of the newly-pinned widget (EXTRA_APPWIDGET_ID).
            val successCallback = PendingIntent.getBroadcast(
                *//* context = *//* this,
                *//* requestCode = *//*
                0,
                *//* intent = *//*
                Intent(this, Uri.parse()MainActivity::class.java),
                *//* flags = *//*
                if (LabCompatibilityManager.isMarshmallow()) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
            )

            appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
        }
        }*/

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
        const val KEY_DESTINATION: String = "destination"
    }
}