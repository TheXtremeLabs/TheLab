package com.riders.thelab.call.data

import android.telecom.Call
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

object LabCallRepository {

    private val _callState = MutableStateFlow<Int?>(null)
    val callState: StateFlow<Int?> = _callState

    private var call: Call? = null
    private var callStartTime: Long = 0L

    fun setCall(newCall: Call) {
        call = newCall

        newCall.registerCallback(object : Call.Callback() {
            override fun onStateChanged(call: Call, state: Int) {
                Timber.i("onStateChanged() | State: $state")

                _callState.value = state
                if (state == Call.STATE_ACTIVE) {
                    callStartTime = System.currentTimeMillis()
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
}