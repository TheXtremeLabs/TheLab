package com.riders.thelab.ui.mainactivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.ComponentActivity
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.riders.thelab.R
import com.riders.thelab.TheLabApplication
import com.riders.thelab.core.broadcast.LocationBroadcastReceiver
import com.riders.thelab.core.bus.LocationFetchedEvent
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabLocationManager
import com.riders.thelab.core.common.network.LabNetworkManagerNewAPI
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.interfaces.ConnectivityListener
import com.riders.thelab.core.location.GpsUtils
import com.riders.thelab.core.location.OnGpsListener
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.LabGlideUtils
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.databinding.ActivityMainBinding
import com.riders.thelab.feature.weather.ui.WeatherUtils
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.mainactivity.fragment.exit.ExitDialog
import com.riders.thelab.utils.Constants.GPS_REQUEST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MainActivity : ComponentActivity(),
    CoroutineScope,
    View.OnClickListener,
    ConnectivityListener, LocationListener, OnGpsListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: ActivityMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: MainActivityViewModel by viewModels()

    private lateinit var navigator: Navigator

    // Location
    private var labLocationManager: LabLocationManager? = null
    private var locationReceiver: LocationBroadcastReceiver? = null
    private lateinit var mGpsUtils: GpsUtils
    private var isGPS: Boolean = false
    private var lastKnowLocation: Location? = null

    // Network
    private var mConnectivityManager: ConnectivityManager? = null
    private lateinit var networkManager: LabNetworkManagerNewAPI

    // Time
    private var isTimeUpdatedStarted: Boolean = false
    private var isConnected: Boolean = true


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w = window
        w.allowEnterTransitionOverlap = true

        // In Activity's onCreate() for instance
        // make fully Android Transparent Status bar
        when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                w.statusBarColor = Color.TRANSPARENT
            }

            Configuration.UI_MODE_NIGHT_NO,
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
        }
        window.navigationBarColor = ContextCompat.getColor(this, R.color.default_dark)

        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        // setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainContent(viewModel = mViewModel)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkLocationPermissions()
            }
        }
    }

    override fun onPause() {
        Timber.e("onPause()")

        // Unregister Event Bus
//        EventBus.getDefault().unregister(this)

        // Unregister Connectivity Manager
        try {
            if (null != mConnectivityManager) {
                mConnectivityManager?.unregisterNetworkCallback(networkManager)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Unregister Location receiver
        try {
            if (null != locationReceiver) {
                // View Models implementation
                // don't forget to remove receiver data source
                mViewModel.removeDataSource(locationReceiver!!.getLocationStatus())
                unregisterReceiver(locationReceiver)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        // Stop update timer
        if (isTimeUpdatedStarted) {
            isTimeUpdatedStarted = false
        }

        labLocationManager?.stopUsingGPS()

        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume()")

        // Register Network callback events
        registerConnectivityManager()

        // Register Lab Location manager
        registerLabLocationManager()

        updateTime()
        startViewSwitcher()

        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)

        // View Models implementation
        // add data source
//        mViewModel.addDataSource(locationReceiver.getLocationStatus())
//        registerReceiver(locationReceiver, intentFilter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GPS_REQUEST) {
            isGPS = true // flag maintain before get location
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        ExitDialog(this).apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
            .show()
    }


    override fun onDestroy() {
        Timber.d("onDestroy()")
        Timber.d("unregister network callback()")
        try {
            networkManager.let { mConnectivityManager?.unregisterNetworkCallback(it) }
        } catch (exception: RuntimeException) {
            Timber.e("NetworkCallback was already unregistered")
        }
        super.onDestroy()

        _viewBinding = null
    }


    /////////////////////////////////////
    //
    // BUS
    //
    /////////////////////////////////////
    @DelicateCoroutinesApi
    //@Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationFetchedEventResult(event: LocationFetchedEvent) {
        Timber.e("onLocationFetchedEvent()")
        val location: Location = event.location
        val latitude = location.latitude
        val longitude = location.longitude
        Timber.e("$latitude, $longitude")

        lastKnowLocation = location

        if (this.isConnected) {
            mViewModel.fetchWeather(this@MainActivity, latitude, longitude)
        } else {
            Timber.e("Not connected to the internet. Cannot perform network action")
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
            .withContext(this@MainActivity)
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

                    navigator = Navigator(this@MainActivity)

                    retrieveApplications()

                    // Variables
                    initActivityVariables()

                    registerLocationReceiver()
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

    private fun initActivityVariables() {
        Timber.d("initActivityVariables()")

        mConnectivityManager =
            this@MainActivity.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        networkManager = LabNetworkManagerNewAPI(this@MainActivity)
        locationReceiver = LocationBroadcastReceiver()
        mGpsUtils = GpsUtils(this@MainActivity)

        labLocationManager =
            LabLocationManager(
                activity = this@MainActivity,
                locationListener = this@MainActivity
            )
    }

    private fun registerLocationReceiver() {
        Timber.d("registerLocationReceiver()")

        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)

        if (null != locationReceiver) {
            // View Models implementation
            // add data source
            mViewModel.addDataSource(locationReceiver!!.getLocationStatus())
            registerReceiver(locationReceiver, intentFilter)
        }
    }

    private fun startViewSwitcher() = CoroutineScope(Dispatchers.Main).launch {
        Timber.d("startViewSwitcher()")

        val viewSwitcher = binding.includeContentLayout.vs
        val welcomeToTheLabContainer = binding.includeContentLayout.llWelcomeToTheLabContainer
        val timeContainer = binding.includeContentLayout.llTimeContainer

        while (isTimeUpdatedStarted) {
            delay(TimeUnit.SECONDS.toMillis(10))

            if (viewSwitcher.currentView != welcomeToTheLabContainer) {
                // Show firstView
                viewSwitcher.inAnimation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_in_top)
                viewSwitcher.outAnimation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_out_down)
                viewSwitcher.displayedChild = 0
//                viewSwitcher.showPrevious()
            } else if (viewSwitcher.currentView != timeContainer) {
                // Show secondView
                viewSwitcher.inAnimation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_in_down)
                viewSwitcher.outAnimation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_out_top)
                viewSwitcher.displayedChild = 1
//                viewSwitcher.showNext()
            }
        }
    }

    private fun registerConnectivityManager() {
        Timber.d("registerConnectivityManager()")
        // register connection status listener
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Register Network callback events
        if (null == mConnectivityManager) {
            Timber.e("Connectivity Manager is null | Cannot register network callback events")
        } else {
            mConnectivityManager?.registerNetworkCallback(request, networkManager)
        }
    }

    private fun registerLabLocationManager() {
        Timber.d("registerLabLocationManager()")
        if (null == labLocationManager) {
            Timber.e("Lab Location Manager is null | Cannot register location callback events")
        } else {
            if (!labLocationManager!!.canGetLocation()) {
                Timber.e("Cannot get location please enable position")

                binding.includeToolbarLayout.ivLocationStatus.setBackgroundResource(
                    R.drawable.ic_location_off
                )
                // TODO : Should show alert with compose dialog
                // labLocationManager?.showSettingsAlert()
            } else {
                labLocationManager?.setLocationListener()
                labLocationManager?.getCurrentLocation()

                binding.includeToolbarLayout.ivLocationStatus.setBackgroundResource(
                    R.drawable.ic_location_on
                )
            }
        }
    }

    private fun updateTime() = CoroutineScope(Dispatchers.Main).launch {
        Timber.d("updateTime()")

        isTimeUpdatedStarted = true

        while (isTimeUpdatedStarted) {
            val date = Date(System.currentTimeMillis())
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
            binding.includeContentLayout.tvTime.text = time

            delay(1000)
        }
    }

    @DelicateCoroutinesApi
    private fun initViewModelsObservers() {

        mViewModel
            .getConnectionStatus()
            .observe(
                this
            ) { connectionStatus ->
                UIManager.showConnectionStatusInSnackBar(this, connectionStatus)
                updateToolbarConnectionIcon(connectionStatus)
            }

        mViewModel
            .getConnectionStatus()
            .observe(
                this
            ) { connectionStatus ->
                UIManager.showConnectionStatusInSnackBar(this, connectionStatus)
                updateToolbarConnectionIcon(connectionStatus)
            }

        mViewModel
            .getLocationData()
            .observe(
                this
            ) { locationStatus ->
                Timber.d("getLocationData().observe : $locationStatus")

            }

        mViewModel.getWeather().observe(
            this
        ) {
            Timber.d("getWeather().observe : $it")

            LabGlideUtils.getInstance().loadImage(
                this@MainActivity,
                WeatherUtils.getWeatherIconFromApi(it.weatherIconUrl),
                binding.includeContentLayout.ivWeatherIcon
            )

            val sb: StringBuilder =
                StringBuilder()
                    .append(it.temperature)
                    .append(" °c,")
                    .append(" ")
                    .append(it.city)

            // bind data
            binding.includeContentLayout.tvWeather.text = sb.toString()
        }
    }

    private fun retrieveApplications() {
        mViewModel.retrieveApplications(TheLabApplication.getInstance().getContext())
        mViewModel.retrieveRecentApps(TheLabApplication.getInstance().getContext())
    }

    private fun toggleLocation() {
        Timber.e("toggleLocation()")
        if (!isGPS) mGpsUtils.turnGPSOn(this)
    }

    @SuppressLint("InlinedApi")
    private fun toggleWifi() {
        Timber.d("toggleWifi()")
        if (LabCompatibilityManager.isAndroid10()) {
            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            @Suppress("DEPRECATION")
            startActivityForResult(panelIntent, 0)
        } else {
            // use previous solution, add appropriate permissions to AndroidManifest file (see answers above)
            (this.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager)
                ?.apply {
                    // isWifiEnabled = true /*or false*/
                    if (!isWifiEnabled) {
                        Timber.d("(this.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager) $isWifiEnabled")
                        Timber.d("This should activate wifi")

                        @Suppress("DEPRECATION")
                        isWifiEnabled = true

                    } else {
                        Timber.d("(this.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager) $isWifiEnabled")
                        Timber.d("This should disable wifi")

                        @Suppress("DEPRECATION")
                        isWifiEnabled = false

                    }
                    @Suppress("DEPRECATION")
                    this.isWifiEnabled = !isWifiEnabled
                }
        }
    }

    private fun toggleRecyclerViewLinearLayout() {
        Timber.d("toggleRecyclerViewLinearLayout()")

        // clear staggered icon
        UIManager.setBackgroundColor(
            this,
            binding.includeContentLayout.ivStaggeredLayout,
            R.color.transparent
        )

        //Apply selected background color
        UIManager.setBackgroundColor(
            this,
            binding.includeContentLayout.ivLinearLayout,
            R.color.teal_700
        )
        // applyRecycler()
    }

    private fun toggleRecyclerViewStaggeredLayout() {
        Timber.d("toggleRecyclerViewStaggeredLayout()")

        // clear staggered icon
        UIManager.setBackgroundColor(
            this,
            binding.includeContentLayout.ivLinearLayout,
            R.color.transparent
        )

        //Apply selected background color
        UIManager.setBackgroundColor(
            this,
            binding.includeContentLayout.ivStaggeredLayout,
            R.color.teal_700
        )
        // applyRecycler()
    }

    fun launchApp(item: App) {
        Timber.d("launchApp : $item")
        when {
            item is LocalApp && item.title?.lowercase()?.contains("drive") == true -> {
                UIManager.showActionInToast(
                    this@MainActivity,
                    "Please check this functionality later. Problem using Drive REST API v3"
                )
                return
            }

            item is LocalApp && -1L != item.id -> {
                mViewModel.launchActivityOrPackage(navigator, item)
            }

            item is PackageApp -> {
                mViewModel.launchActivityOrPackage(navigator, item)
            }

            else -> {
                Timber.e("Item id == -1 , not app activity. Should launch package intent.")
            }
        }
    }


    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    /*override fun onMenuItemClick(item: MenuItem?): Boolean {
        Timber.d("onMenuItemClick()")
        when (item?.itemId) {
            R.id.action_connection_settings -> networkManager.changeWifiState(
                this.applicationContext,
                this@MainActivity
            )
            R.id.action_location_settings -> if (!isGPS) mGpsUtils.turnGPSOn(this)
            R.id.action_settings -> UIManager.showActionInToast(this, "Settings clicked")
            R.id.action_force_crash -> throw RuntimeException("This is a crash")
            else -> {
            }
        }
        return true
    }*/

    /////////// OnClick Listener ///////////
    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.iv_internet_status -> {
                Timber.e("Internet wifi icon status clicked")
            }

            R.id.iv_location_status -> {
                Timber.e("Location icon status clicked")
            }

            R.id.iv_settings -> navigator.callSettingsActivity()
            R.id.btn_more_info -> navigator.callWeatherActivity()
            R.id.iv_linear_layout -> toggleRecyclerViewLinearLayout()
            R.id.iv_staggered_layout -> toggleRecyclerViewStaggeredLayout()
        }
    }

    override fun onConnected() {
        UIManager.showConnectionStatusInSnackBar(this@MainActivity, true)
        updateToolbarConnectionIcon(true)

    }

    override fun onLostConnection() {
        UIManager.showConnectionStatusInSnackBar(this@MainActivity, false)
        updateToolbarConnectionIcon(false)

    }

    private fun updateToolbarConnectionIcon(isConnected: Boolean) {
        Timber.e("updateToolbarConnectionIcon, is connected : %s", isConnected)
        if (!LabCompatibilityManager.isTablet(this))

            runOnUiThread {
                try {

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
    }

    override fun gpsStatus(isGPSEnable: Boolean) {
        Timber.d("gpsStatus()")
        Timber.d("turn on/off GPS - isGPSEnable : $isGPSEnable")
        isGPS = isGPSEnable
    }

    override fun onLocationChanged(location: Location) {
        Timber.d("$location")
    }
}