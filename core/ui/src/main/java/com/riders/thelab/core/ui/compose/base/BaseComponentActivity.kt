package com.riders.thelab.core.ui.compose.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import timber.log.Timber

abstract class BaseComponentActivity : ComponentActivity() {

    private var permissionLauncher: ActivityResultLauncher<String>? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (LabCompatibilityManager.isTiramisu()) {
            // init Post notifications
            initPostNotificationsForAndroid13()

            // Handle onBackPressed for Android 13+
            onBackInvokedDispatcher
                .registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                    Timber.e("Android 13+ onBackInvokedDispatcher | OnBackInvokedDispatcher.registerOnBackInvokedCallback()")
                    backPressed()
                }
        } else {
            onBackPressedDispatcher
                .addCallback(
                    this,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            Timber.e("Android 13- onBackPressedDispatcher | OnBackPressedCallback.handleOnBackPressed() | finish()")
                            // Back is pressed... Finishing the activity
                            backPressed()
                        }
                    })
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initPostNotificationsForAndroid13() {
        Timber.d("initPostNotificationsForAndroid13()")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launchPermissionRequest(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            Timber.d("POST_NOTIFICATIONS Permission granted")
        }
    }

    private fun launchPermissionRequest(permission: String) {
        Timber.e("requestPermission() | permission: $permission")
        permissionLauncher?.launch(permission) ?: {
            Timber.e("Permission launcher has NOT been initialized")
        }
    }

    abstract fun backPressed()
}