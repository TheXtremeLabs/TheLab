package com.riders.thelab.ui.multipane;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.riders.thelab.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class MultiPaneDetailFragment extends Fragment {

    private Context mContext;

    //Views
    @BindView(R.id.multi_pane_movie_item_image_detail)
    ImageView ivMovieImage;
    @BindView(R.id.multi_pane_movie_item_title_detail)
    TextView tvTitleDetail;
    @BindView(R.id.multi_pane_movie_item_genre_detail)
    TextView tvGenreDetail;
    @BindView(R.id.multi_pane_movie_item_year_detail)
    TextView tvYearDetail;
    @BindView(R.id.multi_pane_movie_item_loader_image)
    ProgressBar progressBar;
    private Unbinder unbinder;


    public static MultiPaneDetailFragment newInstance() {

        Bundle args = new Bundle();

        MultiPaneDetailFragment fragment = new MultiPaneDetailFragment();
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_multi_pane_detail, container, false);
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
    public void setTextOnFragment(String title, String genre, String year, String movieUrl) {

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        Timber.e("URL RECEIVE : %s", movieUrl);

        Picasso.get()
                .load(movieUrl)
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

        tvTitleDetail.setText(title);
        tvGenreDetail.setText(genre);
        tvYearDetail.setText(year);
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
