package com.riders.thelab.core.utils;

/**
 * Created by Michaël on 07/03/2017.
 */


import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

/**
 * Class that check compatibility, SDK's version and stuff to be sure that your device can handle the actions
 */
public class LabCompatibilityManager {


    /** This class can't be instantiated. */
    private LabCompatibilityManager() { }

    /** Get the current Android API level. */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /** Determine if the device is running API level 8 or higher. */
    public static boolean isFroyo() {
        return getSdkVersion() >= Build.VERSION_CODES.FROYO;
    }

    /** Determine if the device is running API level 11 or higher. */
    public static boolean isHoneycomb() {
        return getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB;
    }

    /** Determine if the device is running API level 19 or higher. */
    public static boolean isKitkat(){
        return getSdkVersion() >= Build.VERSION_CODES.KITKAT;
    }

    /** Determine if the device is running API level 21 or higher. */
    public static boolean isLollipop(){
        return getSdkVersion() >= Build.VERSION_CODES.LOLLIPOP;
    }

    /** Determine if the device is running API level 23 or higher. */
    public static boolean isMarshmallow(){
        return getSdkVersion() >= Build.VERSION_CODES.M;
    }

    /** Determine if the device is running API level is higher than 21. */
    public static boolean isLollipopPlus(){
        return getSdkVersion() > Build.VERSION_CODES.LOLLIPOP;
    }

    /** Determine if the device is running API level 26 or higher. */
    public static boolean isOreo(){
        return getSdkVersion() >= Build.VERSION_CODES.O;
    }


    /**
     * Determine if the device is a tablet (i.e. it has a large screen).
     *
     * @param context The calling context.
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    /**
     * Determine if the device is a HoneyComb tablet.
     *
     * @param context The calling context.
     */
    public static boolean isHoneycombTablet(Context context) {
        return isHoneycomb() && isTablet(context);
    }
}
