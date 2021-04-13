package com.riders.thelab.ui.youtubelike;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.UIManager;
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

    // Context
    private YoutubeLikeActivity context;

    // Views
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

        setupToolbar();
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

        UIManager.showActionInToast(context, "Unable to fetch content");
    }

    @SuppressLint("NewApi")
    private void setupToolbar() {
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_youtube_like));

        if (LabCompatibilityManager.isLollipop()) {
            this.context.getSupportActionBar()
                    .setBackgroundDrawable(new ColorDrawable(
                            ContextCompat.getColor(
                                    context,
                                    R.color.swipeDownColorPrimary)));
            this.context.getWindow()
                    .setStatusBarColor(
                            ContextCompat.getColor(
                                    context,
                                    R.color.swipeDownColorPrimaryDark));
        }
    }

    public void initAdapter() {
        Timber.d("initAdapter()");

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        contentRecyclerView.setLayoutManager(layoutManager);
        contentRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onYoutubeItemClicked(
            @NonNull ShapeableImageView thumbShapeableImageView,
            @NonNull MaterialTextView titleTextView,
            @NonNull MaterialTextView descriptionTextView,
            Video video,
            int position) {
        getPresenter().onYoutubeItemClicked(
                thumbShapeableImageView,
                titleTextView,
                descriptionTextView,
                video,
                position);
    }
}
