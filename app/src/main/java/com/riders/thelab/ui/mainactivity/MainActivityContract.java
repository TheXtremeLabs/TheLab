package com.riders.thelab.ui.mainactivity;


import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface MainActivityContract {

    interface View extends BaseView {


        void onStart();

        void onPause();

        void onResume();

        void onCreateOptionsMenu(Menu menu);

        void onOptionsItemSelected(MenuItem item);

        void startActivityForResult(Intent intent, int requestCode);

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

        void launchActivityOrPackage(App item);

        void launchIntentForPackage(String targetPackage);

        void launchActivity(Class<? extends Activity> targetActivity);
    }
}
