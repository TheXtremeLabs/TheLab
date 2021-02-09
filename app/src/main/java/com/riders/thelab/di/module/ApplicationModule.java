package com.riders.thelab.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    /*@Singleton
    @Provides
    Configuration provideWorkManagerConfiguration(LabDelegatingWorkerFactory labDelegatingWorkerFactory) {
        return new Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .setWorkerFactory(labDelegatingWorkerFactory)
                .build();
    }*/
}


