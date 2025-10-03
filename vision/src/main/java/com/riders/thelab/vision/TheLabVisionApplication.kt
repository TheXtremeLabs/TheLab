package com.riders.thelab.vision

import androidx.multidex.MultiDexApplication
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.performance
import com.riders.thelab.core.common.utils.LabDeviceManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TheLabVisionApplication : MultiDexApplication() {


    ////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////
    override fun onCreate() {
        super.onCreate()

        initTimber()
        // initFirebase()

        if (BuildConfig.DEBUG) {
            LabDeviceManager.logDeviceInfo()
            Timber.i("${this@TheLabVisionApplication::class.java.simpleName} successfully initialized")
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
        Timber.e("onTerminate() | ${this::class.java.simpleName} was killed")
    }

    ////////////////////////////////////////
    //
    // CLAS METHODS
    //
    ////////////////////////////////////////
    private fun initTimber() {
        Timber.d("initTimber()")

        if (BuildConfig.DEBUG) {
            // Timber : logging
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initFirebase() {
        Timber.d("initFirebase()")

        FirebaseApp.initializeApp(this)
        // Firebase Crashlytics
        FirebaseCrashlytics.getInstance()
        Firebase.crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("wayne")
        }

        FirebasePerformance.getInstance()
        Firebase.performance.apply {
            isPerformanceCollectionEnabled = true
            newTrace("TheLabVision Trace")
        }
    }
}