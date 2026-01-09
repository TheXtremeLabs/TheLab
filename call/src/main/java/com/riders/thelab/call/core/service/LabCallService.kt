package com.riders.thelab.call.core.service

import android.content.Intent
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import com.riders.thelab.call.core.telecom.CallConnection
import com.riders.thelab.call.ui.call.LabCallActivity
import timber.log.Timber

class LabCallService: ConnectionService() {

    companion object {
        val ongoingCalls = mutableListOf<CallConnection>()
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Timber.d("onCreateOutgoingConnection()")

        val connection = CallConnection()
        connection.setAddress(request?.address, TelecomManager.PRESENTATION_ALLOWED)
        connection.setDialing()

        // Here you would typically start your audio playback for the call.
        // For this example, we'll just log it.
        Timber.d("onCreateOutgoingConnection() | Dialing out to ${request?.address}")

        ongoingCalls.add(connection)

        val intent = Intent(this, LabCallActivity::class.java)
        intent.data = request?.address
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        return connection
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Timber.d("onCreateIncomingConnection() | Incoming call from ${request?.address}")

        // TODO: Implement incoming call logic
        val connection = CallConnection()
        connection.setAddress(request?.address, 0)
        connection.setRinging()
        ongoingCalls.add(connection)
        return connection
    }
}