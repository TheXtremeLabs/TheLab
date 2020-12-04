package com.riders.thelab.ui.mainactivity;


import android.app.Activity;
import android.os.Bundle;

import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface MainActivityContract {

    interface View extends BaseView {

        void onSaveInstanceState(Bundle savedInstanceState);

        void onRestoreInstanceState(Bundle savedInstanceState);

        void onPause();

        void onResume();

        /**
         * Shows loader progressBar
         */
        void showLoading();

        /**
         * Hide loader progressBar
         */
        void hideLoading();


        /**
         * When the data has been correctly fetched from server and display them
         *
         * @param
         */
        void onSuccessPackageList(final List<App> applications);

        /**
         * When the data has been correctly saved in database
         */
        void onErrorPackageList();

        void closeApp();
    }

    interface Presenter {

        /**
         * Retrieve applications from both compiled classes and packages installed on the devices
         */
        void getApplications();

        void launchIntentForPackage(String targetPackage);

        void launchActivity(Class <? extends Activity> targetActivity);
    }
}
