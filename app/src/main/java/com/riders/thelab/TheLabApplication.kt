package com.riders.thelab

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDexApplication
import androidx.work.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.riders.thelab.ui.weather.WeatherDownloadWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TheLabApplication : MultiDexApplication(), Configuration.Provider {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    // Firebase
    private var mFirebaseCrashlytics: FirebaseCrashlytics? = null

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()


    override fun onCreate() {
        super.onCreate()

        mInstance = this
        init()
        delayedInit()
    }

    private fun init() {
        LAB_PACKAGE_NAME = packageName

        // Timber : logging
        Timber.plant(Timber.DebugTree())

        // ThreeTen Date Time Library
        AndroidThreeTen.init(this)

        // Firebase Crashlytics
        mFirebaseCrashlytics = FirebaseCrashlytics.getInstance()
        mFirebaseCrashlytics?.setCrashlyticsCollectionEnabled(true)
        mFirebaseCrashlytics?.setUserId("wayne")

        // Mobile ADS
        MobileAds.initialize(this) { initializationStatus: InitializationStatus ->
            Timber.d(
                "initializationStatus : %s",
                initializationStatus.toString()
            )
        }
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupWork()
        }
    }

    private fun setupWork() {
        Timber.d("setupWork()")
        val workRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<WeatherDownloadWorker>().build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniqueWork(
                WeatherDownloadWorker::class.java.simpleName,
                ExistingWorkPolicy.KEEP,
                workRequest
            )
    }

    fun getContext(): Context {
        return super.getApplicationContext()
    }

    fun getLabPackageName(): String? {
        return LAB_PACKAGE_NAME
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            notifyAppInBackground()
        }

        if (level == TRIM_MEMORY_RUNNING_LOW) {
            Timber.e(
                "The device is running much lower on memory. Your app is running and not killable, but please release unused resources to improve system performance"
            )
        }
    }

    private fun notifyAppInBackground() {
        Timber.e("App went in background")
    }

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
}