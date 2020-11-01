package com.riders.thelab.ui.splashscreen;

import android.content.Context;

import com.riders.thelab.ui.base.BaseView;


public interface SplashScreenContract {

    interface View extends BaseView {
        void onPermissionsGranted();

        void onPermissionsDenied();

        void displayAppVersion(String appVersion);

        void closeApp();
    }

    interface Presenter {
        void hasPermissions(Context context);

        void getAppVersion();
    }
}
