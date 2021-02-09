package com.riders.thelab.ui.weather;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class WeatherModule {

    @ActivityScope
    @Provides
    static WeatherContract.View provideView(WeatherActivity activity) {
        return new WeatherView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(WeatherActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(WeatherPresenter presenter);


    /*@ActivityScope
    @Binds
    abstract LabDelegatingWorkerFactory provideWeatherDownloadWorker(LabDelegatingWorkerFactory worker);*/
}
