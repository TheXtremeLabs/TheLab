package com.riders.thelab.ui.weather;

import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.ui.base.BaseView;

import java.util.ArrayList;

public interface WeatherContract {

    interface View extends BaseView {

        void onStart();

        void onStop();

        void showLoader();

        void hideLoader();

        void updateUI();

        void onNoConnectionDetected();

        void onFetchedSuccessful(ArrayList<Video> youtubeList);

        void onFetchError();

    }

    interface Presenter {

        void getCityDataFromFile();

    }
}
