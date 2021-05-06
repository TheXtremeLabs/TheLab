package com.riders.thelab.ui.splashscreen;

import android.content.Context;
import android.os.Bundle;

import com.riders.thelab.ui.base.BaseView;


public interface SplashScreenContract {

    interface View extends BaseView {
        void onPermissionsGranted();

        void onPermissionsDenied();

        void onSaveInstanceState(Bundle savedInstanceState);

        void onRestoreInstanceState(Bundle savedInstanceState);

        void onPause();

        void onResume();

        void displayAppVersion(String appVersion);

        void closeApp();
    }

    interface Presenter {
        void hasPermissions(Context context);

        void getAppVersion();
    }
}
