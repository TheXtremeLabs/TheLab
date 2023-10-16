package com.riders.thelab.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

class LocationBroadcastReceiver : BroadcastReceiver() {

    private val mLocationStatus: MutableLiveData<Boolean> = MutableLiveData()

    fun getLocationStatus(): LiveData<Boolean> {
        return mLocationStatus
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (LocationManager.PROVIDERS_CHANGED_ACTION == intent!!.action) {

            if (null == context) {
                Timber.e("Context is null")
                return
            }

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            mLocationStatus.value = isGpsEnabled || isNetworkEnabled
        }
    }
}