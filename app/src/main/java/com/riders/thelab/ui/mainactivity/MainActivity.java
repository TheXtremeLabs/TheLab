package com.riders.thelab.ui.mainactivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<MainActivityView> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // In Activity's onCreate() for instance
        // make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}