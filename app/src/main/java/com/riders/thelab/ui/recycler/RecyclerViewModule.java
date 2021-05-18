package com.riders.thelab.ui.recycler;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RecyclerViewModule {


    @ActivityScope
    @Provides
    static RecyclerViewContract.View provideView(RecyclerViewActivity activity) {
        return new RecyclerViewView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(RecyclerViewActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(RecyclerViewPresenter presenter);
}
