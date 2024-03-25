package com.riders.thelab.feature.flightaware.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
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
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.ui.airport.AirportSearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FlightMainActivity : BaseComponentActivity() {

    private val mViewModel: FlightViewModel by viewModels<FlightViewModel>()


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {

                    var searchType by remember { mutableIntStateOf(0) }

                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val depAirportFlow by mViewModel.departureAirportStateFlow.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            FlightMainContent(
                                airportsSize = mViewModel.airports.size,
                                onSearchCategorySelected = {
                                    searchType = it
                                },
                                departureAirport = { departureAirport ->
                                    Timber.d("FlightMainContent | departureAirport : $departureAirport")
                                    mViewModel.updateDepartureAirport(departureAirport)
                                    Timber.d("FlightMainContent | mViewModel.departureAirport.value : ${mViewModel.departureAirport}")
                                },
                                arrivalAirport = { arrivalAirport ->
                                    Timber.d("FlightMainContent | arrivalAirport : $arrivalAirport")
                                },
                                onSearch = {
                                    when (searchType) {
                                        0 -> {
                                            mViewModel.getAirport()
                                        }

                                        1 -> {
                                            mViewModel.getOperator()
                                        }
                                    }
                                }
                            )
                        }
                    }

                    LaunchedEffect(key1 = depAirportFlow) {
                        Timber.d("LaunchedEffect | depAirportFlow: $depAirportFlow | coroutineContext: ${this.coroutineContext}")

                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("onBackPressed()")
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