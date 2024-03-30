package com.riders.thelab.feature.flightaware.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.ui.airport.AirportSearchActivity
import com.riders.thelab.feature.flightaware.viewmodel.FlightSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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
            .also {
                mViewModel.observeNetworkState(it)
                mFlightSearchViewModel.observeNetworkState(it)
            }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    var searchType by remember { mutableIntStateOf(0) }

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
                                onSearchCategorySelected = {
                                    searchType = it
                                    if (0 == it && mViewModel.departureDropdownExpanded) {
                                        mViewModel.updateDepartureExpanded(false)
                                    }
                                },
                                flightNumberQuery = mFlightSearchViewModel.flightNumber,
                                onUpdateFlightNumber = mFlightSearchViewModel::updateFlightNumber,
                                onSearch = {
                                    when (searchType) {
                                        0 -> mFlightSearchViewModel.searchFlightByFlightNumber()
                                        1 -> {
                                            if(null == mViewModel.departureAirportOptionSelected?.icaoCode ) {
                                                return@FlightMainContent
                                            }
                                            if(null == mViewModel.arrivalAirportOptionSelected?.icaoCode) {
                                                return@FlightMainContent
                                            }

                                            mFlightSearchViewModel.searchFlightByRoute(
                                                departureAirportCode = mViewModel.departureAirportOptionSelected!!.icaoCode!!.toString(),
                                                arrivalAirportCode = mViewModel.arrivalAirportOptionSelected!!.icaoCode!!.toString()
                                            )
                                        }
                                    }
                                },
                                departureExpanded = mViewModel.departureDropdownExpanded || departureAirportsFlow.isNotEmpty(),
                                onDepartureExpanded = mViewModel::updateDepartureExpanded,
                                departureQuery = mFlightSearchViewModel.departureAirportQuery,
                                onUpdateDepartureQuery = mFlightSearchViewModel::updateDepartureAirportQuery,
                                departureSuggestions = departureAirportsFlow,
                                onDepartureOptionsSelected = {
                                    mViewModel.updateDepartureAirportOption(it)
                                    mFlightSearchViewModel.isOptionSelectedByUser = true
                                    mFlightSearchViewModel.updateDepartureAirportQuery("${it.name.toString()} ${if (null == it.iataCode) "" else "(${it.iataCode?.toString()})"}")
                                    mViewModel.updateDepartureExpanded(false)
                                },
                                arrivalExpanded = mViewModel.arrivalDropdownExpanded || departureAirportsFlow.isNotEmpty(),
                                onArrivalExpanded = mViewModel::updateArrivalExpanded,
                                arrivalQuery = mFlightSearchViewModel.arrivalAirportQuery,
                                onUpdateArrivalQuery = mFlightSearchViewModel::updateArrivalAirportQuery,
                                arrivalSuggestions = arrivalAirportsFlow
                            ) {
                                mViewModel.updateArrivalAirportOption(it)
                                mFlightSearchViewModel.isOptionSelectedByUser = true
                                mFlightSearchViewModel.updateArrivalAirportQuery("${it.name.toString()} ${if (null == it.iataCode) "" else "(${it.iataCode?.toString()})"}")
                                mViewModel.updateArrivalExpanded(false)
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