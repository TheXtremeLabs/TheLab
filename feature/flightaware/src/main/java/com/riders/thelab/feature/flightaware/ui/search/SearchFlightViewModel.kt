package com.riders.thelab.feature.flightaware.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.SearchFlightUiState
import com.riders.thelab.core.data.local.model.flight.FlightModel
import com.riders.thelab.core.data.local.model.flight.OperatorModel
import com.riders.thelab.core.data.local.model.flight.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import org.jsoup.Jsoup
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchFlightViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _searchFlightUiState: MutableStateFlow<SearchFlightUiState> =
        MutableStateFlow(SearchFlightUiState.Loading)
    var searchFlightUiState: StateFlow<SearchFlightUiState> = _searchFlightUiState

    private fun updateUiState(newState: SearchFlightUiState) {
        this._searchFlightUiState.value = newState
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)
        }



    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    fun getBundle(intent: Intent) {
        Timber.d("getBundle()")

        // Try to get bundle values
        intent.extras?.let {

            val searchType = it.getString(SearchFlightActivity.EXTRA_SEARCH_TYPE).toString()
            val extraItem: FlightModel? = if (!LabCompatibilityManager.isTiramisu()) {
                it.getSerializable(SearchFlightActivity.EXTRA_FLIGHT) as FlightModel?
            } else {
                it.getSerializable(
                    SearchFlightActivity.EXTRA_FLIGHT,
                    FlightModel::class.java
                )
            }

            when (searchType) {
                SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER -> {
                    extraItem?.let {
                        // Log
                        Timber.d("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER | item : $it")
                        updateUiState(SearchFlightUiState.SearchFlightByNumber(it))
                    }
                        ?: run { Timber.e("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER | Extra item object is null") }
                }

                SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE -> {
                    extraItem?.let {
                        // Log
                        Timber.d("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE | item : $it")
                        updateUiState(SearchFlightUiState.SearchFlightByRoute(it))

                        getAirlinesThumbnails(it)
                    }
                        ?: run { Timber.e("SearchFlightActivity.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE | Extra item object is null") }
                }

                else -> {
                    Timber.e("Unknown search type: $searchType")
                }
            }
        } ?: run { Timber.e("Intent extras are null") }
    }

    fun onEvent(uiEvent: UiEvent) {
        Timber.d("onEvent() | uiEvent: $uiEvent")

    }

    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun getAirlinesThumbnails(flightModel: FlightModel) {
        Timber.d("getAirlinesThumbnails()")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            flightModel.segments?.forEach { segmentModel ->
                val response: OperatorModel =
                    repository.getOperatorById(segmentModel.operatorICAO.toString()).toModel()

                val wikiResponse = repository.getWikimediaResponse(
                    NotBlankString.create(
                        response.wikiUrl.toString().split("/").last()
                    )
                )

                runCatching {
                    val body = Jsoup.parse(wikiResponse.parsed.text.toString()).body()
                    Timber.d("getAirlinesThumbnails() | body : $body with children size of ${body.childrenSize()}")

                }
                    .onFailure {
                        it.printStackTrace()
                        Timber.e("getAirlinesThumbnails() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.simpleName})")
                    }
                    .onSuccess {
                        Timber.d("getAirlinesThumbnails() | onSuccess | is success: $it")
                    }
            }
        }
    }
}