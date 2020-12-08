package com.riders.thelab.core.utils;

/**
 * Created by Michaël on 07/03/2017.
 */


import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Class that check compatibility, SDK's version and stuff to be sure that your device can handle the actions
 */
public class LabCompatibilityManager {


    /**
     * This class can't be instantiated.
     */
    private LabCompatibilityManager() {
    }

    /**
     * Get the current Android API level.
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Determine if the device is running API level 8 or higher.
     */
    public static boolean isFroyo() {
        return getSdkVersion() >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Determine if the device is running API level 11 or higher.
     */
    public static boolean isHoneycomb() {
        return getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Determine if the device is running API level 19 or higher.
     */
    public static boolean isKitkat() {
        return getSdkVersion() >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Determine if the device is running API level 21 or higher.
     */
    public static boolean isLollipop() {
        return getSdkVersion() >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Determine if the device is running API level 23 or higher.
     */
    public static boolean isMarshmallow() {
        return getSdkVersion() >= Build.VERSION_CODES.M;
    }

    /**
     * Determine if the device is running API level is higher than 21.
     */
    public static boolean isLollipopPlus() {
        return getSdkVersion() > Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Determine if the device is running API level 26 or higher.
     */
    public static boolean isOreo() {
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

    /**
     * Gets the version name from version code. Note! Needs to be updated
     * when new versions arrive, or will return a single letter. Like Android 8.0 - Oreo
     * yields "O" as a version name.
     *
     * @return version name of device's OS
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getOsVersionName(String name) {
        if (name.equals("Q")) return "Android 10";
        if (name.equals("P")) return "Pie";
        if (name.equals("O")) return "Oreo";
        if (name.equals("N")) return "Nougat";
        if (name.equals("M")) return "Marshmallow";

        if (name.startsWith("O_")) return "Oreo++";
        if (name.startsWith("N_")) return "Nougat++";

        return "UNKNOWN";
    }
}


/**
 * Android version table
 *
 * SDK_INT value        Build.VERSION_CODES        Human Version Name
 *     1                  BASE                      Android 1.0 (no codename)
 *     2                  BASE_1_1                  Android 1.1 Petit Four
 *     3                  CUPCAKE                   Android 1.5 Cupcake
 *     4                  DONUT                     Android 1.6 Donut
 *     5                  ECLAIR                    Android 2.0 Eclair
 *     6                  ECLAIR_0_1                Android 2.0.1 Eclair
 *     7                  ECLAIR_MR1                Android 2.1 Eclair
 *     8                  FROYO                     Android 2.2 Froyo
 *     9                  GINGERBREAD               Android 2.3 Gingerbread
 *    10                  GINGERBREAD_MR1           Android 2.3.3 Gingerbread
 *    11                  HONEYCOMB                 Android 3.0 Honeycomb
 *    12                  HONEYCOMB_MR1             Android 3.1 Honeycomb
 *    13                  HONEYCOMB_MR2             Android 3.2 Honeycomb
 *    14                  ICE_CREAM_SANDWICH        Android 4.0 Ice Cream Sandwich
 *    15                  ICE_CREAM_SANDWICH_MR1    Android 4.0.3 Ice Cream Sandwich
 *    16                  JELLY_BEAN                Android 4.1 Jellybean
 *    17                  JELLY_BEAN_MR1            Android 4.2 Jellybean
 *    18                  JELLY_BEAN_MR2            Android 4.3 Jellybean
 *    19                  KITKAT                    Android 4.4 KitKat
 *    20                  KITKAT_WATCH              Android 4.4 KitKat Watch
 *    21                  LOLLIPOP                  Android 5.0 Lollipop
 *    22                  LOLLIPOP_MR1              Android 5.1 Lollipop
 *    23                  M                         Android 6.0 Marshmallow
 *    24                  N                         Android 7.0 Nougat
 *    25                  N_MR1                     Android 7.1.1 Nougat
 *    26                  O                         Android 8.0 Oreo
 *    27                  O_MR1                     Android 8 Oreo MR1
 *    28                  P                         Android 9 Pie
 *    29                  Q                         Android 10
 *   10000                CUR_DEVELOPMENT           Current Development Version
 *
 *
 *
 */