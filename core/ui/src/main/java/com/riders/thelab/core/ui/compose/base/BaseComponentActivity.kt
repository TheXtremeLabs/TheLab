package com.riders.thelab.core.ui.compose.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.window.layout.WindowMetricsCalculator
import com.riders.thelab.core.common.bus.Listen
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.compose.WindowSizeClass
import timber.log.Timber
import java.lang.reflect.InvocationTargetException

abstract class BaseComponentActivity : ComponentActivity() {

    //    open var permissionLauncher: ActivityResultLauncher<String>? = null
    open var permissionLauncher: ActivityResultLauncher<Array<String>>? = null

    private var deviceWindowsSizeClass: WindowSizeClass? = null


    val isTv: Boolean
        get() = packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
            .also { isTelevision: Boolean ->
                if (isTelevision) {
                    Timber.tag("DeviceTypeRuntimeCheck").i("Running on a TV Device")
                } else {
                    Timber.tag("DeviceTypeRuntimeCheck").i("Running on a non-TV Device")
                }
            }

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
    /*
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
        }*/

    ///////////////////////////////
    //
    // BUS METHODS
    //
    ///////////////////////////////
    fun subscribeToKotlinBus() {
        Timber.i("subscribeToKotlinBus()")
        javaClass.declaredMethods
            .filter { it.isAnnotationPresent(Listen::class.java) }
            .forEach {
                try {
                    it.invoke(this)
                } catch (e: IllegalAccessException) {
                    Timber.e(e)
                } catch (e: InvocationTargetException) {
                    Timber.e(e)
                }
            }
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

    fun launchPermissionRequest(permission: String) =
        launchPermissionRequest(arrayOf(permission))


    private fun launchPermissionRequest(permissions: Array<String>) {
        Timber.e("requestPermission() | permissions: ${permissions.contentToString()}")
        permissionLauncher?.launch(permissions) ?: {
            Timber.e("Permission launcher has NOT been initialized")
        }
    }

    fun registerReceivers(vararg receivers: Pair<BroadcastReceiver, IntentFilter>) {
        Timber.d("registerReceivers() | receivers: ${receivers.contentToString()}")

        runCatching {
            receivers.forEach {
                registerReceiver(it.first, it.second)
            }
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("registerReceivers() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.i("registerReceivers() | onSuccess | ${receivers.joinToString(",") { it::class.java.simpleName }} registered")
            }
    }

    fun unregisterReceivers(vararg receivers: BroadcastReceiver) {
        Timber.e("unregisterReceivers() | receivers: ${receivers.contentToString()}")
        runCatching {
            receivers.forEach {
                unregisterReceiver(it)
            }
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("unregisterReceivers() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.i("unregisterReceivers() | onSuccess | ${receivers.joinToString(",") { it::class.java.simpleName }} unregistered")
            }
    }


    fun computeWindowSizeClasses() {
        Timber.d("computeWindowSizeClasses()")

        val metrics = WindowMetricsCalculator
            .getOrCreate()
            .computeCurrentWindowMetrics(this)

        val widthDp = metrics.bounds.width() /
                resources.displayMetrics.density
        val widthWindowSizeClass = when {
            widthDp < 600f -> WindowSizeClass.COMPACT
            widthDp < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        Timber.i("widthWindowSizeClass: $widthWindowSizeClass")

        val heightDp = metrics.bounds.height() /
                resources.displayMetrics.density
        val heightWindowSizeClass = when {
            heightDp < 480f -> WindowSizeClass.COMPACT
            heightDp < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }
        Timber.i("heightWindowSizeClass: $heightWindowSizeClass")

        // Use widthWindowSizeClass and heightWindowSizeClass.
        deviceWindowsSizeClass = widthWindowSizeClass
    }

    fun getDeviceWindowsSizeClass(): WindowSizeClass {
        Timber.d("getDeviceWindowsSizeClass()")
        return deviceWindowsSizeClass!!
    }

    abstract fun backPressed()
}