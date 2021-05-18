package com.riders.thelab.ui.mainactivity;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.di.scopes.FragmentScope;
import com.riders.thelab.ui.base.BasePresenter;
import com.riders.thelab.ui.mainactivity.fragment.news.NewsFragment;
import com.riders.thelab.ui.mainactivity.fragment.time.TimeFragment;
import com.riders.thelab.ui.mainactivity.fragment.weather.WeatherFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class MainActivityModule {

    @ActivityScope
    @Provides
    static MainActivityContract.View provideView(MainActivity activity) {
        return new MainActivityView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(MainActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(MainActivityPresenter presenter);

    @FragmentScope
    @ContributesAndroidInjector
    abstract TimeFragment provideTimeFragment();

    @FragmentScope
    @ContributesAndroidInjector
    abstract WeatherFragment provideWeatherFragment();


    @FragmentScope
    @ContributesAndroidInjector
    abstract NewsFragment provideNewsFragment();
}
