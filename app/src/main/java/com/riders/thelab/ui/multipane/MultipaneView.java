package com.riders.thelab.ui.multipane;

import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.model.Movie;
import com.riders.thelab.ui.base.BaseViewImpl;
import com.riders.thelab.ui.mainactivity.fragment.time.TimeFragment;
import com.riders.thelab.ui.mainactivity.fragment.weather.WeatherFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class MultipaneView extends BaseViewImpl<MultipanePresenter>
        implements MultipaneContract.View, MovieClickListener {

    // Views
    private RecyclerView recyclerView;

    private FragmentContainerView fcMainMovieList;
    private FragmentContainerView fcDetailMovie;

    private MultipaneActivity context;
    private MoviesAdapter mAdapter;

    @Inject
    MultipaneView(MultipaneActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        if (!LabCompatibilityManager.isTablet(context)) {
            bindSmartphoneViews();
        } else {
            bindTabletViews();
        }

        context.getSupportActionBar().setDisplayShowHomeEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_multipane));

    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();
        context = null;
    }

    @Override
    public void showLoading() {
        Timber.d("showLoading()");

    }

    @Override
    public void hideLoading() {
        Timber.d("hideLoading()");

    }

    @Override
    public void onPause() {
        Timber.d("onPause()");

    }

    @Override
    public void onResume() {
        Timber.d("onResume()");
        if (!LabCompatibilityManager.isTablet(context)) {
            getPresenter().fetchMovies();
        }
    }

    @Override
    public void onMovieFetchedSuccess(List<Movie> movies) {
        Timber.d("onMovieFetchedSuccess()");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        mAdapter = new MoviesAdapter(context, movies, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onMovieFetchedError(Throwable error) {
        Timber.d("onMovieFetchedError()");
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Timber.d("movie clicked: %s", movie.toString());
        getPresenter().getMovieDetail(movie);
    }


    /////////////////////////////////////
    //
    // BUTTERKNIFE
    //
    /////////////////////////////////////
    private void bindSmartphoneViews() {
        Timber.d("bindSmartphoneViews()");
        // Butterknife view binding
        ButterKnife.bind(context, recyclerView);
    }

    private void bindTabletViews() {
        Timber.d("bindTabletViews()");
        ButterKnife.bind(context, fcMainMovieList);
        ButterKnife.bind(context, fcDetailMovie);

        context.getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.fc_main_multi_pane,
                        MultiPaneMainFragment.newInstance())
                .commit();

        context.getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.fc_detail_movie_pane,
                        MultiPaneDetailFragment.newInstance())
                .commit();
    }
}
