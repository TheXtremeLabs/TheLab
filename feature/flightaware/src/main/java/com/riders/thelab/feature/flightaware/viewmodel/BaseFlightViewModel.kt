package com.riders.thelab.feature.flightaware.viewmodel

import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class BaseFlightViewModel @Inject constructor(
    labNetworkManager: LabNetworkManager,
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository) {

    //////////////////////////////////////////
    // Composable states
    //////////////////////////////////////////
    // Network State
    val hasInternetConnection: StateFlow<Boolean> = labNetworkManager.isConnectedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    /////////////////////////////////////
    // Coroutine
    /////////////////////////////////////
    private val mNetworkCoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e("mNetworkCoroutineExceptionHandler | Error caught with message: ${throwable.message}")
        }
}