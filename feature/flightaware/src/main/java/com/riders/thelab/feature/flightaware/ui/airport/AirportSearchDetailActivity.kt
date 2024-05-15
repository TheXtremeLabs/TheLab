package com.riders.thelab.feature.flightaware.ui.airport

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.core.theme.backgroundColor
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {

                    // Register lifecycle events
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AnimatedContent(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = backgroundColor),
                                targetState = null != mViewModel.airportID && null != mViewModel.airportModel,
                                transitionSpec = { fadeIn() + slideInHorizontally() togetherWith fadeOut() + slideOutHorizontally() },
                                label = "transition",
                                contentAlignment = Alignment.Center
                            ) { targetState ->
                                if (!targetState) {
                                    LabLoader(modifier = Modifier.size(72.dp))
                                } else {
                                    AirportDetailContent(
                                        airportModel = mViewModel.airportModel!!,
                                        departureFlights = mViewModel.departureFlights,
                                        arrivalFlights = mViewModel.arrivalFlights,
                                        isFlightsFetched = mViewModel.isFlightsFetched,
                                        onFlightRequested = mViewModel::fetchFlights
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }

    companion object {
        var EXTRA_AIRPORT_ID = "EXTRA_AIRPORT_ID"
    }
}