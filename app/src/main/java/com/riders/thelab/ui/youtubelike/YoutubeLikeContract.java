package com.riders.thelab.ui.youtubelike;

;

import androidx.annotation.NonNull;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.ui.base.BaseView;

import java.util.ArrayList;

public interface YoutubeLikeContract {

    interface View extends BaseView {

        void onStart();

        void onStop();

        void showLoader();

        void hideLoader();

        void onNoConnectionDetected();

        void onFetchedSuccessful(ArrayList<Video> youtubeList);

        void onFetchError();
    }

    interface Presenter {

        void fetchContent();

        void onYoutubeItemClicked(@NonNull ShapeableImageView view, Video video, int position);
    }
}
