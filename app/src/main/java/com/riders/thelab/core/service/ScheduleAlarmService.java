package com.riders.thelab.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import timber.log.Timber;

public class ScheduleAlarmService extends Service {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private IBinder mBinder = new Binder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.d("Service bound");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("Service created");
    }

    @Override
    public void onDestroy() {
        if (null != mBinder)
            mBinder = null;

        super.onDestroy();
    }
}
