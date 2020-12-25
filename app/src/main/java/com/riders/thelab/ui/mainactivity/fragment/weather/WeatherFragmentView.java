package com.riders.thelab.ui.mainactivity.fragment.weather;

import com.riders.thelab.ui.base.BaseView;

import javax.inject.Inject;

public class WeatherFragmentView implements BaseView {

    WeatherFragment weatherFragment;

    @Inject
    WeatherFragmentView(WeatherFragment weatherFragment) {
        this.weatherFragment = weatherFragment;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
