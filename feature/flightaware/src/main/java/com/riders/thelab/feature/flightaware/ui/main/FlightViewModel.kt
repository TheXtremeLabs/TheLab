package com.riders.thelab.feature.flightaware.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.flight.AirportModel
import com.riders.thelab.core.data.local.model.flight.OperatorModel
import com.riders.thelab.core.data.local.model.flight.toModel
import com.riders.thelab.core.data.remote.dto.flight.AirportSearch
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel(), DefaultLifecycleObserver {

    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////
    var airports: SnapshotStateList<AirportModel> = mutableStateListOf<AirportModel>()
    var operators: SnapshotStateList<OperatorModel> = mutableStateListOf<OperatorModel>()

    var departureAirport by mutableStateOf("")
        private set

    var departureAirportStateFlow: StateFlow<List<AirportSearch>> =
        snapshotFlow { departureAirport }
            .debounce(750)
            .distinctUntilChanged()
            .mapLatest {
                if (it.isEmpty()) {
                    return@mapLatest emptyList()
                }

                Timber.d("departureAirportStateFlow | mapLatest | it: ${it}")
                val list = repository.searchAirportById(it).airports
                Timber.d("result: $list")
                list
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    var arrivalAirport = MutableStateFlow("")
        private set
    var arrivalAirportStateFlow: StateFlow<List<AirportSearch>> =
        snapshotFlow { arrivalAirport }.debounce(750).mapLatest {
            val list = repository.searchAirportById(it.value).airports
            Timber.d("result: $list")
            list
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var currentAirport: AirportModel? by mutableStateOf(null)
        private set

    var currentOperator: OperatorModel? by mutableStateOf(null)
        private set

    fun updateDepartureAirport(airport: String) {
        this.departureAirport = airport
    }

    private fun updateAirportList(newList: List<AirportModel>) {
        this.airports.addAll(newList)
    }

    private fun updateOperators(newList: List<OperatorModel>) {
        this.operators.addAll(newList)
    }

    private fun updateAirport(newAirport: AirportModel) {
        this.currentAirport = newAirport
    }

    private fun updateOperator(newOperator: OperatorModel) {
        this.currentOperator = newOperator
    }

    /////////////////////////////////////
    // Coroutine
    /////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e("CoroutineExceptionHandler | Error caught with message: ${throwable.message}")
        }


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Timber.d("onCreate()")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Timber.e("onPause()")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Timber.d("onResume()")

//        getAirport()
//        getOperator()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onStop()")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onDestroy()")

    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    fun getAirports() {
        Timber.d("getAirports()")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val firstAirports = repository.getAirports(maxPages = 3)
            Timber.d("First Airports Next link: ${firstAirports.links.getNext()}")

            val secondAirports =
                repository.getAirports(maxPages = 3, cursor = firstAirports.links.getNext())
            val thirdAirports =
                repository.getAirports(maxPages = 3, cursor = secondAirports.links.getNext())

            val totalAirports =
                firstAirports.airports + secondAirports.airports + thirdAirports.airports

            Timber.d("Total Airports: ${totalAirports.size}")
            updateAirportList(totalAirports.map { it.toModel() })
        }
    }

    fun getAirport(airportID: String = "01LA") {
        Timber.d("getAirport() | id: $airportID")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val airport = repository.getAirportById(airportID).toModel()
            Timber.d("airport: $airport")
            updateAirport(airport)
        }
    }

    fun getOperators() {
        Timber.d("getOperators()")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val firstOperators = repository.getOperators(maxPages = 3)
            Timber.d("First Operators Next link: ${firstOperators.links.getNext()}")

            val secondOperators =
                repository.getOperators(maxPages = 3, cursor = firstOperators.links.getNext())
            val thirdOperators =
                repository.getOperators(maxPages = 3, cursor = secondOperators.links.getNext())

            val totalOperators =
                firstOperators.operators + secondOperators.operators + thirdOperators.operators

            Timber.d("Total Operators: ${totalOperators.size}")
            updateOperators(totalOperators.map { it.toModel() })
        }
    }

    fun getOperator(operatorID: String = "AFR") {
        Timber.d("getOperator() | id: $operatorID")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val operator = repository.getOperatorById(operatorID).toModel()
            Timber.d("operator: $operator")
            updateOperator(operator)
        }
    }
}