package com.riders.thelab.central

import android.app.Application
import android.util.Log
import com.riders.thelab.core.common.utils.LabDeviceManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TheLabCentralApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()

        if (BuildConfig.DEBUG) {
            LabDeviceManager.logDeviceInfo()
            Timber.i("${TheLabCentralApplication::class.java.simpleName} successfully initialized")
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        when (level) {
            TRIM_MEMORY_UI_HIDDEN -> {
                Timber.w("onTrimMemory() | App went background")
//                notifyAppInBackground()
            }

            @Suppress("DEPRECATION")
            TRIM_MEMORY_RUNNING_LOW -> Timber.e(
                "The device is running much lower on memory. Your app is running and not killable, but please release unused resources to improve system performance"
            )

            else -> {
                Timber.e(
                    "onTrimMemory() | else branch level type : $level"
                )
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.e("onTerminate() | ${this@TheLabCentralApplication::class.java.simpleName} was killed")
    }

    ////////////////////////////////////////
    //
    // CLAS METHODS
    //
    ////////////////////////////////////////
    private fun initTimber() {
        Log.d(TheLabCentralApplication::class.java.simpleName,"initTimber()")

        if (BuildConfig.DEBUG) {
            // Timber : logging
            Timber.plant(Timber.DebugTree())
        }
    }
}