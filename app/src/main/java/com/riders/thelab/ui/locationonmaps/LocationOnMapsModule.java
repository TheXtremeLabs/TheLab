package com.riders.thelab.ui.locationonmaps;

import com.google.android.gms.maps.MapFragment;
import com.riders.thelab.di.scopes.ActivityScope;
import com.riders.thelab.di.scopes.FragmentScope;
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
    static MapFragment provideSupportMapFragment(LocationOnMapsActivity activity) {
        return new MapFragment();
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
