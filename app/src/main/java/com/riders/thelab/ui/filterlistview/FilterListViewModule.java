package com.riders.thelab.ui.filterlistview;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class FilterListViewModule {

    @ActivityScope
    @Provides
    static FilterListViewContract.View provideView(FilterListViewActivity activity) {
        return new FilterListViewView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(FilterListViewActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(FilterListViewPresenter presenter);
}
