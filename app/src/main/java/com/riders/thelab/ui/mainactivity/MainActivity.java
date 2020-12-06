package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.ui.base.BaseActivity;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity<MainActivityView> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // In Activity's onCreate() for instance
        // make fully Android Transparent Status bar
        if (LabCompatibilityManager.isLollipop()) {
            Window w = getWindow();
            /*w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
            w.setStatusBarColor(Color.TRANSPARENT);
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        view.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        view.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        view.onCreateOptionsMenu(menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connection_icon:
                UIManager.showActionInToast(this, "Wifi clicked");
                // TODO : Modify wifi state
                break;

            case R.id.action_settings:
                UIManager.showActionInToast(this, "Settings clicked");
                break;

            case R.id.info_icon:
                showBottomSheetDialogFragment();
                break;

            case R.id.action_force_crash:
                throw new RuntimeException("This is a crash");

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showBottomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}