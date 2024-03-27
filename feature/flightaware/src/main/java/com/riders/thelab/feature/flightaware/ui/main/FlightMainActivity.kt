package com.riders.thelab.feature.flightaware.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.ui.airport.AirportSearchActivity
import com.riders.thelab.feature.flightaware.viewmodel.FlightSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

// TODO : Implement splashscreen palette
// IMPORTANT : add some blue like the flight aware logo

@AndroidEntryPoint
class FlightMainActivity : BaseComponentActivity() {

    private val mViewModel: FlightViewModel by viewModels<FlightViewModel>()
    private val mFlightSearchViewModel: FlightSearchViewModel by viewModels<FlightSearchViewModel>()

    private var mLabNetworkManager: LabNetworkManager? = null


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLabNetworkManager = LabNetworkManager
            .getInstance(this, lifecycle)
            .also { mViewModel.observeNetworkState(it) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    // Register lifecycle for viewModel
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    var searchType by remember { mutableIntStateOf(0) }

                    val departureAirportsFlow by mFlightSearchViewModel.departureAirportStateFlow.collectAsStateWithLifecycle()
                    val arrivalAirportsFlow by mFlightSearchViewModel.arrivalAirportStateFlow.collectAsStateWithLifecycle()

                    val departureTextFieldState = rememberTextFieldState()
                    val arrivalTextFieldState = rememberTextFieldState()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            FlightMainContent(
                                hasConnection = mViewModel.hasInternetConnection,
                                onSearchCategorySelected = {
                                    searchType = it
                                },
                                departureTextFieldState = departureTextFieldState,
                                arrivalTextFieldState = arrivalTextFieldState,
                                departureAirport = { departureAirport ->
                                    Timber.d("FlightMainContent | departureAirport : $departureAirport")
                                    // mViewModel.updateDepartureAirport(departureAirport)
                                    // Timber.d("FlightMainContent | mViewModel.departureAirport.value : ${mViewModel.departureAirport}")
                                },
                                arrivalAirport = { arrivalAirport ->
                                    Timber.d("FlightMainContent | arrivalAirport : $arrivalAirport")
                                },
                                onSearch = {
                                    when (searchType) {
                                        0 -> {
                                            //  mViewModel.getAirport()
                                        }

                                        1 -> {
                                            // mViewModel.getOperator()
                                        }
                                    }
                                },
                                departureExpanded = mViewModel.departureDropdownExpanded || departureAirportsFlow.isNotEmpty(),
                                onDepartureExpanded = mViewModel::updateDepartureExpanded,
                                onDepartureIconClicked = { },
                                departureSelectedText = mViewModel.departureAirportSelectedText
                                    ?: "",
                                onDepartureSelectedTextChanged = mViewModel::updateDepartureAirportSelectedText,
                                departureSuggestions = departureAirportsFlow
                            ) {
                                mViewModel::updateDepartureAirportOption
                                departureTextFieldState.setTextAndPlaceCursorAtEnd("${it.name.toString()} (${it.iataCode.toString()})")
                            }
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
    fun launchAirportSearchActivity() = Intent(this, AirportSearchActivity::class.java)
        .run { startActivity(this) }
}