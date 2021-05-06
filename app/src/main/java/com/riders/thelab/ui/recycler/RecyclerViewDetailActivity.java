package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.remote.dto.Artist;
import com.riders.thelab.ui.base.SimpleActivity;
import com.riders.thelab.utils.Validator;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class RecyclerViewDetailActivity extends SimpleActivity {

    public static final String EXTRA_RECYCLER_ITEM = "recycler_item";
    public static final String EXTRA_TRANSITION_ICON_NAME = "icon";

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar_recycler_view_detail)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar_recycler_view_detail)
    Toolbar toolbar;
    @BindView(R.id.transition_imageView)
    ShapeableImageView transitionImageView;
    @BindView(R.id.tv_name_detail)
    MaterialTextView tvNameDetail;
    @BindView(R.id.tv_full_name_detail)
    MaterialTextView tvFullNameDetail;
    @BindView(R.id.tv_activities_detail)
    MaterialTextView tvActivitiesDetail;
    @BindView(R.id.description)
    MaterialTextView tvDescriptionDetail;

    Artist item;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_detail);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        item = Parcels.unwrap(extras.getParcelable(EXTRA_RECYCLER_ITEM));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Timber.d("item : %s", item.toString());


        collapsingToolbar.setTitle(item.getArtistName());
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(item.getArtistName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });


        // Display image
        Glide.with(this)
                .load(item.getUrlThumb())
                .into(transitionImageView);

        tvNameDetail.setText(item.getArtistName());

        StringBuilder sb = new StringBuilder();

        sb.append(item.getFirstName());
        if (null != item.getSecondName()
                && !Validator.isEmpty(item.getSecondName()))
            sb.append(", ").append(item.getSecondName());
        sb.append(" ").append(item.getLastName());
        tvFullNameDetail.setText(sb.toString());

        tvActivitiesDetail.setText(item.getActivities());

        tvDescriptionDetail.setText(item.getDescription());
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (LabCompatibilityManager.isLollipop())
            finishAfterTransition();
    }
}
