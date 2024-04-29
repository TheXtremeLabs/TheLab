package com.riders.thelab.feature.flightaware.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.data.local.model.compose.SearchFlightUiState
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.ui.flight.FlightDetailActivity
import com.riders.thelab.feature.flightaware.utils.Constants
import kotlinx.coroutines.launch
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class SearchFlightActivity : BaseComponentActivity() {

    private val mViewModel: SearchFlightViewModel by viewModels<SearchFlightViewModel>()
    private val locale = Locale.getDefault()
    private var currentDate: NotBlankString? = null

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupCurrentDate()

        mViewModel.getBundle(intent)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {

                    val uiState by mViewModel.searchFlightUiState.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AnimatedContent(
                                modifier = Modifier.fillMaxSize(),
                                targetState = uiState,
                                contentAlignment = Alignment.Center,
                                label = "search_flight_animation_content"
                            ) { targetState ->
                                when (targetState) {
                                    is SearchFlightUiState.Loading -> {
                                        LabLoader(modifier = Modifier.size(40.dp))
                                    }

                                    is SearchFlightUiState.Success -> {
                                        SearchFlightsContent(
                                            currentDate = currentDate!!,
                                            flights = targetState.flights,
                                            uiEvent = {
                                                when (it) {
                                                    is UiEvent.OnFlightClicked -> {
                                                        launchFlightDetail(it.flightModel)
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
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
    }

    override fun backPressed() {
        finish()
    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun setupCurrentDate() {
        Timber.d("setupCurrentDate()")
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN, locale)
        val formattedDate = now.format(formatter)

        currentDate = NotBlankString.create(formattedDate)
    }

    private fun launchFlightDetail(flightModel: FlightModel) =
        Intent(this@SearchFlightActivity, FlightDetailActivity::class.java)
            .apply { this.putExtra(Constants.EXTRA_FLIGHT, flightModel) }
            .run { startActivity(this) }

    companion object {
        const val DATE_FORMAT_PATTERN = "d MMM uuuu"
    }
}