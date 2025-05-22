package com.riders.thelab.core.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import com.riders.thelab.core.common.bus.KotlinBus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class LabLocationReceiver : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("onReceive() | PROVIDERS_CHANGED has been detected")

        if (null == context) {
            Timber.e("onReceive() | context is null")
            return
        }

        intent?.action?.let {
            when (it) {
                LocationManager.PROVIDERS_CHANGED_ACTION -> {
                    Timber.i("onReceive() | PROVIDERS_CHANGED has been detected")

                    //  Retrieve the LocationManager
                    val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    //  Provide values to retrieve the Location availability
                    //  Provide boolean references for Location availability types
                    val isGpsEnabled: Boolean =
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    val isNetworkEnabled: Boolean =
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                    Timber.d("onReceive() | PROVIDERS_CHANGED has been detected - GPS: $isGpsEnabled NET: $isNetworkEnabled")

                    //  If (for example), the GPS is ENABLED, start one of your Activities, etc.
                    //if (isGpsEnabled) {
                    //  Timber.d("onReceive() | GPS is enabled. If (for example), the GPS is ENABLED, start one of your Activities, etc.")
                    // val startYourActivity: Intent = Intent(context.getApplicationContext(), YourActivity::class.java)
                    // context.startActivity(startYourActivity)

                    GlobalScope.launch {
                        KotlinBus.publish(
                            GPSProvidersResultModel(
                                isGPS = isGpsEnabled,
                                isNet = isNetworkEnabled
                            )
                        )
                    }
                    // }
                }

                else -> {
                    Timber.i("onReceive() | Unknown action: $it")
                }
            }
        }
    }

    companion object {
        fun getIntentFilters(): IntentFilter =
            IntentFilter().apply { addAction(LocationManager.PROVIDERS_CHANGED_ACTION) }
    }
}