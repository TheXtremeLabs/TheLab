package com.riders.thelab.ui.deviceinformation;

import android.annotation.SuppressLint;
import android.widget.TextView;

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

    private DeviceInformationActivity context;

    //Views
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_device_brand)
    TextView tvDeviceBrand;
    @BindView(R.id.tv_device_model)
    TextView tvDeviceModel;
    @BindView(R.id.tv_device_hardware)
    TextView tvDeviceHardware;
    @BindView(R.id.tv_device_screen_dimensions)
    TextView tvDeviceScreenDimensions;
    @BindView(R.id.tv_device_serial)
    TextView tvDeviceSerial;
    @BindView(R.id.tv_device_fingerprint)
    TextView tvDeviceFingerPrint;
    @BindView(R.id.tv_device_android_name)
    TextView tvDeviceAndroidName;
    @BindView(R.id.tv_device_android_sdk)
    TextView tvDeviceAndroidSdk;
    @BindView(R.id.tv_device_android_release)
    TextView tvDeviceAndroidRelease;
    @BindView(R.id.tv_device_android_rooted)
    TextView tvDeviceRooted;


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
                        ? context.getString(R.string.yes)
                        : context.getString(R.string.no));
    }


    @Override
    public void onDestroy() {

        getPresenter().detachView();
        context = null;
    }
}
