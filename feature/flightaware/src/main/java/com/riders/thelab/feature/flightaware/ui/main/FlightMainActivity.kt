package com.riders.thelab.feature.flightaware.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.permissions.Permission
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.data.local.model.SearchFlightType
import com.riders.thelab.feature.flightaware.utils.FlightNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

// TODO : Package organization
// TODO : flight (search id and route ; list and details)
// TODO : airports (search and details)

@AndroidEntryPoint
class FlightMainActivity : BaseComponentActivity() {

    private val mViewModel: FlightMainViewModel by viewModels<FlightMainViewModel>()

    var mFlightNavigator: FlightNavigator? = null
        private set


    private var continueWithBlock: Pair<Boolean, () -> Unit> = false to {}

    override var permissionLauncher: ActivityResultLauncher<Array<String>>? =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { areGranted: Map<String, Boolean> ->
            if (!areGranted.values.all { it }) {
                Timber.e("$areGranted permissions is NOT granted")
            } else {
                Timber.d("$areGranted permissions is granted")

                if (continueWithBlock.first) {
                    continueWithBlock.second.invoke()

                    continueWithBlock = false to {}
                }
            }
        }


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initVariables()

        checkPermissions()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val hasInternetConnection by mViewModel.hasInternetConnection.collectAsStateWithLifecycle()
                    val locationState by mViewModel.mLabLocationManager.locationState.collectAsStateWithLifecycle()

                    val departureAirportsFlow by mViewModel.departureAirportStateFlow.collectAsStateWithLifecycle()
                    val arrivalAirportsFlow by mViewModel.arrivalAirportStateFlow.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            FlightMainContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                hasConnection = hasInternetConnection,
                                isLocationEnabled = mViewModel.mLabLocationManager.canGetLocation,
                                searchPageIndex = mViewModel.searchPageIndex,
                                airportsNearBy = mViewModel.airportsNearBy,
                                isLoading = mViewModel.isAirportsNearByLoading,
                                departureExpanded = mViewModel.departureDropdownExpanded,
                                departureSuggestions = departureAirportsFlow,
                                arrivalExpanded = mViewModel.arrivalDropdownExpanded,
                                arrivalSuggestions = arrivalAirportsFlow,
                                uiEvent = { event ->
                                    Timber.i("uiEvent | ${event.javaClass.name}")

                                    // Call onEvent for FlightSearchViewModel as well
                                    when (event) {
                                        is UiEvent.OnFetchAirportNearBy -> {
                                            if (!hasLocationPermission()) {
                                                permissionLauncher?.launch(
                                                    Permission.Location
                                                        .permissions
                                                        .toList()
                                                        .toTypedArray()
                                                )

                                                mViewModel.initLocationManager(this@FlightMainActivity)

                                                continueWithBlock = true to {
                                                    // Call onEvent for ViewModel
                                                    mViewModel.onEvent(
                                                        uiEvent = event,
                                                        activity = this@FlightMainActivity
                                                    )
                                                }
                                            } else {
                                                mViewModel.initLocationManager(this@FlightMainActivity)
                                                // Call onEvent for ViewModel
                                                mViewModel.onEvent(
                                                    uiEvent = event,
                                                    activity = this@FlightMainActivity
                                                )
                                            }
                                        }

                                        is UiEvent.OnSearchFlightByID -> mFlightNavigator?.launchSearchFlightActivity(
                                            searchFlightType = SearchFlightType.NUMBER,
                                            flightId = event.id.toString()
                                        )

                                        is UiEvent.OnSearchFlightByRoute -> {
                                            mViewModel.departureAirportOptionSelected?.let { departure ->
                                                mViewModel.arrivalAirportOptionSelected?.let { arrival ->
                                                    mFlightNavigator?.launchSearchFlightActivity(
                                                        searchFlightType = SearchFlightType.ROUTE,
                                                        flightRoute = departure.icaoCode!!.toString() to arrival.icaoCode!!.toString()
                                                    )
                                                } ?: run {
                                                    Timber.e("onEvent() | onSearchFlightByRoute | arrivalAirportOptionSelected is null")
                                                }
                                            } ?: run {
                                                Timber.e("onEvent() | onSearchFlightByRoute | departureAirportOptionSelected is null")
                                            }
                                        }

                                        else -> {
                                            // Call onEvent for ViewModel
                                            mViewModel.onEvent(uiEvent = event)
                                        }
                                    }
                                },
                            )
                        }
                    }

                    LaunchedEffect(departureAirportsFlow) {
                        Timber.d("LaunchedEffect | departure Airports Flow value: $departureAirportsFlow | coroutineContext: ${this.coroutineContext}")
                        mViewModel.onEvent(UiEvent.OnDepartureExpanded(departureAirportsFlow.isNotEmpty()))
                    }
                    LaunchedEffect(arrivalAirportsFlow) {
                        Timber.d("LaunchedEffect | arrival Airports Flow value: $arrivalAirportsFlow | coroutineContext: ${this.coroutineContext}")
                        mViewModel.onEvent(UiEvent.OnArrivalExpanded(arrivalAirportsFlow.isNotEmpty()))
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
    }

    override fun onResume() {
        Timber.d("onResume()")
        mViewModel.isResumed = true
        super.onResume()
    }

    override fun backPressed() {
        Timber.e("backPressed()")

        if (mViewModel.departureDropdownExpanded) {
            mViewModel.updateDepartureExpanded(false)
            return
        }

        finish()
    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    private fun initVariables() {
        mFlightNavigator = FlightNavigator(this)
        initViewModels()
    }

    private fun initViewModels() {

        mViewModel.initWeakReference(this)

        if (hasLocationPermission() || true == mViewModel.mLabLocationManager?.canGetLocation()) {
            mViewModel.initLocationManager(this@FlightMainActivity)
        }
    }

    private fun checkPermissions() {
        if (!hasLocationPermission()) {
            permissionLauncher?.launch(
                Permission.Location
                    .permissions
                    .toList()
                    .toTypedArray()
            )
        }
    }

    private fun hasLocationPermission(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

}