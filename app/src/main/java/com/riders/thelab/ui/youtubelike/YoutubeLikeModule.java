package com.riders.thelab.ui.youtubelike;


import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;
import com.riders.thelab.ui.filterlistview.FilterListViewPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class YoutubeLikeModule {


    @ActivityScope
    @Provides
    static YoutubeLikeContract.View provideView(YoutubeLikeActivity activity) {
        return new YoutubeLikeView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(YoutubeLikeActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(FilterListViewPresenter presenter);
}
