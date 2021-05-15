package com.riders.thelab.ui.splashscreen;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;


public class SplashScreenActivity extends BaseActivity<SplashScreenView> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT > 16 && Build.VERSION.SDK_INT < 29) {
            /*View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            int uiOptions2 = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //decorView.setSystemUiVisibility(uiOptions);
            decorView.setSystemUiVisibility(uiOptions2);*/
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            /*ActionBar actionBar = getActionBar();
            actionBar.hide();*/
            Window w = getWindow();
            w.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_splashscreen);
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
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
