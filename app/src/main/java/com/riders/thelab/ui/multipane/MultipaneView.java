package com.riders.thelab.ui.multipane;

import com.riders.thelab.data.local.model.Movie;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.List;

import javax.inject.Inject;

public class MultipaneView extends BaseViewImpl<MultipanePresenter>
        implements MultipaneContract.View {

    private MultipaneActivity context;

    @Inject
    MultipaneView(MultipaneActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onMovieFetchedSuccess(List<Movie> movies) {

    }

    @Override
    public void onMovieFetchedError(Throwable error) {

    }
}
