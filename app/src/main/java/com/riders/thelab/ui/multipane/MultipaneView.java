package com.riders.thelab.ui.multipane;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.model.Movie;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class MultipaneView extends BaseViewImpl<MultipanePresenter>
        implements MultipaneContract.View, MovieClickListener {

    @BindView(R.id.rv_multi_pane)
    RecyclerView recyclerView;
    private MultipaneActivity context;
    //    private List<Movie> movieList;
    private MoviesAdapter mAdapter;

    @Inject
    MultipaneView(MultipaneActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        if (LabCompatibilityManager.isTablet(context))
            //Force screen orientation to Landscape mode
            this.context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            //Force screen orientation to Portrait mode
            this.context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context.setContentView(R.layout.activity_multi_pane);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

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

        getPresenter().fetchMovies();
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
}
