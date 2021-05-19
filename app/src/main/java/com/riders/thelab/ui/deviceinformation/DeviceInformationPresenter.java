package com.riders.thelab.ui.deviceinformation;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;

import com.riders.thelab.core.utils.LabDeviceManager;
import com.riders.thelab.data.local.model.DeviceInformation;

import javax.inject.Inject;

import timber.log.Timber;

public class DeviceInformationPresenter extends BasePresenterImpl<DeviceInformationView>
        implements DeviceInformationContract.Presenter {

    @Inject()
    DeviceInformationActivity activity;


    @Inject
    DeviceInformationPresenter() {
    }

    @SuppressLint("NewApi")
    @Override
    public void getDeviceInfo() {
        Timber.d("getDeviceInfo()");

        DeviceInformation deviceInformation = new DeviceInformation();

        deviceInformation.setName(LabDeviceManager.getDevice());
        deviceInformation.setBrand(LabDeviceManager.getBrand());
        deviceInformation.setModel(LabDeviceManager.getModel());
        deviceInformation.setHardware(LabDeviceManager.getHardware());

        //Retrieve Screen's height and width
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        deviceInformation.setScreenWidth(metrics.widthPixels);
        deviceInformation.setScreenHeight(metrics.heightPixels);

        deviceInformation.setSerial(LabDeviceManager.getSerial());

        deviceInformation.setFingerPrint(LabDeviceManager.getFingerPrint());

        deviceInformation
                .setAndroidVersionName(
                        LabCompatibilityManager.getOSName());

        deviceInformation.setSdkVersion(LabDeviceManager.getSdkVersion());
        deviceInformation.setAndroidRelease(LabDeviceManager.getRelease());
        deviceInformation.setRooted(LabDeviceManager.isRooted());

        getView().updateViews(deviceInformation);
    }
}
