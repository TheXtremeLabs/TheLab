package com.riders.thelab.ui.multipane;

import com.riders.thelab.data.local.model.Movie;
import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface MultipaneContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onPause();

        void onResume();

        void onMovieFetchedSuccess(List<Movie> movies);

        void onMovieFetchedError(Throwable error);
    }

    interface Presenter {
        void fetchMovies();

        void getMovieDetail(Movie movie);
    }
}
