package com.riders.thelab.ui.locationonmaps;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.data.remote.dto.directions.Legs;
import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class LocationOnMapsPresenter extends BasePresenterImpl<LocationOnMapsView>
        implements LocationOnMapsContract.Presenter {

    @Inject
    LocationOnMapsActivity activity;

    @Inject
    LabService service;

    CompositeDisposable compositeDisposable;

    @Inject
    LocationOnMapsPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getDirections() {
        Timber.d("getDirections");

        String origin = "New York";
        String destination = "Washington";
        String key = activity.getString(R.string.google_api_key);

        Disposable disposable =
                service
                        .getRoutes(origin, destination, key)
                        .subscribe(
                                directions -> {
                                    Timber.d("directions : %s ", directions.toString());

                                    if (directions.getStatus().equals("OK")) {
                                        Legs legs = directions.getRoute().get(0).getLegs().get(0);
//                                Route route = Route(getString(R.string.time_square), getString(R.string.chelsea_market), legs.startLocation.lat, legs.startLocation.lng, legs.endLocation.lat, legs.endLocation.lng, directions.routes[0].overviewPolyline.points)

//                                mMap.setMarkersAndRoute(route);
                                    } else {
                                        UIManager.showActionInToast(activity, directions.getStatus());
                                    }
                                },
                                Timber::e
                        );

        compositeDisposable.add(disposable);

    }

    @Override
    public void detachView() {
        if (null != compositeDisposable)
            compositeDisposable.clear();
        super.detachView();
    }
}
