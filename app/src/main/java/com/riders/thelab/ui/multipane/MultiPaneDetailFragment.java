package com.riders.thelab.ui.multipane;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Movie;

import org.parceler.Parcels;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class MultiPaneDetailFragment extends Fragment {

    private Context mContext;

    public static final String BUNDLE_MOVIE = "movie";

    //Views
    private View rootView;
    @BindView(R.id.multi_pane_movie_image_detail)
    ShapeableImageView ivMovieImage;
    @BindView(R.id.multi_pane_movie_title_detail)
    MaterialTextView tvTitleDetail;
    @BindView(R.id.multi_pane_movie_genre_detail)
    MaterialTextView tvGenreDetail;
    @BindView(R.id.multi_pane_movie_year_detail)
    MaterialTextView tvYearDetail;
    @BindView(R.id.multi_pane_movie_loader_image)
    ProgressBar progressBar;
    private Unbinder unbinder;

    private static MultiPaneDetailFragment mFragmentInstance;


    public static MultiPaneDetailFragment newInstance() {
        if (null == mFragmentInstance) {
            mFragmentInstance = new MultiPaneDetailFragment();
        }

        return mFragmentInstance;
    }

    public static MultiPaneDetailFragment newInstance(Movie movie) {

        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_MOVIE, Parcels.wrap(movie));

        MultiPaneDetailFragment fragment = new MultiPaneDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MultiPaneDetailFragment getInstance() {
        return mFragmentInstance;
    }


    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_multi_pane_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////


    ///////////////////////////////////////////
    //
    //  Class methods
    //
    ///////////////////////////////////////////
    public void displayContent() {

        Bundle bundle = this.getArguments();

        if (null == bundle) {
            Timber.e("Bundle is null - Cannot retrieve movie");
            return;
        }

        Movie movie = Parcels.unwrap(bundle.getParcelable(BUNDLE_MOVIE));
        setTextOnFragment(movie);
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void setTextOnFragment(Movie movie) {

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        Timber.e("URL RECEIVE : %s", movie.getUrlThumbnail());

        Glide.with(Objects.requireNonNull(getActivity()))
                .load(movie.getUrlThumbnail())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ivMovieImage.setImageResource(R.mipmap.ic_launcher);

                        Timber.e("Picasso - OOOOOOOHHH CA VA PAAAAAS LAAAAA !!!");

                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);

                            Timber.e("Picasso - Image bien téléchargée et affichée !!!");
                        }
                        return false;
                    }
                })
                .into(ivMovieImage);

        tvTitleDetail.setText(movie.getTitle());
        tvGenreDetail.setText(movie.getGenre());
        tvYearDetail.setText(movie.getYear());
    }
}
