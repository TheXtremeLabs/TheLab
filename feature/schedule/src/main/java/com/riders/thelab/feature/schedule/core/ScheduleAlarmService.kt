package com.riders.thelab.feature.schedule.core

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import timber.log.Timber

class ScheduleAlarmService : Service() {
    companion object {
        const val STATUS_RUNNING = 0
        const val STATUS_FINISHED = 1
        const val STATUS_ERROR = 2
    }

    private var mBinder: IBinder? = Binder()

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("Service bound")
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("Service created")
    }

    override fun onDestroy() {
        if (null != mBinder) mBinder = null
        super.onDestroy()
    }
}