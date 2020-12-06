package com.riders.thelab.ui.weather;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;

public class WeatherActivity extends BaseActivity<WeatherView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_weather);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        view.onStart();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
