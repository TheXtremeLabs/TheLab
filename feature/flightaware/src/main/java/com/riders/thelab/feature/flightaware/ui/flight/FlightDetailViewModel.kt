package com.riders.thelab.feature.flightaware.ui.flight

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.feature.flightaware.ui.main.UiEvent
import timber.log.Timber

class FlightDetailViewModel : BaseViewModel() {

    var flight: FlightModel? by mutableStateOf(null)
        private set

    private fun updateFlight(newFlight: FlightModel) {
        this.flight = newFlight
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    fun getBundle(intent: Intent) {
        Timber.d("getBundle()")

        // Try to get bundle values
        intent.extras?.let {
            val extraItem: FlightModel? = if (!LabCompatibilityManager.isTiramisu()) {
                it.getSerializable(FlightDetailActivity.EXTRA_FLIGHT) as FlightModel?
            } else {
                it.getSerializable(FlightDetailActivity.EXTRA_FLIGHT, FlightModel::class.java)
            }

            extraItem?.let {
                // Log
                Timber.d("item : $it")
                updateFlight(it)
            } ?: run { Timber.e("Extra recycler view item object is null") }
        } ?: run { Timber.e("Intent extras are null") }
    }

    fun onEvent(uiEvent: UiEvent) {
        Timber.d("onEvent() | uiEvent: $uiEvent")

    }
}