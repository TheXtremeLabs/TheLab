package com.riders.thelab.call.core.telecom

import android.telecom.CallAudioState
import android.telecom.Connection
import android.telecom.DisconnectCause
import timber.log.Timber

class CallConnection : Connection() {

    override fun onShowIncomingCallUi() {
        // Since this is an outgoing call, we don't need to show incoming UI.
    }

    override fun onCallAudioStateChanged(state: CallAudioState?) {
        // No-op
        Timber.d("onCallAudioStateChanged() state: $state")
    }

    override fun onHold() {
        Timber.w("onHold()")
    }

    override fun onUnhold() {
        Timber.w("onUnhold()")
    }

    override fun onAnswer() {
        Timber.d("onAnswer()")
        // Outgoing call is answered, set active
        setActive()
    }

    override fun onReject() {
        Timber.e("onReject()")
        // Outgoing call is rejected, set disconnected
        setDisconnected(DisconnectCause(DisconnectCause.REJECTED))
        destroy()
    }

    override fun onDisconnect() {
        Timber.e("onDisconnect()")
        // Call is disconnected
        setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
        destroy()
    }
}
