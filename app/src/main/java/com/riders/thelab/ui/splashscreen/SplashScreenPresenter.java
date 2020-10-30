package com.riders.thelab.ui.splashscreen;

import android.Manifest;
import android.content.Context;

import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;


public class SplashScreenPresenter extends BasePresenterImpl<SplashScreenView>
        implements SplashScreenContract.Presenter {

    private static final int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Inject
    SplashScreenPresenter() {
    }

    @Override
    public boolean hasPermissions(Context context) {
        return false;
    }
}
