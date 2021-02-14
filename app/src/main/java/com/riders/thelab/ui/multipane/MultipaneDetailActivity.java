package com.riders.thelab.ui.multipane;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Movie;
import com.riders.thelab.ui.base.SimpleActivity;

import org.parceler.Parcels;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class MultipaneDetailActivity extends SimpleActivity {

    public static final String EXTRA_MOVIE = "MOVIE";
    //Views
    @BindView(R.id.multi_pane_movie_item_image_detail)
    ShapeableImageView ivMovieImage;
    @BindView(R.id.multi_pane_movie_item_title_detail)
    MaterialTextView tvTitleDetail;
    @BindView(R.id.multi_pane_movie_item_genre_detail)
    MaterialTextView tvGenreDetail;
    @BindView(R.id.multi_pane_movie_item_year_detail)
    MaterialTextView tvYearDetail;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_pane_detail);

        mContext = this;

        ButterKnife.bind(this);

        getBundle();
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (null == bundle) {
            Timber.e("Bundle is null exit activity.");
            finish();
        }

        Movie movie = Parcels.unwrap(Objects.requireNonNull(bundle).getParcelable(EXTRA_MOVIE));

        getSupportActionBar().setTitle(movie.getTitle());
        setViews(movie);

    }

    private void setViews(Movie movie) {

        Glide.with(mContext)
                .load(movie.getUrlThumbnail())
                .into(ivMovieImage);

        tvTitleDetail.setText(movie.getTitle());
        tvGenreDetail.setText(movie.getGenre());
        tvYearDetail.setText(movie.getYear());
    }
}
