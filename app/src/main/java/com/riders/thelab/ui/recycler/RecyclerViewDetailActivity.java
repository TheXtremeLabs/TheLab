package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.remote.dto.Artist;
import com.riders.thelab.utils.Validator;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class RecyclerViewDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECYCLER_ITEM = "recycler_item";
    public static final String EXTRA_TRANSITION_ICON_NAME = "icon";

    private RecyclerViewDetailActivity_ViewBinding viewBinding;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar_recycler_view_detail)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar_recycler_view_detail)
    Toolbar toolbar;
    ShapeableImageView ivBackgroundBlurred;
    @BindView(R.id.transition_imageView)
    ShapeableImageView transitionImageView;
    @BindView(R.id.tv_name_detail)
    MaterialTextView tvNameDetail;
    @BindView(R.id.tv_debutes_detail)
    MaterialTextView tvDebutesDetail;
    @BindView(R.id.tv_full_name_detail)
    MaterialTextView tvFullNameDetail;
    @BindView(R.id.tv_activities_detail)
    MaterialTextView tvActivitiesDetail;
    @BindView(R.id.description)
    MaterialTextView tvDescriptionDetail;

    private Artist item;

    private boolean isTablet;


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

        if (LabCompatibilityManager.isTablet(this)) {
            isTablet = true;
            bindTabletViews();
        }

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

        if (isTablet) {
            UIManager.loadImageBlurred(
                    this,
                    item.getUrlThumb(),
                    ivBackgroundBlurred);
            //Load the background  thumb image
            /*Glide.with(context)
                    .load(artist.getUrlThumb())
                    .apply(bitmapTransform(new BlurTransformation(5, 3)))
                    .into(ivBackgroundBlurred);*/

            tvDebutesDetail.setText("Since : " + item.getDebutes());
        }

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

    private void bindTabletViews() {
        ivBackgroundBlurred = findViewById(R.id.iv_background_blurred);
        tvDebutesDetail = findViewById(R.id.tv_debutes_detail);
    }
}
