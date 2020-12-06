package com.riders.thelab.ui.youtubelike;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class YoutubeLikeView extends BaseViewImpl<YoutubeLikePresenter>
        implements YoutubeLikeContract.View, YoutubeListClickListener {

    private YoutubeLikeActivity context;

    @BindView(R.id.no_connection_linear_container)
    LinearLayout mLinearNoConnectionContainer;

    @BindView(R.id.youtube_content_loader)
    ProgressBar mLoader;

    @BindView(R.id.youtube_content_recyclerView)
    RecyclerView contentRecyclerView;

    private YoutubeLikeListAdapter contentAdapter;


    @Inject
    YoutubeLikeView(YoutubeLikeActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_youtube_like));

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        showLoader();

        getPresenter().fetchContent();
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();

        context = null;
    }

    @Override
    public void onStart() {
        Timber.d("onStart()");

    }

    @Override
    public void onStop() {
        Timber.d("onStop()");

    }

    @Override
    public void showLoader() {

        //create a new progress bar for each image to be loaded
        mLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        mLoader.setVisibility(View.GONE);
    }

    @Override
    public void onNoConnectionDetected() {
        hideLoader();
        mLinearNoConnectionContainer.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onFetchedSuccessful(ArrayList<Video> youtubeList) {
        Timber.d("onFetchedSuccessful()");

        if (contentRecyclerView != null && !contentRecyclerView.isInLayout()) {
            contentAdapter = new YoutubeLikeListAdapter(context, youtubeList, this);
            contentRecyclerView.setVisibility(View.VISIBLE);
            contentRecyclerView.setAdapter(contentAdapter);
        }
    }

    @Override
    public void onFetchError() {
        Timber.e("onFetchError()");

    }

    public void setAdapter() {
        Timber.d("setAdapter()");

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        contentRecyclerView.setLayoutManager(layoutManager);
        contentRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public YoutubeLikeListAdapter getContentAdapter() {
        return this.contentAdapter;
    }

    @Override
    public void onYoutubeItemClicked(@NonNull ImageView view, Video video, int position) {
        getPresenter().onYoutubeItemClicked(view, video, position);
    }
}
