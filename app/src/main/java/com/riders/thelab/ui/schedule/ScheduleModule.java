package com.riders.thelab.ui.schedule;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;
import com.riders.thelab.ui.palette.PaletteActivity;
import com.riders.thelab.ui.palette.PaletteContract;
import com.riders.thelab.ui.palette.PalettePresenter;
import com.riders.thelab.ui.palette.PaletteView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ScheduleModule {

    @ActivityScope
    @Provides
    static ScheduleContract.View provideView(ScheduleActivity activity) {
        return new ScheduleView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(ScheduleActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(PalettePresenter presenter);

}