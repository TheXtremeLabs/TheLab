package com.riders.thelab.ui.multipane;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Movie;

import org.parceler.Parcels;

import timber.log.Timber;

public class MultipaneActivity extends BaseActivity<MultipaneView>
        implements MultiPaneMainFragment.OnItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (LabCompatibilityManager.isTablet(this))
            //Force screen orientation to Landscape mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            //Force screen orientation to Portrait mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_multi_pane);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        view.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        view.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onMovieItemSelected(Movie movie) {
        MultiPaneDetailFragment fragment = MultiPaneDetailFragment.getInstance();

        if (null != fragment) {
            Bundle args = new Bundle();
            args.putParcelable(MultiPaneDetailFragment.BUNDLE_MOVIE, Parcels.wrap(movie));
            fragment.setArguments(args);

            fragment.displayContent();
        } else {
            Timber.e("Something went wrong while loading data into fragment");
        }
    }
}
