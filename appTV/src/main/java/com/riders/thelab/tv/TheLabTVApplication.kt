package com.riders.thelab.tv

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.riders.thelab.core.common.utils.LabDeviceManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TheLabTVApplication : MultiDexApplication() {

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

        if (BuildConfig.DEBUG) {
            LabDeviceManager.logDeviceInfo()
            Timber.i("${TheLabTVApplication::class.java.simpleName} successfully initialized")
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        when (level) {
            TRIM_MEMORY_UI_HIDDEN -> {
                Timber.w("onTrimMemory() | App is hidden")
//                notifyAppInBackground()
            }

            @Suppress("DEPRECATION")
            TRIM_MEMORY_RUNNING_LOW -> Timber.e(
                "The device is running much lower on memory. Your app is running and not killable, but please release unused resources to improve system performance"
            )

            TRIM_MEMORY_BACKGROUND -> Timber.w("onTrimMemory() | App went background")

            else -> {
                Timber.e(
                    "onTrimMemory() | else branch level type : $level"
                )
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.e("onTerminate() | ${this@TheLabTVApplication::class.java.simpleName} was killed")
    }


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
    }

    private fun initAdsAndFirebase() {
        /*Timber.d("initAdsAndFirebase()")

        // Firebase Crashlytics
        FirebaseApp.initializeApp(this@TheLabApplication)
        Firebase.crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("wayne")
        }

        //  Gets the instance of the Firebase App Check SDK.
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        // Tells App Check to use the Play Integrity provider to obtain tokens.
        firebaseAppCheck.installAppCheckProviderFactory(
            // Gets the Play Integrity App Check provider factory.
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )*/
    }

    fun getContext(): Context = super.getApplicationContext()

    fun getLabPackageName(): String = packageName

    companion object {
        private var mInstance: TheLabTVApplication? = null

        @Synchronized
        fun getInstance(): TheLabTVApplication = mInstance ?: synchronized(this) {
            mInstance ?: TheLabTVApplication().also { mInstance = it }
        }
    }
}