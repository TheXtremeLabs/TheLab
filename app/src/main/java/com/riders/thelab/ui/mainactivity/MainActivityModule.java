package com.riders.thelab.ui.mainactivity;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.di.scopes.FragmentScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


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
}
