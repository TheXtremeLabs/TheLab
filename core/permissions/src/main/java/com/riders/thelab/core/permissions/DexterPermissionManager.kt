package com.riders.thelab.core.permissions

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import timber.log.Timber

class DexterPermissionManager(private val activity: Activity) {
    fun checkPermission(
        permission: String,
        onPermissionGranted: (Boolean) -> Unit,
        onPermissionDenied: (Boolean) -> Unit,
        onShouldShowRationale: (Boolean) -> Unit
    ) {
        Dexter
            .withContext(activity)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(grantedResponse: PermissionGrantedResponse?) {
                    // if all the permissions are granted we are displaying
                    // a simple toast message.
                    Timber.e("Permissions Granted..")

                    onPermissionGranted(true)
                }

                override fun onPermissionDenied(permissionDenied: PermissionDeniedResponse?) {
                    // if the permissions are not accepted we are displaying
                    // a toast message as permissions denied on below line.
                    Timber.e("Permissions Denied..")

                    onPermissionDenied(true)

                }

                // on below line we are calling on permission
                // rational should be shown method.
                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    // in this method we are calling continue
                    // permission request until permissions are not granted.
                    token?.continuePermissionRequest()
                    onShouldShowRationale(true)
                }
            })
            .withErrorListener {

                // on below line method will be called when dexter
                // throws any error while requesting permissions.
                Timber.e("withErrorListener | ${it.name}")
            }
            .check()
    }

}