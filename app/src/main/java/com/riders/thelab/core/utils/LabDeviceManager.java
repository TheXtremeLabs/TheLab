package com.riders.thelab.core.utils;

import android.os.Build;

import timber.log.Timber;

public class LabDeviceManager {

    private LabDeviceManager() {
    }

    public static void logDeviceInfo() {
        Timber.d("logDeviceInfo()");
        Timber.i("SERIAL: %s ", Build.SERIAL);
        Timber.i("MODEL: %s ", Build.MODEL);
        Timber.i("ID: %s ", Build.ID);
        Timber.i("Manufacture: %s ", Build.MANUFACTURER);
        Timber.i("brand: %s ", Build.BRAND);
        Timber.i("type: %s ", Build.TYPE);
        Timber.i("user: %s ", Build.USER);
        Timber.i("BASE: %s ", Build.VERSION_CODES.BASE);
        Timber.i("INCREMENTAL: %s ", Build.VERSION.INCREMENTAL);
        Timber.i("SDK : %s ", Build.VERSION.SDK);
        Timber.i("BOARD: %s ", Build.BOARD);
        Timber.i("BRAND: %s ", Build.BRAND);
        Timber.i("HOST: %s ", Build.HOST);
        Timber.i("FINGERPRINT: %s ", Build.FINGERPRINT);
        Timber.i("Version Code: %s ", Build.VERSION.RELEASE);
    }

    public static String getSerial() {
        return Build.SERIAL;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getID() {
        return Build.ID;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getType() {
        return Build.TYPE;
    }

    public static String getUser() {
        return Build.USER;
    }

    public static int getVersionBase() {
        return Build.VERSION_CODES.BASE;
    }

    public static String getVersionIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    public static String getSdkVersion() {
        return Build.VERSION.SDK;
    }

    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getHost() {
        return Build.HOST;
    }

    public static String getFingerPrint() {
        return Build.FINGERPRINT;
    }

    public static String getVersionCode() {
        return Build.VERSION.RELEASE;
    }


}
