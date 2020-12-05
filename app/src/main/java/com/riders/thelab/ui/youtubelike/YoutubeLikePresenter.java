package com.riders.thelab.ui.youtubelike;

import android.content.Intent;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.riders.thelab.R;
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
    public void onYoutubeItemClicked(@NonNull ImageView view, Video video, int position) {
        Timber.d("onYoutubeItemClicked()");

        Timber.e("Click on : " + position + ", " + video.getName());

        Intent intent = new Intent(activity, YoutubeLikeDetailActivity.class);

        intent.putExtra(YoutubeLikeDetailActivity.VIDEO_OBJECT_ARG, video);

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Apply activity transition
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.
                            makeSceneTransitionAnimation(
                                    activity,
                                    view,
                                    ViewCompat.getTransitionName(view));

            navigator.callYoutubeDetailActivityWithTransition(intent, options.toBundle());

        } else {
            // Swap without transition
            navigator.callYoutubeDetailActivity(intent);
        }
    }
}
