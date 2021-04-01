package com.riders.thelab.ui.youtubelike;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.LabNetworkManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenterImpl;

import java.util.ArrayList;

import javax.inject.Inject;

import timber.log.Timber;

public class YoutubeLikePresenter extends BasePresenterImpl<YoutubeLikeView>
        implements YoutubeLikeContract.Presenter {

    @Inject
    YoutubeLikeActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    LabService service;


    @Inject
    YoutubeLikePresenter() {
    }

    @SuppressLint("NewApi")
    @Override
    public void fetchContent() {
        Timber.d("fetchContent()");

        //Test the internet's connection
        if (!LabNetworkManager.isConnected(activity)) {
            Timber.e("No Internet connection");

            getView().onNoConnectionDetected();

            UIManager.showActionInToast(activity, activity.getResources().getString(R.string.pas_de_connexion));

        } else {

            getView().setAdapter();

            Timber.e("Fetch Content");
            makeRESTCallVideosData();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void makeRESTCallVideosData() {
        Timber.d("makeRESTCallVideosData()");
        service.getVideos()
                .subscribe(
                        videos -> {
                            if (videos.isEmpty()) {
                                getView().onFetchError();
                            }

                            getView().hideLoader();
                            getView().onFetchedSuccessful((ArrayList<Video>) videos);
                        },
                        throwable -> {
                            Timber.e(throwable);

                            getView().hideLoader();
                            getView().onFetchError();
                        });
    }

    @Override
    public void onYoutubeItemClicked(
            @NonNull ShapeableImageView thumbShapeableImageView,
            @NonNull MaterialTextView titleTextView,
            @NonNull MaterialTextView descriptionTextView,
            Video video,
            int position) {

        Timber.e("Click on : " + position + ", " + video.getName());

        // Variables
        ActivityOptionsCompat options = null;

        Pair<View, String> sePairThumb;
        Pair<View, String> sePairTitle;
        Pair<View, String> sePairDescription;


        Intent intent = new Intent(activity, YoutubeLikeDetailActivity.class);
        intent.putExtra(YoutubeLikeDetailActivity.VIDEO_OBJECT_ARG, video);

        // Check if we're running on Android 5.0 or higher
        if (LabCompatibilityManager.isLollipop()) {

            sePairThumb = Pair.create(thumbShapeableImageView, activity.getString(R.string.thumb_transition_name));
            sePairTitle = Pair.create(titleTextView, activity.getString(R.string.title_transition_name));
            sePairDescription = Pair.create(descriptionTextView, activity.getString(R.string.description_transition_name));

            options =
                    ActivityOptionsCompat.
                            makeSceneTransitionAnimation(
                                    activity,
                                    sePairThumb, sePairTitle, sePairDescription);
        }

        navigator.callYoutubeDetailActivity(
                intent,
                !LabCompatibilityManager.isLollipop()
                        ? null
                        : options.toBundle(),
                LabCompatibilityManager.isLollipop());
    }
}
