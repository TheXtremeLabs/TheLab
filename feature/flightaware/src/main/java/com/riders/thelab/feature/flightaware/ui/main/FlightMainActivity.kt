package com.riders.thelab.feature.flightaware.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.data.local.model.Permission
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.ui.airport.AirportSearchActivity
import com.riders.thelab.feature.flightaware.ui.airport.AirportSearchDetailActivity
import com.riders.thelab.feature.flightaware.viewmodel.FlightSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import timber.log.Timber

@AndroidEntryPoint
class FlightMainActivity : BaseComponentActivity() {

    private val mViewModel: FlightViewModel by viewModels<FlightViewModel>()
    private val mFlightSearchViewModel: FlightSearchViewModel by viewModels<FlightSearchViewModel>()

    private var mLabNetworkManager: LabNetworkManager? = null

    private var continueWithBlock: Pair<Boolean, () -> Unit> = false to {}

    override var permissionLauncher: ActivityResultLauncher<Array<String>>?
        get() = super.permissionLauncher
        set(value) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { areGranted: Map<String, Boolean> ->
                if (!areGranted.values.all { it }) {
                    Timber.e("Location permissions is NOT granted")
                } else {
                    Timber.d("Location permissions is granted")

                    if (continueWithBlock.first) {
                        continueWithBlock.second()

                        continueWithBlock = false to {}
                    }
                }
            }
        }


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLabNetworkManager = LabNetworkManager
            .getInstance(this, lifecycle)
            .also {
                mViewModel.observeNetworkState(it)
                mFlightSearchViewModel.observeNetworkState(it)
            }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    val departureAirportsFlow by mFlightSearchViewModel.departureAirportStateFlow.collectAsStateWithLifecycle()
                    val arrivalAirportsFlow by mFlightSearchViewModel.arrivalAirportStateFlow.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            FlightMainContent(
                                hasConnection = mViewModel.hasInternetConnection,
                                searchPageIndex = mViewModel.searchPageIndex,
                                uiEvent = { event ->
                                    Timber.d("uiEvent | $event")

                                    // Call onEvent for ViewModel
                                    mViewModel.onEvent(event)

                                    // Call onEvent for FlightSearchViewModel as well
                                    when (event) {
                                        is UiEvent.OnFetchAirportNearBy -> {
                                            if (!hasLocationPermission()) {
                                                permissionLauncher?.launch(
                                                    Permission.Location.permissions.toList()
                                                        .toTypedArray()
                                                )
                                                continueWithBlock = true to {
                                                    mFlightSearchViewModel.onEvent(
                                                        uiEvent = event,
                                                        activity = this@FlightMainActivity
                                                    )
                                                }
                                            } else {
                                                mFlightSearchViewModel.initLocationManager(this@FlightMainActivity)
                                                mFlightSearchViewModel.onEvent(
                                                    uiEvent = event,
                                                    activity = this@FlightMainActivity
                                                )
                                            }
                                        }

                                        else -> {

                                            mFlightSearchViewModel.onEvent(
                                                uiEvent = event,
                                                activity = null
                                            )
                                        }
                                    }
                                },
                                airportsNearBy = mFlightSearchViewModel.airportsNearBy,
                                isLoading = mFlightSearchViewModel.isAirportsNearByLoading,
                                departureExpanded = mViewModel.departureDropdownExpanded || departureAirportsFlow.isNotEmpty(),
                                onDepartureExpanded = mViewModel::updateDepartureExpanded,
                                departureSuggestions = departureAirportsFlow,
                                arrivalExpanded = mViewModel.arrivalDropdownExpanded || departureAirportsFlow.isNotEmpty(),
                                onArrivalExpanded = mViewModel::updateArrivalExpanded,
                                arrivalSuggestions = arrivalAirportsFlow
                            )
                        }
                    }

                    LaunchedEffect(departureAirportsFlow) {
                        Timber.d("LaunchedEffect | departure Airports Flow value: $departureAirportsFlow | coroutineContext: ${this.coroutineContext}")

                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("onBackPressed()")

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
    private fun hasLocationPermission(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    fun launchAirportSearchActivity() = Intent(this, AirportSearchActivity::class.java)
        .run { startActivity(this) }

    fun launchAirportDetail(airportID: String) =
        Intent(this, AirportSearchDetailActivity::class.java)
            .apply { this.putExtra(AirportSearchDetailActivity.EXTRA_AIRPORT_ID, airportID) }
            .run { startActivity(this) }
}