package com.riders.thelab.feature.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.riders.thelab.core.common.bus.KotlinBus
import com.riders.thelab.core.common.bus.Listen
import com.riders.thelab.core.common.location.LabLocationManager
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.location.GPSProvidersResultModel
import com.riders.thelab.core.location.LabLocationReceiver
import com.riders.thelab.core.permissions.Permission
import com.riders.thelab.core.permissions.PermissionManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.data.local.bean.SnackBarType
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.weather.core.worker.WeatherDownloadWorker
import com.riders.thelab.feature.weather.data.compose.WeatherUiModel
import com.riders.thelab.feature.weather.data.compose.WeatherUiState
import com.riders.thelab.feature.weather.ui.WeatherViewModel.Companion.URL_REQUEST
import com.riders.thelab.feature.weather.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

@AndroidEntryPoint
class WeatherActivity : BaseComponentActivity(), LocationListener {

    private val mWeatherViewModel: WeatherViewModel by viewModels<WeatherViewModel>()

    private var mLabLocationManager: LabLocationManager? = null
    private val mLabLocationReceiver: LabLocationReceiver by lazy { LabLocationReceiver() }


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)

        subscribeToKotlinBus()

        mWeatherViewModel.initWeakReference(activity = this@WeatherActivity)

        checkLocationPermissions()
    }

    override fun onPause() {
        Timber.d("onPause()")
        super.onPause()

        if (hasLocationPermissions()) {
            mLabLocationManager?.stopUsingGPS()
        }

        mLabLocationReceiver?.let { unregisterReceivers(it) }
    }

    public override fun onResume() {
        super.onResume()
        Timber.d("onResume()")

        registerReceivers(mLabLocationReceiver to LabLocationReceiver.getIntentFilters())

        if (hasLocationPermissions()) {
            registerLabLocationManager()

            mLabLocationManager?.let {
                updateLocationIcon(it.canGetLocation())
                if (!it.canGetLocation()) {
                    Timber.e("!it.canGetLocation() | WeatherUIState.Error()")
                    // TODO : Show snackbar info message cannot get user's location
                    // mWeatherViewModel.updateWeatherDataState(WeatherDataState.Error())
                }

//                if (!mWeatherViewModel.hasWeatherLocalData()) {
//                    mWeatherViewModel.fetchCities(this@WeatherActivity)
//                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    override fun onDestroy() {
        Timber.e("onDestroy()")
        super.onDestroy()
    }


    /////////////////////////////////////
    //
    // BUS METHODS
    //
    /////////////////////////////////////
    @OptIn(DelicateCoroutinesApi::class)
    @Listen
    fun onLocationProvidersChanged() {
        lifecycleScope.launch {
            KotlinBus.subscribe<GPSProvidersResultModel> { result ->
                Timber.d("onLocationProvidersChanged() | ${result.toString()}")
                updateLocationIcon(result.isGPS)
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

        PermissionManager
            .from(this@WeatherActivity)
            .request(Permission.Location)
            .rationale("Location is needed to discover some features")
            .checkPermission { granted: Boolean ->

                if (!granted) {
                    Timber.e("Permissions are denied. User may access to app with limited location related features")
                    UIManager.showToast(
                        this,
                        "Permissions are denied. User may access to app with limited location related features"
                    )
                } else {
                    initViewModelObservers()

                    // Start a coroutine in the lifecycle scope
                    lifecycleScope.launch {
                        // repeatOnLifecycle launches the block in a new coroutine every time the
                        // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            setContent {
                                // Register lifecycle events
                                mWeatherViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                                val theme: AppTheme by mWeatherViewModel
                                    .theme
                                    .collectAsStateWithLifecycle()
                                val isDarkTheme: Boolean? by mWeatherViewModel
                                    .isDarkMode
                                    .collectAsStateWithLifecycle()

                                val hasInternetConnection by mWeatherViewModel.hasInternetConnection.collectAsStateWithLifecycle()

                                val weatherUiState: WeatherUiState by mWeatherViewModel.weatherUiState.collectAsStateWithLifecycle()
                                val citySearch by mWeatherViewModel.searchText.collectAsStateWithLifecycle()
                                /*val citySearchQuery by mWeatherViewModel.citySearchQuery.collectAsStateWithLifecycle(
                                    initialValue = emptyList()
                                )*/

                                TheLabTheme(
                                    theme = theme,
                                    darkTheme = isDarkTheme ?: isSystemInDarkTheme()
                                ) {
                                    // A surface container using the 'background' color from the theme
                                    Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.background
                                    ) {
                                        WeatherContent(
                                            theme = theme,
                                            darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                            weatherUiState = weatherUiState,
                                            iconState = mWeatherViewModel.iconState,
                                            searchMenuExpanded = mWeatherViewModel.expanded,
                                            searchCityQuery = citySearch,
                                            suggestions = mWeatherViewModel.suggestions,
                                            isWeatherMoreDataVisible = mWeatherViewModel.isWeatherMoreDataVisible,
                                        ) { event ->
                                            when (event) {
                                                is UiEvent.OnMyLocationClicked -> fetchCurrentLocation()
                                                else -> mWeatherViewModel.onEvent(event)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
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

        if (null == mLabLocationManager) {
            mLabLocationManager = LabLocationManager(
                activity = this@WeatherActivity,
                locationListener = this@WeatherActivity
            )
        }

        mLabLocationManager?.let { locationManager ->

            if (!locationManager.canGetLocation()) {
                Timber.e("Cannot get location please enable position")

                // TODO : Should show alert with compose dialog
                // mLabLocationManager?.showSettingsAlert()
            } else {
                locationManager.setLocationListener(this)
                locationManager.getCurrentLocation()
            }
        } ?: run { Timber.e("Lab location object is null") }
    }


    fun fetchCurrentLocation() {
        Timber.d("fetchCurrentLocation()")
        if (null == mLabLocationManager || mLabLocationManager?.canGetLocation() == false) {
            UIManager.showActionInSnackBar(
                this,
                "Cannot get location please enable device's position setting.",
                SnackBarType.ALERT,
                getString(com.riders.thelab.core.ui.R.string.action_ok)
            ) { }

            return
        }

        val location = mLabLocationManager?.getCurrentLocation() ?: return
        mWeatherViewModel.fetchWeather((location.latitude to location.longitude).toLocation())
    }

    private fun updateLocationIcon(iconState: Boolean) {
        Timber.d("updateLocationIcon() | state: $iconState")

        if (!iconState) {
            mWeatherViewModel.updateIconState(false)
            //mWeatherViewModel.updateWeatherDataState(WeatherDataState.Error())
        } else {
            mWeatherViewModel.updateIconState(true)
//            mWeatherViewModel.fetchCities(this@WeatherActivity)
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
    // WORKER
    //
    /////////////////////////////////////
    /**
     * Launch Worker that will manage download and extraction of the cities zip file from bulk openweather server
     */
    @SuppressLint("RestrictedApi")
    fun startWork() {
        Timber.d("startWork()")

        val workerConstraints: Constraints = Constraints.Builder()
            .apply {
                setRequiredNetworkType(NetworkType.CONNECTED)
                setRequiresBatteryNotLow(true)
                setRequiresCharging(false)
                setRequiresStorageNotLow(true)
            }
            .build()

        val weatherCitiesWorkRequest: WorkRequest =
            OneTimeWorkRequest.Builder(WeatherDownloadWorker::class.java)
                .setConstraints(workerConstraints)
                .setInputData(
                    Data.Builder()
                        .putString(
                            URL_REQUEST,
                            Constants.BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD + Constants.WEATHER_BULK_DOWNLOAD_URL
                        )
                        .build()
                )
                .addTag(WeatherDownloadWorker::class.java.simpleName)
                .build()

        val id = weatherCitiesWorkRequest.id

        WorkManager
            .getInstance(this)
            .enqueue(weatherCitiesWorkRequest)

        runOnUiThread {
            listenToTheWorker(id)
        }
    }


    private fun listenToTheWorker(workerId: UUID) {
        Timber.d("listenToTheWorker() | ID : $workerId")

        lifecycleScope.launch {
            WorkManager
                .getInstance(this@WeatherActivity)
                .getWorkInfoByIdFlow(workerId)
                .collect { workInfo: WorkInfo? ->
                    workInfo?.let {
                        when (it.state) {
                            WorkInfo.State.ENQUEUED -> Timber.d("listenToTheWorker() | Worker ENQUEUED")
                            WorkInfo.State.RUNNING -> {
                                Timber.d("listenToTheWorker() | Worker RUNNING")
//                            workerStatus.value = WorkInfo.State.RUNNING
//                            updateWeatherDataState(WeatherDataState.Loading)
                            }

                            WorkInfo.State.SUCCEEDED -> {
                                Timber.d("listenToTheWorker() | Worker SUCCEEDED")
                                mWeatherViewModel.updateWeatherUIState(
                                    WeatherUiState.Success(
                                        WeatherUiModel(cities = mWeatherViewModel.getCitiesSync())
                                    )
                                )
                            }

                            WorkInfo.State.FAILED -> {
                                Timber.e("listenToTheWorker() | Worker FAILED")

                                runOnUiThread {
                                    UIManager.showActionInSnackBar(
                                        this@WeatherActivity,
                                        "Worker FAILED",
                                        SnackBarType.ALERT,
                                        "",
                                        null
                                    )
                                }
                            }

                            WorkInfo.State.BLOCKED -> Timber.e("listenToTheWorker() | Worker BLOCKED")
                            WorkInfo.State.CANCELLED -> Timber.e("listenToTheWorker() | Worker CANCELLED")
                            else -> {
                                Timber.e("listenToTheWorker() | else branch")
                                //updateWeatherDataState(WeatherDataState.Error())
                            }
                        }
                    } ?: run {
                        Timber.e("listenToTheWorker() | WorkInfo is null")
                        //updateWeatherDataState(WeatherDataState.Error())
                    }
                }
        }
    }

    fun clearBackgroundResources(activity: WeatherActivity) {
        cancelWorker(activity)
    }

    private fun cancelWorker(activity: WeatherActivity) {
        Timber.e("cancelWorker()")
        Timber.i("Worker is about to be cancelled")
        WorkManager
            .getInstance(activity)
            .cancelAllWork()
    }

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onLocationChanged(location: Location) {
        Timber.d("onLocationChanged : $location")
    }

    @Deprecated("Deprecated in Java")
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