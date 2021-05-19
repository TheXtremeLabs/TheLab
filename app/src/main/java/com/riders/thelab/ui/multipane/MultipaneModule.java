package com.riders.thelab.ui.multipane;

import com.riders.thelab.di.scopes.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MultipaneModule {

    @ActivityScope
    @Provides
    static MultipaneContract.View provideView(MultipaneActivity activity) {
        return new MultipaneView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(MultipaneActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(MultipanePresenter presenter);
}
