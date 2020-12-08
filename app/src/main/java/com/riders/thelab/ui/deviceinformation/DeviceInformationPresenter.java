package com.riders.thelab.ui.deviceinformation;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.DisplayMetrics;

import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.LabDeviceManager;
import com.riders.thelab.data.local.model.DeviceInformation;
import com.riders.thelab.ui.base.BasePresenterImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

        deviceInformation.setName(Build.DEVICE);
        deviceInformation.setBrand(LabDeviceManager.getBrand());
        deviceInformation.setModel(LabDeviceManager.getModel());
        deviceInformation.setHardware(LabDeviceManager.getHardware());

        //Retrieve Screen's height and width
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        deviceInformation.setScreenWidth(metrics.widthPixels);
        deviceInformation.setScreenHeight(metrics.heightPixels);


        /**
         * http://stackoverflow.com/questions/14161282/serial-number-from-samsung-device-running-android
         *
         * The OP asked about Galaxy Tab 2 and for that indeed the answer was ril.serialnumber (even for the non-3G model - see this gist).
         * According to Himanshu's answer Galaxy Tab 3 uses sys.serialnumber (also backed by this answer).
         * sys.serialnumber makes better sense for tablets as ril.* stands for Radio Interface Layer, something most tablets are not equipped with
         * (ril.serialnumber, respectively, makes better sense for phones).
         *
         * There is no standard API for getting the device serial number
         * (ie the serial number on the packaging - not to be confused with Settings.Secure.ANDROID_ID or the various other "unique" identifiers scattered throughout the API).
         * This means it is up to the manufacturer to decide where to store the device serial (if at all).
         * On the S3 Mini it's ril.serialnumber,
         * on NexusOne it's ro.serialno (gist),
         * on Galaxy Tab 2 it's ril.serialnumber,
         * on Galaxy Tab 3/4 it's ril.serialnumber,
         * on Lenovo Tab it's none of the above.
         *
         * These settings appear to be the usual suspects, when looking for the device serial, but shouldn't be taken for granted,
         * and as such, shouldn't be relied on for tracking unique app installations.
         */

        try {

            @SuppressLint("PrivateApi") Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            String serial = (String) get.invoke(c, "ril.serialnumber", "unknown");
            deviceInformation.setSerial(serial);

        } catch (Exception e) {
            Timber.e("Some error occurred : %s", e.getMessage());
        }

        deviceInformation.setFingerPrint(LabDeviceManager.getFingerPrint());

        Field[] fields;
        fields = Build.VERSION_CODES.class.getFields();
        String codeName = "UNKNOWN";
        for (Field field : fields) {
            try {
                if (field.getInt(Build.VERSION_CODES.class) == (Build.VERSION.SDK_INT - 1)) {

                    codeName = field.getName();
                    Timber.e("code name %s", codeName);

                    if (LabCompatibilityManager.isOreo()) {
                        deviceInformation
                                .setAndroidVersionName(
                                        LabCompatibilityManager.getOsVersionName(codeName));
                    } else {

                        deviceInformation
                                .setAndroidVersionName(codeName);
                    }

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        fields = null;

        deviceInformation.setSdkVersion(LabDeviceManager.getSdkVersion());
        deviceInformation.setAndroidRelease(LabDeviceManager.getRelease());
        deviceInformation.setRooted(LabDeviceManager.isRooted());

        // TEST:
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ")
                        .append(fieldName).append(" : ")
                        .append("sdk=").append(fieldValue);
            }
        }
        fields = null;

        Timber.e("OS: %s", builder.toString());

        getView().updateViews(deviceInformation);
    }
}
