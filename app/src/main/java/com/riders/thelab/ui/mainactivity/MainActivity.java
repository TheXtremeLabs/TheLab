package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.base.BaseActivity;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity<MainActivityView>
        implements MainActivityAppClickListener {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        // In Activity's onCreate() for instance
        // make fully Android Transparent Status bar
        if (LabCompatibilityManager.isLollipop()) {

            int nightModeFlags =
                    context.getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    Window w = getWindow();
                    /*w.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
                    w.setStatusBarColor(Color.TRANSPARENT);
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    break;
            }

            getWindow()
                    .setNavigationBarColor(ContextCompat.getColor(this, R.color.default_dark));


        }
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        view.onStart();
        super.onStart();
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


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        view.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        view.startActivityForResult(intent, requestCode);
        super.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onAppItemCLickListener(View rootView, App item, int position) {
        view.onAppItemCLickListener(rootView, item, position);
    }
}