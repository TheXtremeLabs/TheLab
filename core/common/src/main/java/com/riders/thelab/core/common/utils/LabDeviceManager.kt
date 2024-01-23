package com.riders.thelab.core.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.WindowMetrics
import timber.log.Timber
import java.io.File

object LabDeviceManager {

    const val MODEL_NAME_GALAXY_NOTE_4 = "SM-N910F"
    const val MODEL_NAME_GALAXY_NOTE_8 = "SM-N950F"
    const val MODEL_NAME_GALAXY_NOTE_20_ULTRA = "SM-N980B"

    // TODO : GoldFinger deprecated

    @Suppress("DEPRECATION")
    fun logDeviceInfo() {
        Timber.d("logDeviceInfo()")
        Timber.i("SERIAL: %s ", Build.SERIAL)
        Timber.i("MODEL: %s ", Build.MODEL)
        Timber.i("ID: %s ", Build.ID)
        Timber.i("Manufacture: %s ", Build.MANUFACTURER)
        Timber.i("brand: %s ", Build.BRAND)
        Timber.i("type: %s ", Build.TYPE)
        Timber.i("user: %s ", Build.USER)
        Timber.i("BASE: %s ", Build.VERSION_CODES.BASE)
        Timber.i("INCREMENTAL: %s ", Build.VERSION.INCREMENTAL)
        Timber.i("SDK : %s ", Build.VERSION.SDK)
        Timber.i("BOARD: %s ", Build.BOARD)
        Timber.i("BRAND: %s ", Build.BRAND)
        Timber.i("HOST: %s ", Build.HOST)
        Timber.i("FINGERPRINT: %s ", Build.FINGERPRINT)
        Timber.i("Version Code: %s ", Build.VERSION.RELEASE)
    }

    fun getDevice(): String? = Build.DEVICE

    fun getSerial(): String? = try {
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

        @SuppressLint("PrivateApi") val c = Class.forName("android.os.SystemProperties")
        val get = c.getMethod("get", String::class.java, String::class.java)
        val serial = get.invoke(c, "ril.serialnumber", "unknown") as String
        serial
    } catch (e: Exception) {
        Timber.e("Some error occurred : %s", e.message)
        null
    }

    fun getModel(): String = Build.MODEL.run {
        if (this.contains(MODEL_NAME_GALAXY_NOTE_8, true)) {
            "$this (Galaxy Note 8)"
        } else if (this.contains(MODEL_NAME_GALAXY_NOTE_4, true)) {
            "$this (Galaxy Note 4)"
        } else {
            this
        }
    }

    fun getID(): String? = Build.ID

    fun getManufacturer(): String? = Build.MANUFACTURER

    fun getBrand(): String? = Build.BRAND

    fun getType(): String? = Build.TYPE

    fun getUser(): String? = Build.USER

    fun getVersionBase(): Int = Build.VERSION_CODES.BASE

    fun getVersionIncremental(): String? = Build.VERSION.INCREMENTAL

    fun getSdkVersion(): Int = Build.VERSION.SDK_INT

    fun getBoard(): String? = Build.BOARD

    fun getHost(): String? = Build.HOST

    fun getFingerPrint(): String? = Build.FINGERPRINT

    fun getVersionCode(): String? = Build.VERSION.RELEASE

    fun getHardware(): String? = Build.HARDWARE

    fun getRelease(): String? = Build.VERSION.RELEASE


    ///////////////////////////////////////////////////////
    // Screen Dimensions
    ///////////////////////////////////////////////////////
    @SuppressLint("NewApi")
    fun getScreenHeight(activity: Activity): Int {
        val screenHeight: Int = if (LabCompatibilityManager.isAndroid10()
            && getModel().trim { it <= ' ' }.lowercase()
                .contains(Constants.EMULATOR_DEVICE_TAG)
        ) {
            val metrics = getDisplayMetricsAndroid10(activity)
            metrics.bounds.height()
        } else {
            val metrics = getDisplayMetrics(activity)
            metrics.heightPixels
        }
        return screenHeight
    }

    @SuppressLint("NewApi")
    fun getScreenWidth(activity: Activity): Int {
        val screenWidth: Int = if (LabCompatibilityManager.isAndroid10()
            && getModel().trim { it <= ' ' }.lowercase()
                .contains(Constants.EMULATOR_DEVICE_TAG)
        ) {
            val metrics = getDisplayMetricsAndroid10(activity)
            metrics.bounds.width()
        } else {
            val metrics = getDisplayMetrics(activity)
            metrics.widthPixels
        }
        return screenWidth
    }


    /**
     * Get DisplayMetrics for below Android 10 devices
     *
     * @param activity
     * @return
     */
    private fun getDisplayMetrics(activity: Activity): DisplayMetrics {
        //Retrieve Screen's height and width
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        activity
            .windowManager
            .defaultDisplay
            .getMetrics(metrics)
        return metrics
    }

    /**
     * Get WindowMetrics for Android 10+ devices
     *
     * @param activity
     * @return the [WindowMetrics] according to the current system state.
     */
    @SuppressLint("NewApi")
    fun getDisplayMetricsAndroid10(activity: Activity): WindowMetrics =
        (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).currentWindowMetrics

    ///////////////////////////////////////////////////////
    // Rooted
    ///////////////////////////////////////////////////////
    /**
     * Checks if the device is rooted.
     *
     * @return `true` if the device is rooted, `false` otherwise.
     */
    fun isRooted(): Boolean {

        // get from build info
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        // check if /system/app/Superuser.apk is present
        try {
            val file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                return true
            }
        } catch (exception: Exception) {
            // ignore
            exception.printStackTrace()
        }

        // try executing commands
        return (canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su")
                || canExecuteCommand("which su"))
    }


    /**
     * Check if the device can execute sudo sh command
     *
     * @param command
     * @return
     */
    private fun canExecuteCommand(command: String): Boolean = try {
        // executes a command on the system
        Runtime.getRuntime().exec(command)
        true
    } catch (e: Exception) {
        false
    }


    /////////////////////////////////////////
    // Vibration
    /////////////////////////////////////////
    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    fun vibrate(context: Context, ms: Int) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrator: VibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrator.vibrate(
                CombinedVibration.createParallel(
                    VibrationEffect.createOneShot(
                        ms.toLong(),
                        VibrationEffect.EFFECT_DOUBLE_CLICK
                    )
                )
            )
        } else {
            val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(ms.toLong())
        }
}
