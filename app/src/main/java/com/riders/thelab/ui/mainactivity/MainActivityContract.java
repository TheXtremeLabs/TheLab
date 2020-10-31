package com.riders.thelab.ui.mainactivity;


import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface MainActivityContract {

    interface View extends BaseView {

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
         * Fetch movies data from external API
         */
        void getPackageList();
    }
}
