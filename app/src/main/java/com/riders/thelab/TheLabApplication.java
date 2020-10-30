package com.riders.thelab;

import android.app.Application;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class TheLabApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCrashlyticsCollectionEnabled(true);
        crashlytics.setUserId("myAppUserId");
    }
}
