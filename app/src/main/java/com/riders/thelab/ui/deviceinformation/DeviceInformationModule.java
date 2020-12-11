package com.riders.thelab.ui.deviceinformation;

import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class DeviceInformationModule {


    @ActivityScope
    @Provides
    static DeviceInformationContract.View provideView(DeviceInformationActivity activity) {
        return new DeviceInformationView(activity);
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(DeviceInformationActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(DeviceInformationPresenter presenter);

}