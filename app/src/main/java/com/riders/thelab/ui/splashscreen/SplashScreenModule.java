package com.riders.thelab.ui.splashscreen;


import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SplashScreenModule {

    @ActivityScope
    @Provides
    static SplashScreenContract.View provideView(SplashScreenActivity activity) {
        return new SplashScreenView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(SplashScreenActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(SplashScreenPresenter presenter);
}
