package com.riders.thelab

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabDeviceManager
import com.riders.thelab.feature.weather.core.worker.WeatherWorker
import com.riders.thelab.feature.weather.ui.WeatherDownloadWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class TheLabApplication : MultiDexApplication(), LifecycleEventObserver, Configuration.Provider {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    ////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////
    override fun onCreate() {
        super.onCreate()

        mInstance = this

        initTimberAndThreeten()
        initAdsAndFirebase()
        // delayedInit()

//        val appLifecycleObserver = TheLabAppLifecycleObserver()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        if (BuildConfig.DEBUG) {
            LabDeviceManager.logDeviceInfo()
            Timber.i("${TheLabApplication::class.java.simpleName} successfully initialized")
        }
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

    override fun onTerminate() {
        super.onTerminate()
        Timber.e("onTerminate() | ${this@TheLabApplication::class.java.simpleName} was killed")
    }

    // New Worker configuration since version 2.9.0
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()


    ////////////////////////////////////////
    //
    // CLAS METHODS
    //
    ////////////////////////////////////////
    private fun initTimberAndThreeten() {
        Timber.d("initTimberAndThreeten()")

        if (BuildConfig.DEBUG) {
            // Timber : logging
            Timber.plant(Timber.DebugTree())
        }

        // ThreeTen Date Time Library
        AndroidThreeTen.init(this)
    }

    private fun initAdsAndFirebase() {
        Timber.d("initAdsAndFirebase()")

        // Firebase Crashlytics
        FirebaseCrashlytics.getInstance().apply {
            setCrashlyticsCollectionEnabled(true)
            setUserId("wayne")
        }

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

    @SuppressLint("NewApi")
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


        // Setup periodic worker to update widget
        val periodicWeatherWorkRequest: PeriodicWorkRequest =
            if (LabCompatibilityManager.isNougat()) PeriodicWorkRequestBuilder<WeatherWorker>(
                Duration.ofMinutes(15L)
            ).build()
            else PeriodicWorkRequestBuilder<WeatherWorker>(15, TimeUnit.MINUTES).build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                WeatherWorker::class.java.simpleName,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWeatherWorkRequest
            )
    }

    fun getContext(): Context {
        return super.getApplicationContext()
    }

    fun getLabPackageName(): String = packageName

    private fun notifyAppInBackground() {
        Timber.e("App went in background")

        val workRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<WeatherWorker>().build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniqueWork(
                "WeatherWorker_when_app_in_background_${workRequest.id}",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    companion object {
        private var mInstance: TheLabApplication? = null

        @Synchronized
        fun getInstance(): TheLabApplication {

            if (null == mInstance) {
                mInstance = TheLabApplication()
            }

            return mInstance as TheLabApplication
        }

        @JvmStatic
        fun getActivityPackageName(activityName: String): String? {
            val pManager: PackageManager = getInstance().packageManager
            val packageName: String = getInstance().applicationContext.packageName

            var returnedActivityPackageToString = ""

            return try {
                val list =
                    pManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities
                for (activityInfo in list) {
                    val activityNameFound = activityInfo.name
                    Timber.d("ActivityInfo = " + activityInfo.name)
                    if (activityNameFound.lowercase().contains(activityName.lowercase())) {
                        returnedActivityPackageToString = activityInfo.name
                        break
                    }
                }
                returnedActivityPackageToString
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                null
            }
        }
    }


    ////////////////////////////////////////
    //
    // LIFECYCLE
    //
    ////////////////////////////////////////
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {}

            Lifecycle.Event.ON_PAUSE -> {
                Timber.e("App in background")
            }

            Lifecycle.Event.ON_RESUME -> {
                Timber.d("App in foreground")
            }

            else -> {
                // Do nothing
            }
        }
    }
}