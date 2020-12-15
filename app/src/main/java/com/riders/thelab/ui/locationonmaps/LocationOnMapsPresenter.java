package com.riders.thelab.ui.locationonmaps;

import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class LocationOnMapsPresenter extends BasePresenterImpl<LocationOnMapsView>
        implements LocationOnMapsContract.Presenter {

    @Inject
    LocationOnMapsActivity activity;

    @Inject
    LocationOnMapsPresenter() {

    }
}
