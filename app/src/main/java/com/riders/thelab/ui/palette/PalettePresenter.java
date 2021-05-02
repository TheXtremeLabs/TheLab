package com.riders.thelab.ui.palette;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

import timber.log.Timber;

public class PalettePresenter extends BasePresenterImpl<PaletteView>
        implements PaletteContract.Presenter {

    @Inject
    PaletteActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    PalettePresenter() {
    }


    @Override
    public void loadImage(ShapeableImageView targetImageView, String imageURL) {

        Glide.with(activity)
                .load(imageURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Timber.e(e);
                        e.printStackTrace();

                        getView().onLoadingImageFailed();
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        Timber.i("Image is correctly downloaded");

                        //retrouver le bitmap téléchargé par Picasso
                        Bitmap bitmap = ((BitmapDrawable) targetImageView.getDrawable()).getBitmap();

                        getView().onLoadingImageSuccessful(bitmap);
                        return true;
                    }
                })
                .into(targetImageView);
    }
}
