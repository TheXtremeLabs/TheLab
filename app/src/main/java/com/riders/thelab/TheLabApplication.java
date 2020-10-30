package com.riders.thelab;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.riders.thelab.data.DataModule;
import com.riders.thelab.di.module.ApplicationModule;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

public class TheLabApplication extends DaggerApplication {

    public static String HUB_PACKAGE_NAME = "";
    FirebaseCrashlytics mCrashlytics;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerComponentInjector.builder()
                .application(this)
                .applicationModule(new ApplicationModule(this))
                .dataModule(new DataModule(this))
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();

        HUB_PACKAGE_NAME = getPackageName();
    }

    private void init() {
        Timber.plant(new Timber.DebugTree());

        // Firebase Crashlytics
        mCrashlytics = FirebaseCrashlytics.getInstance();
        mCrashlytics.setCrashlyticsCollectionEnabled(true);
        mCrashlytics.setUserId("wayne");

    }
}
