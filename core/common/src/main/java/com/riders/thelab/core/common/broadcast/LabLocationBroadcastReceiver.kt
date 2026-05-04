package com.riders.thelab.core.common.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import timber.log.Timber

class LabLocationBroadcastReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (null == context || null == intent) {
            Timber.e("onReceive() | context or intent is null")
            return
        }

        when (intent.action) {
            LocationManager.PROVIDERS_CHANGED_ACTION -> {
                val providerName = getProviderName(intent)
                val providerEnabled = getProviderEnabled(intent)
                val isLocationEnabled = isLocationEnabled(intent)
                Timber.d("onReceive() | Location providers changed | providerName: $providerName | providerEnabled: $providerEnabled, isLocationEnabled: $isLocationEnabled")
            }

            else -> Timber.d("onReceive() | Unhandled action: ${intent.action}")
        }
    }

    @SuppressLint("InlinedApi")
    private fun isLocationEnabled(intent: Intent): Boolean = if (LabCompatibilityManager.isR()) {
        intent.getBooleanExtra(LocationManager.MODE_CHANGED_ACTION, false)
    } else {
        intent.getBooleanExtra(LocationManager.EXTRA_LOCATION_ENABLED, false)
    }

    @SuppressLint("InlinedApi")
    private fun getProviderName(intent: Intent): String? =
        if (LabCompatibilityManager.isAndroid10()) {
            intent.getStringExtra(LocationManager.EXTRA_PROVIDER_NAME)
        } else {
            intent.getStringExtra(LocationManager.EXTRA_PROVIDER_NAME)
        }

    @SuppressLint("InlinedApi")
    private fun getProviderEnabled(intent: Intent): Boolean = if (LabCompatibilityManager.isR()) {
        intent.getBooleanExtra(LocationManager.EXTRA_PROVIDER_ENABLED, false)
    } else {
        intent.getBooleanExtra(LocationManager.EXTRA_LOCATION_ENABLED, false)
    }

    companion object {
        val intentFilters = IntentFilter().apply {
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            addAction(LocationManager.MODE_CHANGED_ACTION)
        }
    }
}