package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
/*
        supportPostponeEnterTransition();
        transitionImageView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        transitionImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                }
        );*/
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

    private void loadWithGlide() {
        Glide.with(this)
                .load(R.drawable.logo_colors)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                })
                .into(transitionImageView);


    }
}
