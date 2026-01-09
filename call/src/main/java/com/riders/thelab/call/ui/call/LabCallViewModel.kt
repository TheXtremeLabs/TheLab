package com.riders.thelab.call.ui.call

import androidx.lifecycle.viewModelScope
import com.riders.thelab.call.core.service.LabCallService
import com.riders.thelab.call.data.LabCallRepository
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LabCallViewModel @Inject constructor(
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository) {

    val callState: StateFlow<Int?> = LabCallRepository.callState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null
        )


    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds

    init {
        viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                _elapsedSeconds.value = LabCallRepository.getElapsedSeconds()
            }
        }
    }

    fun onEvent(event: UiEvent) {
        Timber.d("onEvent() | Event: $event")

        when (event) {
            is UiEvent.OnHold -> {
                // Handle hold event
            }

            is UiEvent.OnMute -> {
                // Handle mute event
            }

            is UiEvent.OnSpeaker -> {
                // Handle speaker event
            }

            is UiEvent.OnKeypad -> {
                // Handle keypad event
            }

            is UiEvent.OnHangUp -> {
                // Handle hangup event
                hangUp(event.phoneNumber)
            }
        }
    }


    fun hangUp(phoneNumber: String) {
        Timber.e("hangUp()")

        val call = LabCallService.ongoingCalls.find {
            it.address.schemeSpecificPart == phoneNumber
        }
        call?.onDisconnect()
        LabCallRepository.endCall()
    }

    fun elapsedTime(): Long = LabCallRepository.getElapsedSeconds()
}