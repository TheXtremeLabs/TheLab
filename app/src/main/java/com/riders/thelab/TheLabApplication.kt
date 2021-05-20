package com.riders.thelab

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TheLabApplication : MultiDexApplication(), androidx.work.Configuration.Provider {

    companion object {
        private var LAB_PACKAGE_NAME: String? = null
        private var mInstance: TheLabApplication? = null

        @Synchronized
        fun getInstance(): TheLabApplication {

            if (null == mInstance) {
                mInstance = TheLabApplication()
            }

            return mInstance as TheLabApplication
        }

    }


    // Firebase
    private var mFirebaseCrashlytics: FirebaseCrashlytics? = null


    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()

        mInstance = this
        init()
    }

    private fun init() {
        LAB_PACKAGE_NAME = packageName

        // Timber : logging
        Timber.plant(Timber.DebugTree())

        // ThreeTen Date Time Library
        AndroidThreeTen.init(this)

        // Firebase Crashlytics

        // Firebase Crashlytics
        mFirebaseCrashlytics = FirebaseCrashlytics.getInstance()
        mFirebaseCrashlytics?.setCrashlyticsCollectionEnabled(true)
        mFirebaseCrashlytics?.setUserId("wayne")

        // Mobile ADS

        // Mobile ADS
        MobileAds.initialize(this) { initializationStatus: InitializationStatus ->
            Timber.d(
                "initializationStatus : %s",
                initializationStatus.toString()
            )

        }
    }

    fun getContext(): Context {
        return super.getApplicationContext()
    }

    fun getLabPackageName(): String? {
        return LAB_PACKAGE_NAME
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    @SuppressLint("BinaryOperationInTimber")
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            notifyAppInBackground()
        }

        if (level == TRIM_MEMORY_RUNNING_LOW) {
            Timber.e(
                "The device is running much lower on memory. " +
                        "Your app is running and not killable," +
                        " but please release unused resources to improve system performance"
            )
        }
    }

    private fun notifyAppInBackground() {
        Timber.e("App went in background")
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun getWorkManagerConfiguration() =
        androidx.work.Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}