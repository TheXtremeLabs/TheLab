package com.riders.thelab.ui.deviceinformation;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.ui.base.BaseActivity;

public class DeviceInformationActivity extends BaseActivity<DeviceInformationView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
