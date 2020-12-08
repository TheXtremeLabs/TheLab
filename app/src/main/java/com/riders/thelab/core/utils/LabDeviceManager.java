package com.riders.thelab.core.utils;

import android.os.Build;

import java.io.File;

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

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
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

    public static String getHardware() {
        return Build.HARDWARE;
    }

    public static String getRelease() {
        return Build.VERSION.RELEASE;
    }


    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    public static boolean isRooted() {

        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }
}
