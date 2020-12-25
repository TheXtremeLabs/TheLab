package com.riders.thelab.ui.mainactivity.fragment.time;

import com.riders.thelab.ui.base.BaseView;
import com.riders.thelab.ui.mainactivity.fragment.weather.WeatherFragment;

import javax.inject.Inject;

public class TimeFragmentView implements BaseView {

    TimeFragment timeFragment;

    @Inject
    TimeFragmentView() {
        this.timeFragment = timeFragment;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
