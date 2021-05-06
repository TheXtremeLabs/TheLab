package com.riders.thelab.ui.deviceinformation;

import android.annotation.SuppressLint;

import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.DeviceInformation;
import com.riders.thelab.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class DeviceInformationView extends BaseViewImpl<DeviceInformationPresenter>
        implements DeviceInformationContract.View {

    //Views
    @BindView(R.id.tv_device_name)
    MaterialTextView tvDeviceName;
    @BindView(R.id.tv_device_brand)
    MaterialTextView tvDeviceBrand;
    @BindView(R.id.tv_device_model)
    MaterialTextView tvDeviceModel;
    @BindView(R.id.tv_device_hardware)
    MaterialTextView tvDeviceHardware;
    @BindView(R.id.tv_device_screen_dimensions)
    MaterialTextView tvDeviceScreenDimensions;
    @BindView(R.id.tv_device_serial)
    MaterialTextView tvDeviceSerial;
    @BindView(R.id.tv_device_fingerprint)
    MaterialTextView tvDeviceFingerPrint;
    @BindView(R.id.tv_device_android_name)
    MaterialTextView tvDeviceAndroidName;
    @BindView(R.id.tv_device_android_sdk)
    MaterialTextView tvDeviceAndroidSdk;
    @BindView(R.id.tv_device_android_release)
    MaterialTextView tvDeviceAndroidRelease;
    @BindView(R.id.tv_device_android_rooted)
    MaterialTextView tvDeviceRooted;
    private DeviceInformationActivity context;


    @Inject
    DeviceInformationView(DeviceInformationActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_device_information));

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        getPresenter().getDeviceInfo();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void updateViews(DeviceInformation deviceInformation) {
        Timber.d(deviceInformation.toString());

        tvDeviceName.setText(deviceInformation.getName());
        tvDeviceBrand.setText(deviceInformation.getBrand());
        tvDeviceModel.setText(deviceInformation.getModel());
        tvDeviceHardware.setText(deviceInformation.getHardware());
        tvDeviceScreenDimensions.setText(deviceInformation.getScreenWidth() + " x " + deviceInformation.getScreenHeight());
        tvDeviceFingerPrint.setText(deviceInformation.getFingerPrint());

        tvDeviceAndroidName.setText(deviceInformation.getAndroidVersionName());
        tvDeviceAndroidSdk.setText(deviceInformation.getSdkVersion() + "");
        tvDeviceAndroidRelease.setText(deviceInformation.getAndroidRelease());
        tvDeviceRooted.setText(
                deviceInformation.isRooted()
                        ? context.getString(R.string.action_yes)
                        : context.getString(R.string.action_no));
    }


    @Override
    public void onDestroy() {

        getPresenter().detachView();
        context = null;
    }
}
