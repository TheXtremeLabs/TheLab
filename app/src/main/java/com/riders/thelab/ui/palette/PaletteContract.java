package com.riders.thelab.ui.palette;

import android.graphics.drawable.Drawable;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.ui.base.BaseView;

public interface PaletteContract {

    interface View extends BaseView {

        void onLoadingImageSuccessful(final Drawable drawable);

        void onLoadingImageFailed();
    }

    interface Presenter {
        void loadImage(final ShapeableImageView targetImageView, final String imageURL);
    }
}
