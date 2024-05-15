package com.riders.thelab.feature.googledrive.core.google

import android.app.Activity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.riders.thelab.core.ui.utils.UIManager
import timber.log.Timber

object GooglePlayServicesManager {

    fun checkPlayServices(
        activity: Activity,
        googleApiAvailability: GoogleApiAvailability
    ): Boolean {
        Timber.d("checkPlayServices()")

        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(
                    activity,
                    resultCode,
                    PLAY_SERVICES_RESOLUTION_REQUEST,
                )?.show()
            } else {
                UIManager.showToast(activity, "This device is not supported")
                activity.finish()
            }
            return false
        }

        return true
    }

    private const val PLAY_SERVICES_RESOLUTION_REQUEST = ConnectionResult.RESOLUTION_REQUIRED
}