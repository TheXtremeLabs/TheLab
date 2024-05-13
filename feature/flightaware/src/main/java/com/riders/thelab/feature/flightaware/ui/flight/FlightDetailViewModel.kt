package com.riders.thelab.feature.flightaware.ui.flight

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.compose.FlightDetailUiState
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.feature.flightaware.ui.main.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import timber.log.Timber

class FlightDetailViewModel : BaseViewModel() {

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _flightDetailUiState: MutableStateFlow<FlightDetailUiState> =
        MutableStateFlow(FlightDetailUiState.Loading)
    var flightDetailUiState: StateFlow<FlightDetailUiState> = _flightDetailUiState

    private fun updateUiState(newState: FlightDetailUiState) {
        this._flightDetailUiState.value = newState
    }


    @OptIn(ExperimentalKotoolsTypesApi::class)
    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    fun getBundle(intent: Intent) {
        Timber.d("getBundle()")

        // Try to get bundle values
        intent.extras?.let { bundle: Bundle ->
            val extraItem: SearchFlightModel? = if (!LabCompatibilityManager.isTiramisu()) {
                bundle.getSerializable(FlightDetailActivity.EXTRA_FLIGHT) as SearchFlightModel?
            } else {
                bundle.getSerializable(
                    FlightDetailActivity.EXTRA_FLIGHT,
                    SearchFlightModel::class.java
                )
            }

            extraItem?.let { item: SearchFlightModel ->
                // Log
                Timber.d("item : $item")
                updateUiState(FlightDetailUiState.Success(item))
            } ?: run {
                Timber.e("Extra recycler view item object is null")
                updateUiState(FlightDetailUiState.Error(NotBlankString.create("Error occurred while getting value")))
            }
        } ?: run {
            Timber.e("Intent extras are null")
            updateUiState(FlightDetailUiState.Error(NotBlankString.create("Error occurred while getting value")))
        }
    }

    fun onEvent(uiEvent: UiEvent) {
        Timber.d("onEvent() | uiEvent: $uiEvent")
    }
}