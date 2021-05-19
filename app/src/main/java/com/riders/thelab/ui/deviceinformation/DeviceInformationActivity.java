package com.riders.thelab.ui.deviceinformation;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.R;

public class DeviceInformationActivity extends BaseActivity<DeviceInformationView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_information);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
