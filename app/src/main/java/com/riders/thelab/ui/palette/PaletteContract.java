package com.riders.thelab.ui.palette;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.riders.thelab.ui.base.BaseView;

public interface PaletteContract {

    interface View extends BaseView {

        void onLoadingImageSuccessful(Bitmap bitmap);

        void onLoadingImageFailed();
    }

    interface Presenter {
        void loadImage(ImageView targetImageView, String imageURL);
    }
}
