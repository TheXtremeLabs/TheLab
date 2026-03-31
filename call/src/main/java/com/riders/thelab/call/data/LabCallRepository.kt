package com.riders.thelab.call.data

import android.telecom.Call
import android.telecom.VideoProfile
import com.riders.thelab.call.data.local.compose.CallState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

object LabCallRepository {

    private val _callState = MutableStateFlow<Int?>(null)
    val callState: StateFlow<Int?> = _callState

    private var call: Call? = null
    private var callStartTime: Long = 0L

    private val _uiState: MutableStateFlow<CallState> = MutableStateFlow<CallState>(CallState.Idle)
    val uiState: StateFlow<CallState> = _uiState

    fun setCall(newCall: Call) {
        call = newCall

        newCall.registerCallback(object : Call.Callback() {
            override fun onStateChanged(call: Call, state: Int) {
                Timber.i("onStateChanged() | State: $state")

                _callState.value = state
                if (state == Call.STATE_ACTIVE) {
                    callStartTime = System.currentTimeMillis()
                }

                when (state) {
                    Call.STATE_RINGING ->
                        _uiState.value = CallState.Incoming(call.details.id)

                    Call.STATE_DIALING ->
                        _uiState.value = CallState.Outgoing(call.details.id)

                    Call.STATE_ACTIVE -> {
                        callStartTime = System.currentTimeMillis()
                        _uiState.value = CallState.Active(
                            call.details.id,
                            getElapsedSeconds()
                        )
                    }

                    Call.STATE_HOLDING -> _uiState.value = CallState.OnHold

                    Call.STATE_DISCONNECTED -> _uiState.value = CallState.Disconnected
                }
            }
        })
    }

    fun endCall() {
        call?.disconnect()
    }

    fun resetCall() {
        Timber.e("resetCall()")
        endCall()
        call = null
        _callState.value = null
    }

    fun getElapsedSeconds(): Long =
        if (callStartTime == 0L) 0
        else (System.currentTimeMillis() - callStartTime) / 1000

    // ----- Call Actions -----

    fun answer() {
        call?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun hangUp() {
        call?.disconnect()
    }

    fun toggleMute() {
        call?.let {
            it.details.can(Call.Details.CAPABILITY_MUTE)
            // it.details.setMuted(!it.details.isMuted)
        }
    }

    fun showKeypad() {
        _uiState.value = CallState.Keypad
    }

    fun hideKeypad() {
        _uiState.value = CallState.Active(
            call?.details?.id?:"",
            getElapsedSeconds()
        )
    }
}