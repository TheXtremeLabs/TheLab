package com.riders.thelab;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.riders.thelab.core.broadcast.ConnectivityReceiver;
import com.riders.thelab.data.DataModule;
import com.riders.thelab.di.component.DaggerComponentInjector;
import com.riders.thelab.di.module.ApplicationModule;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

@SuppressLint("StaticFieldLeak")
public class TheLabApplication extends DaggerApplication {

    private static Context context;
    private static TheLabApplication mInstance;

    public static String LAB_PACKAGE_NAME = "";
    private FirebaseCrashlytics mFirebaseCrashlytics;


    public TheLabApplication() {
    }

    public static synchronized TheLabApplication getInstance() {
        if (null == mInstance)
            mInstance = new TheLabApplication();

        return mInstance;
    }


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

        context = this;

        /*if (!LabCompatibilityManager.isOreo())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);*/

        init();

        LAB_PACKAGE_NAME = getPackageName();
    }

    private void init() {
        // Timber : logging
        Timber.plant(new Timber.DebugTree());

        // Set Font for the entire application
        /*LabTypefaceManager.overrideFont(
                getApplicationContext(),
                "serif",
                "fonts/SamsungSans-LightItalic.ttf");*/

        // ThreeTen Date Time Library
        AndroidThreeTen.init(this);

        // Firebase Crashlytics
        mFirebaseCrashlytics = FirebaseCrashlytics.getInstance();
        mFirebaseCrashlytics.setCrashlyticsCollectionEnabled(true);
        mFirebaseCrashlytics.setUserId("wayne");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            notifyAppInBackground();
        }

        if (level == TRIM_MEMORY_RUNNING_LOW) {
            Timber.e(
                    "The device is running much lower on memory. " +
                            "Your app is running and not killable," +
                            " but please release unused resources to improve system performance");
        }
    }

    private void notifyAppInBackground() {
        Timber.e("App went in background");
    }

    public static Context getContext() {
        return context;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        Timber.d("setConnectivityListener(listener)");
        if (null == ConnectivityReceiver.getInstance().getConnectivityReceiverListener())
            new ConnectivityReceiver(listener);
    }
}
