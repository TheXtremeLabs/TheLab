package com.riders.thelab.ui.multipane;

import android.content.Intent;

import com.riders.thelab.data.local.bean.MovieEnum;
import com.riders.thelab.data.local.model.Movie;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

public class MultipanePresenter extends BasePresenterImpl<MultipaneView>
        implements MultipaneContract.Presenter {

    @Inject
    Navigator navigator;

    @Inject
    MultipaneActivity activity;

    @Inject
    public MultipanePresenter() {
    }

    @Override
    public void fetchMovies() {
        List<Movie> list = MovieEnum.getMovies();
        getView().onMovieFetchedSuccess(list);
    }

    @Override
    public void getMovieDetail(Movie movie) {
        if (null != navigator) {
            Intent intent = new Intent(activity, MultipaneDetailActivity.class);
            intent.putExtra(MultipaneDetailActivity.EXTRA_MOVIE, Parcels.wrap(movie));
            navigator.callMultipaneDetailActivity(intent);
        }
    }
}
