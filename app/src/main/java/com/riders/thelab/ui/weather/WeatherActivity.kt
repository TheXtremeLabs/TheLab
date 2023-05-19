package com.riders.thelab.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.WorkInfo
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.riders.thelab.R
import com.riders.thelab.core.bus.KotlinBus
import com.riders.thelab.core.bus.Listen
import com.riders.thelab.core.bus.LocationProviderChangedEvent
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.*
import com.riders.thelab.data.local.bean.SnackBarType
import com.riders.thelab.data.local.model.compose.WeatherUIState
import com.riders.thelab.data.remote.dto.weather.CurrentWeather
import com.riders.thelab.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WeatherActivity : BaseActivity() {

    private val mWeatherViewModel: WeatherViewModel by viewModels()

    private var labLocationManager: LabLocationManager? = null

    private val mGpsSwitchStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {

            if (intent?.action != null && intent.action?.equals("android.location.LocationManager.PROVIDERS_CHANGED_ACTION") == true) {
                // Make an action or refresh an already managed state.
                Timber.d("CHANGED");
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        checkLocationPermissions()

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

    override fun onPause() {
        Timber.d("onPause()")
        super.onPause()

        unregisterReceiver(mGpsSwitchStateReceiver)
    }

    public override fun onResume() {
        super.onResume()
        Timber.d("onResume()")

        registerReceiver(
            mGpsSwitchStateReceiver,
            IntentFilter("android.location.LocationManager.PROVIDERS_CHANGED_ACTION")
        )

        if (null == labLocationManager || !labLocationManager!!.canGetLocation()) {
            mWeatherViewModel.updateUIState(WeatherUIState.Error())
            return
        }

        mWeatherViewModel.fetchCities(this@WeatherActivity)
    }

    override fun onDestroy() {
        Timber.e("onDestroy()")
        super.onDestroy()
    }

    /////////////////////////////////////
    //
    // BUS
    //
    /////////////////////////////////////
    @Listen
    fun onLocationProviderChanged() {
        Timber.e("onLocationProviderChanged()")
        lifecycleScope.launch {
            KotlinBus.subscribe<LocationProviderChangedEvent> {
                Timber.e("onLocationProviderChanged() | $it")

                /*if (!it) {

                } else {

                    val location: Location =
                        labLocationManager?.getCurrentLocation() ?: return@subscribe

                    mWeatherViewModel.updateUIState(WeatherUIState.SuccessWeatherData(true))
                    mWeatherViewModel.fetchWeather(
                        location.run { location.latitude to location.longitude }.toLocation()
                    )
                }*/
            }
        }
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

                    labLocationManager = LabLocationManager(this@WeatherActivity)
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
                    Timber.d("Succeed")
                }

                WorkInfo.State.FAILED -> {
                    Timber.e("Failed")
                }

                else -> {
                    Timber.e("else branch")
                }
            }
        }

    }

    fun fetchCurrentLocation() {
        Timber.d("fetchCurrentLocation()")

        if (labLocationManager?.canGetLocation() == true) {
            val location = labLocationManager?.getCurrentLocation() ?: return

            mWeatherViewModel.fetchWeather((location.latitude to location.longitude).toLocation())
        } else
            UIManager.showActionInSnackBar(
                this,
                findViewById(android.R.id.content),
                "Cannot get location please enable device's position setting.",
                SnackBarType.ALERT,
                getString(R.string.action_ok)
            ) { }
    }
}