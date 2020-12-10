package com.riders.thelab.ui.locationonmaps;

import com.google.android.gms.maps.SupportMapFragment;
import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.di.scopes.FragmentScope;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LocationOnMapsModule {

    @ActivityScope
    @Provides
    static LocationOnMapsContract.View provideView(LocationOnMapsActivity activity) {
        return new LocationOnMapsView(activity);
    }


    @FragmentScope
    @Provides
    static SupportMapFragment provideSupportMapFragment(LocationOnMapsActivity activity) {
        return new SupportMapFragment();
    }

    @ActivityScope
    @Provides
    static Navigator provideNavigator(LocationOnMapsActivity activity) {
        return new Navigator(activity);
    }

    @ActivityScope
    @Binds
    abstract BasePresenter providePresenter(LocationOnMapsPresenter presenter);
}
