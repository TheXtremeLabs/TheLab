package com.riders.thelab.ui.palette;

import android.graphics.Bitmap;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.ui.base.BaseView;

public interface PaletteContract {

    interface View extends BaseView {

        void onLoadingImageSuccessful(Bitmap bitmap);

        void onLoadingImageFailed();
    }

    interface Presenter {
        void loadImage(ShapeableImageView targetImageView, String imageURL);
    }
}
