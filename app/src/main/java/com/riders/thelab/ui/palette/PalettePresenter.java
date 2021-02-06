package com.riders.thelab.ui.palette;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenterImpl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        Picasso.get()
                .load(imageURL)
                //.load(R.drawable.image1)
                .fit()
                .centerCrop()
                .into(
                        targetImageView,
                        //j'écoute le chargement via picasso
                        new Callback() {
                            @Override

                            //puis lorsque l'image a bien été chargée
                            public void onSuccess() {

                                Timber.i("Image is correctly downloaded");

                                //retrouver le bitmap téléchargé par Picasso
                                Bitmap bitmap = ((BitmapDrawable) targetImageView.getDrawable()).getBitmap();

                                getView().onLoadingImageSuccessful(bitmap);

                            }

                            @Override
                            public void onError(Exception e) {
                                Timber.e(e);
                                e.printStackTrace();

                                getView().onLoadingImageFailed();
                            }
                        });
    }
}
