package com.riders.thelab.feature.flightaware.base

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.location.LabLocationManager
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

abstract class BaseFlightViewModel(
    labNetworkManager: LabNetworkManager,
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository) {

    lateinit var mLabLocationManager: LabLocationManager
        private set

    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////
    // Network State
    val hasInternetConnection: StateFlow<Boolean> = labNetworkManager.isConnectedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    fun initLocationManager(activity: Activity) {
        runCatching {
            LabLocationManager.getInstance(activity = activity)
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("initLocationManager() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.d("initLocationManager() | onSuccess | is success: $it")
                mLabLocationManager = it
            }
    }
}