package com.riders.thelab.call.core.service

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import com.riders.thelab.call.data.LabCallRepository
import com.riders.thelab.call.ui.call.LabCallActivity
import timber.log.Timber

class LabInCallService : InCallService() {

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        Timber.d("onCallAdded() | ${call.details.handle.schemeSpecificPart}")

        LabCallRepository.setCall(call)

        startForegroundService(Intent(this, LabCallForegroundService::class.java))

        val intent = Intent(this, LabCallActivity::class.java)
        intent.setData(call.details.handle)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        Timber.e("onCallRemoved() | ${call.details.handle.schemeSpecificPart}")

        stopService(Intent(this, LabCallForegroundService::class.java))

        LabCallRepository.resetCall()
    }
}