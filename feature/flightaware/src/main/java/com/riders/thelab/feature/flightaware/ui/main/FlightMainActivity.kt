package com.riders.thelab.feature.flightaware.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FlightMainActivity : BaseComponentActivity() {

    private val mViewModel: FlightViewModel by viewModels<FlightViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {

                    var searchType by remember { mutableIntStateOf(0) }

                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

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
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("onBackPressed()")
        finish()
    }
}