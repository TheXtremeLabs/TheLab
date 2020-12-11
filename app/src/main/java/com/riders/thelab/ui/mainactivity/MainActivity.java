package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.ui.base.BaseActivity;

import timber.log.Timber;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity<MainActivityView> {

    Context context;

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

        context = this;
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

                WifiManager wifiManager =
                        (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);

                if (!LabCompatibilityManager.isAndroid10()) {
                    boolean isWifi = wifiManager.isWifiEnabled();
                    wifiManager.setWifiEnabled(!isWifi);
                } else {
                    Timber.e("For applications targeting android.os.Build.VERSION_CODES Q or above, this API will always fail and return false");

                    /*
                        ACTION_INTERNET_CONNECTIVITY Shows settings related to internet connectivity, such as Airplane mode, Wi-Fi, and Mobile Data.
                        ACTION_WIFI Shows Wi-Fi settings, but not the other connectivity settings. This is useful for apps that need a Wi-Fi connection to perform large uploads or downloads.
                        ACTION_NFC Shows all settings related to near-field communication (NFC).
                        ACTION_VOLUME Shows volume settings for all audio streams.
                     */
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    this.startActivityForResult(panelIntent, 955);
                }
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


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

        Timber.e("startActivityForResult()");

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