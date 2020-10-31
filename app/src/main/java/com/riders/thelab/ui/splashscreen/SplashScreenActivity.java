package com.riders.thelab.ui.splashscreen;

import android.os.Bundle;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;


public class SplashScreenActivity extends BaseActivity<SplashScreenView> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splashscreen);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
