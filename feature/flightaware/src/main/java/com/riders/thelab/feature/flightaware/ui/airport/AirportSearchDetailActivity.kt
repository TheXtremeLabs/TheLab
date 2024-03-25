package com.riders.thelab.feature.flightaware.ui.airport

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.component.Lottie
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
                                targetState = null != mViewModel.airportID && null != mViewModel.airportModel,
                                label = "transition"
                            ) { targetState ->
                                if (!targetState) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .zIndex(5f), contentAlignment = Alignment.Center
                                    ) {
                                        Lottie(
                                            modifier = Modifier.fillMaxSize(),
                                            url = "https://assets2.lottiefiles.com/packages/lf20_kk62um5v.json"
                                        )
                                    }
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