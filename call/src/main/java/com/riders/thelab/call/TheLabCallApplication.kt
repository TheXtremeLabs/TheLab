package com.riders.thelab.call

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TheLabCallApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()

        Timber.i("onCreate() | ${TheLabCallApplication::class.java.simpleName} successfully initialized")
    }

    fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}