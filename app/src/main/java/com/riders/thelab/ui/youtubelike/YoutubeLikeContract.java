package com.riders.thelab.ui.youtubelike;

import androidx.annotation.NonNull;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.ui.base.BaseView;

import java.util.ArrayList;

public interface YoutubeLikeContract {

    interface View extends BaseView {

        void showLoader();

        void hideLoader();

        void onNoConnectionDetected();

        void onFetchedSuccessful(ArrayList<Video> youtubeList);

        void onFetchError();
    }

    interface Presenter {

        void fetchContent();

        void onYoutubeItemClicked(
                @NonNull final ShapeableImageView thumbShapeableImageView,
                @NonNull final MaterialTextView titleTextView,
                @NonNull final MaterialTextView descriptionTextView,
                final Video video,
                final int position);
    }
}
