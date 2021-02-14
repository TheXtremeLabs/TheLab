package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.model.RecyclerItem;
import com.riders.thelab.ui.base.SimpleActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class RecyclerViewDetailActivity extends SimpleActivity {

    public static final String EXTRA_RECYCLER_ITEM = "recycler_item";
    public static final String EXTRA_TRANSITION_ICON_NAME = "icon";

    @BindView(R.id.transition_imageView)
    ShapeableImageView transitionImageView;

    RecyclerItem item;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_detail);
        supportPostponeEnterTransition();

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        item = Parcels.unwrap(extras.getParcelable(EXTRA_RECYCLER_ITEM));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(item.getName());

        if (LabCompatibilityManager.isLollipop()) {
            String imageTransitionName = extras.getString(EXTRA_TRANSITION_ICON_NAME);
            ViewCompat.setTransitionName(transitionImageView, imageTransitionName);
        }

        loadWithPicasso();
    }

    private void loadWithPicasso() {
        Picasso.get()
                .load(R.drawable.logo_colors)
                .noFade()
                .into(transitionImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {
                        supportStartPostponedEnterTransition();

                    }
                });

    }
}
