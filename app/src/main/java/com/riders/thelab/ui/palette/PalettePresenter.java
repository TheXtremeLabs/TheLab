package com.riders.thelab.ui.palette;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;

import javax.inject.Inject;

import timber.log.Timber;

public class PalettePresenter extends BasePresenterImpl<PaletteView>
        implements PaletteContract.Presenter {

    @Inject
    PaletteActivity activity;

    @Inject
    PalettePresenter() {
    }


    @Override
    public void loadImage(final ShapeableImageView targetImageView, final String imageURL) {

        Glide.with(activity)
                .load(imageURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Timber.e(e);

                        getView().onLoadingImageFailed();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        Timber.i("Image is correctly downloaded");
                        getView().onLoadingImageSuccessful(resource);
                        return false;
                    }
                })
                .into(targetImageView);
    }
}
