package com.riders.thelab.ui.multipane;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

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
        super.onDestroyView();
        unbinder.unbind();
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

        Picasso.get()
                .load(movie.getUrlThumbnail())
                .into(ivMovieImage, new ImageLoadedCallback(progressBar) {
                    @Override
                    public void onSuccess() {

                        if (this.progressBar != null) {
                            this.progressBar.setVisibility(View.GONE);

                            Timber.e("Picasso - Image bien téléchargée et affichée !!!");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        super.onError(e);

                        ivMovieImage.setImageResource(R.mipmap.ic_launcher);

                        Timber.e("Picasso - OOOOOOOHHH CA VA PAAAAAS LAAAAA !!!");

                        if (this.progressBar != null) {
                            this.progressBar.setVisibility(View.GONE);
                        }
                    }

                });

        tvTitleDetail.setText(movie.getTitle());
        tvGenreDetail.setText(movie.getGenre());
        tvYearDetail.setText(movie.getYear());
    }
    ///////////////////////////////////////////
    //
    //  Class methods
    //
    ///////////////////////////////////////////


    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
        }
    }
}
