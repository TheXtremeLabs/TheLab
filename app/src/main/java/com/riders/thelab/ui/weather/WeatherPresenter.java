package com.riders.thelab.ui.weather;

import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class WeatherPresenter extends BasePresenterImpl<WeatherView>
        implements WeatherContract.Presenter {

    @Inject
    WeatherActivity activity;


    @Inject
    WeatherPresenter() {
    }

    @Override
    public void getCityDataFromFile() {

    }
}
