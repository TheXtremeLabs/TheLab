package com.riders.thelab.feature.flightaware.ui.airport

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AirportSearchDetailActivity : BaseComponentActivity() {

    private val mViewModel: AirportSearchDetailViewModel by viewModels<AirportSearchDetailViewModel>()

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getBundle(intent)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    // Register lifecycle events
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val airportDetailUiState by mViewModel.airportDetailUiState.collectAsStateWithLifecycle()
                    val departuresUiState by mViewModel.departureFlights.collectAsStateWithLifecycle()
                    val arrivalsUiState by mViewModel.arrivalFlights.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AirportDetailContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                airportDetailUiState = airportDetailUiState,
                                departuresUiState = departuresUiState,
                                arrivalsUiState = arrivalsUiState,
                                isFlightsFetched = mViewModel.isFlightsFetched,
                                onFlightRequested = mViewModel::fetchFlights
                            )
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }
}