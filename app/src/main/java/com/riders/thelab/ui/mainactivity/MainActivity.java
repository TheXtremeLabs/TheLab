package com.riders.thelab.ui.mainactivity;

import android.os.Bundle;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<MainActivityView> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}