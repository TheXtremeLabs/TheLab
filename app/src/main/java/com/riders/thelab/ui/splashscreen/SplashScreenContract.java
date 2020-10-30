package com.riders.thelab.ui.splashscreen;

import android.content.Context;

import com.riders.thelab.ui.base.BaseView;


public interface SplashScreenContract {

    interface View extends BaseView {
        void onPermissionsGranted();

        void onPermissionsDenied();

        void closeApp();
    }

    interface Presenter {
        boolean hasPermissions(Context context);
    }
}
