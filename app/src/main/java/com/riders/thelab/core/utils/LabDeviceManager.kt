package com.riders.thelab.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import co.infinum.goldfinger.Goldfinger
import co.infinum.goldfinger.Goldfinger.PromptParams
import co.infinum.goldfinger.crypto.CipherCrypter
import co.infinum.goldfinger.crypto.CipherFactory
import co.infinum.goldfinger.crypto.impl.AesCipherFactory
import co.infinum.goldfinger.crypto.impl.Base64CipherCrypter
import co.infinum.goldfinger.rx.RxGoldfinger
import com.riders.thelab.utils.Constants
import timber.log.Timber
import java.io.File

class LabDeviceManager private constructor() {
    companion object {

        //FingerPrint variables
        private var goldFinger: Goldfinger? = null

        // RX approach
        private var rxGoldFinger: RxGoldfinger? = null
        private var params: PromptParams? = null

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

        fun getDevice(): String? {
            return Build.DEVICE
        }

        fun getSerial(): String? {
            var serial: String? = null
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
                @SuppressLint("PrivateApi") val c = Class.forName("android.os.SystemProperties")
                val get = c.getMethod("get", String::class.java, String::class.java)
                serial = get.invoke(c, "ril.serialnumber", "unknown") as String
                return serial
            } catch (e: Exception) {
                Timber.e("Some error occurred : %s", e.message)
            }
            return serial
//        return Build.SERIAL;
        }

        fun getModel(): String {
            return Build.MODEL
        }

        fun getID(): String? {
            return Build.ID
        }

        fun getManufacturer(): String? {
            return Build.MANUFACTURER
        }

        fun getBrand(): String? {
            return Build.BRAND
        }

        fun getType(): String? {
            return Build.TYPE
        }

        fun getUser(): String? {
            return Build.USER
        }

        fun getVersionBase(): Int {
            return Build.VERSION_CODES.BASE
        }

        fun getVersionIncremental(): String? {
            return Build.VERSION.INCREMENTAL
        }

        fun getSdkVersion(): Int {
            return Build.VERSION.SDK_INT
        }

        fun getBoard(): String? {
            return Build.BOARD
        }

        fun getHost(): String? {
            return Build.HOST
        }

        fun getFingerPrint(): String? {
            return Build.FINGERPRINT
        }

        fun getVersionCode(): String? {
            return Build.VERSION.RELEASE
        }

        fun getHardware(): String? {
            return Build.HARDWARE
        }

        fun getRelease(): String? {
            return Build.VERSION.RELEASE
        }

        @SuppressLint("NewApi")
        fun getScreenHeight(activity: Activity): Int {
            var screenHeight = 0
            screenHeight = if (LabCompatibilityManager.isAndroid10()
                && getModel().trim { it <= ' ' }.toLowerCase()
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
            var screenWidth = 0
            screenWidth = if (LabCompatibilityManager.isAndroid10()
                && getModel().trim { it <= ' ' }.toLowerCase()
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
        fun getDisplayMetrics(activity: Activity): DisplayMetrics {
            //Retrieve Screen's height and width
            val metrics = DisplayMetrics()
            activity
                .windowManager
                .defaultDisplay
                .getMetrics(metrics)
            return metrics
        }

        /**
         * Get WindowMetrics for above and equal Android 10 devices
         *
         * @param activity
         * @return
         */
        @SuppressLint("NewApi")
        fun getDisplayMetricsAndroid10(activity: Activity): WindowMetrics {
            val manager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            return WindowMetrics(
                manager.currentWindowMetrics.bounds,
                manager.currentWindowMetrics.windowInsets
            )
        }


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
            } catch (e1: Exception) {
                // ignore
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
        private fun canExecuteCommand(command: String): Boolean {
            val executedSuccesfully: Boolean
            executedSuccesfully = try {
                // executes a command on the system
                Runtime.getRuntime().exec(command)
                true
            } catch (e: Exception) {
                false
            }
            return executedSuccesfully
        }

        /**
         * Init the basic GoldFinger variable
         *
         * @param context
         * @param activity
         */
        @RequiresApi(api = Build.VERSION_CODES.M)
        fun initFingerPrint(context: Context?, activity: FragmentActivity) {

            // fingerprint instantiation
            goldFinger = Goldfinger.Builder(context!!).build()

            // fingerprint prompt instantiation
            initGoldFingerPromptParams(activity)
        }

        /**
         * Init the RxGoldFinger variable
         *
         * @param context
         * @param activity
         */
        @RequiresApi(api = Build.VERSION_CODES.M)
        fun initFingerPrintWithRx(context: Context?, activity: FragmentActivity) {
            val factory: CipherFactory = AesCipherFactory(context!!)
            val crypter: CipherCrypter = Base64CipherCrypter()
            rxGoldFinger = RxGoldfinger.Builder(context)
                .logEnabled(true)
                .cipherFactory(factory)
                .cipherCrypter(crypter)
                .build()

            // fingerprint prompt instantiation
            initGoldFingerPromptParams(activity)
        }

        private fun initGoldFingerPromptParams(activity: FragmentActivity) {
            params = PromptParams.Builder(activity)
                .title("Title")
                .negativeButtonText("Cancel")
                .description("Description")
                .subtitle("Subtitle")
                .build()
        }

        /**
         * This method returns if the device has fingerprint hardware or not
         *
         * @return
         */
        fun hasFingerPrintHardware(): Boolean {
            if (null != goldFinger) return goldFinger!!.hasFingerprintHardware() else if (null != rxGoldFinger) return rxGoldFinger!!.hasFingerprintHardware()
            return false
        }

        /**
         * Returns GoldFinger (using the basic approach) variable
         *
         * @return
         */
        fun getGoldFinger(): Goldfinger? {
            return goldFinger
        }

        /**
         * Returns GoldFinger (using the reactiveX approach) variable
         *
         * @return
         */
        fun getRxGoldFinger(): RxGoldfinger? {
            return rxGoldFinger
        }

        /**
         * Returns GoldFinger prompt params
         *
         * @return
         */
        fun getGoldFingerPromptParams(): PromptParams? {
            return params
        }
    }
}