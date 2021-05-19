package com.riders.thelab.ui.youtubelike;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

        boolean isConnected = false;

        //Test the internet's connection
        if (!LabCompatibilityManager.isLollipop()) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            isConnected = networkInfo != null && networkInfo.isConnected();

            if (!isConnected) {
                Timber.e("No Internet connection");

                getView().onNoConnectionDetected();
                UIManager.showActionInToast(activity, activity.getResources().getString(R.string.network_status_disconnected));
                return;
            }

        } else {
            if (!LabNetworkManager.isConnected(activity)) {
                Timber.e("No Internet connection");

                getView().onNoConnectionDetected();
                UIManager.showActionInToast(activity, activity.getResources().getString(R.string.network_status_disconnected));
            }
        }

        getView().initAdapter();

        Timber.e("Fetch Content");
        makeRESTCallVideosData();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void makeRESTCallVideosData() {
        Timber.d("makeRESTCallVideosData()");
        service.getVideos()
                .subscribe(
                        videos -> {
                            if (videos.isEmpty()) {
                                getView().onFetchError();
                                getView().hideLoader();
                                return;
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
            @NonNull final ShapeableImageView thumbShapeableImageView,
            @NonNull final MaterialTextView titleTextView,
            @NonNull final MaterialTextView descriptionTextView,
            final Video video,
            final int position) {

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
                                    this.activity,
                                    sePairThumb, sePairTitle, sePairDescription);
        }

        // Call navigator to switch activity with or without transition according
        // to the device's version running the application
        navigator.callYoutubeDetailActivity(
                intent,
                !LabCompatibilityManager.isLollipop()
                        ? null
                        : options.toBundle(),
                LabCompatibilityManager.isLollipop());
    }
}
