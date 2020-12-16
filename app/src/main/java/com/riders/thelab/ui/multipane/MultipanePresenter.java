package com.riders.thelab.ui.multipane;

import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class MultipanePresenter extends BasePresenterImpl<MultipaneView>
        implements MultipaneContract.Presenter {

    @Inject
    MultipaneActivity activity;

    @Inject
    public MultipanePresenter() {
    }

    @Override
    public void fetchMovies() {

    }
}
