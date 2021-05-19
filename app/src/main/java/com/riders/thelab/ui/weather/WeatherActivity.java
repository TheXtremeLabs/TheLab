package com.riders.thelab.ui.weather;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.riders.thelab.R;

public class WeatherActivity extends BaseActivity<WeatherView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_weather);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        view.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        view.onResume();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        view.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        view.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
