package com.riders.thelab.ui.palette;

import com.riders.thelab.di.scopes.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class PaletteModule {


    @ActivityScope
    @Provides
    static PaletteContract.View provideView(PaletteActivity activity) {
        return new PaletteView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(PaletteActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(PalettePresenter presenter);
}
