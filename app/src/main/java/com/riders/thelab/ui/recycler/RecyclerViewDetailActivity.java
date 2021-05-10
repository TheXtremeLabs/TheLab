package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.remote.dto.Artist;
import com.riders.thelab.databinding.ActivityRecyclerViewDetailBinding;
import com.riders.thelab.utils.Validator;

import org.parceler.Parcels;

import java.util.Objects;

import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class RecyclerViewDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECYCLER_ITEM = "recycler_item";
    public static final String EXTRA_TRANSITION_ICON_NAME = "icon";

    private ActivityRecyclerViewDetailBinding viewDetailBinding;

    private Artist item;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDetailBinding = ActivityRecyclerViewDetailBinding.inflate(getLayoutInflater());
        setContentView(viewDetailBinding.getRoot());

        getBundle();

        initCollapsingToolbar();

        if (null != item)
            bindData();
        else
            Timber.e("Cannot bind data for artist. Cause : Artist object is null");
    }


    /**
     * Retrieve bundle passed from Recycler View Activity
     * <p>
     * And bind value in artist object
     */
    private void getBundle() {
        Bundle extras = getIntent().getExtras();
        item = Parcels.unwrap(extras.getParcelable(EXTRA_RECYCLER_ITEM));
        Timber.d("item : %s", item.toString());
    }

    private void initCollapsingToolbar() {
        setSupportActionBar(viewDetailBinding.toolbarRecyclerViewDetail);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewDetailBinding.collapsingToolbarRecyclerViewDetail.setTitle(item.getArtistName());
        viewDetailBinding.collapsingToolbarRecyclerViewDetail
                .setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        viewDetailBinding.appBarLayout.setExpanded(true);

        viewDetailBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    viewDetailBinding.collapsingToolbarRecyclerViewDetail.setTitle(item.getArtistName());
                    isShow = true;
                } else if (isShow) {
                    viewDetailBinding.collapsingToolbarRecyclerViewDetail.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Display artist value
     */
    private void bindData() {
        if (LabCompatibilityManager.isTablet(this)) {
            //Load the background  thumb image
            UIManager.loadImageBlurred(
                    this,
                    item.getUrlThumb(),
                    viewDetailBinding.ivBackgroundBlurred);

            Objects.requireNonNull(viewDetailBinding.tvDebutesDetail)
                    .setText(String.format("Since : %s", item.getDebutes()));
        }

        // Display image
        Glide.with(this)
                .load(item.getUrlThumb())
                .into(viewDetailBinding.transitionImageView);

        viewDetailBinding.tvNameDetail.setText(item.getArtistName());

        StringBuilder sb = new StringBuilder();

        sb.append(item.getFirstName());

        if (null != item.getSecondName() && !Validator.isEmpty(item.getSecondName()))
            sb.append(", ").append(item.getSecondName());

        sb.append(" ").append(item.getLastName());
        viewDetailBinding.tvFullNameDetail.setText(sb.toString());

        viewDetailBinding.tvActivitiesDetail.setText(item.getActivities());
        viewDetailBinding.tvDescriptionDetail.setText(item.getDescription());
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (LabCompatibilityManager.isLollipop())
            finishAfterTransition();
    }
}
