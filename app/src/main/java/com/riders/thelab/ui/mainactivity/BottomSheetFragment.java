package com.riders.thelab.ui.mainactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.riders.thelab.R;

import java.lang.reflect.Method;


public class BottomSheetFragment extends BottomSheetDialogFragment {

    // TAG
    private static final String TAG = BottomSheetDialogFragment.class.getSimpleName();

    //Variables
    private String deviceDevice = null,
            deviceModel = null,
            deviceBrand = null,
            deviceHardware = null,
            deviceProduct = null,
            deviceManufact = null,
            deviceSerial = null,
            deviceIMEI = null,
            deviceBoard = null,
            deviceBootlaoder = null,
            deviceDisplay = null,
            deviceFingerprint = null,
            deviceID = null,
            deviceTags = null,
            deviceType = null;

    private int deviceScreenHeight = 0,
            deviceScreenWidth = 0,
            deviceVersionSDK = 0;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated()");

        //Retrieve data
        getDeviceInfo();
    }

    private void getDeviceInfo() {
        Log.i(TAG, "getDeviceInfo");

        deviceDevice = Build.DEVICE;
        deviceModel = Build.MODEL;
        deviceBrand = Build.BRAND;

        //Retrieve Screen's height and width
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        deviceScreenHeight = metrics.heightPixels;
        deviceScreenWidth = metrics.widthPixels;

        deviceHardware = Build.HARDWARE;
        deviceProduct = Build.PRODUCT;
        deviceManufact = Build.MANUFACTURER;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            deviceSerial = (String) get.invoke(c, "ril.serialnumber", "unknown");
        } catch (Exception e) {
            Log.e(TAG, "Some error occured : " + e.getMessage());
        }


        /**
         * http://stackoverflow.com/questions/1972381/how-to-get-the-devices-imei-esn-programmatically-in-android
         */
        final TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_PHONE_STATE)
                .withListener(new PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        deviceIMEI = tm.getDeviceId();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Log.e(TAG, "android.permission.read_phone_state : access not granted");

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                });

        if (deviceIMEI == null || deviceIMEI.length() == 0)
            deviceIMEI = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        deviceBoard = Build.BOARD;
        deviceBootlaoder = Build.BOOTLOADER;
        deviceDisplay = Build.DISPLAY;
        deviceFingerprint = Build.FINGERPRINT;
        deviceID = Build.ID;
        deviceTags = Build.TAGS;
        deviceType = Build.TYPE;
        deviceVersionSDK = Build.VERSION.SDK_INT;

        String logDeviceInfo =
                "Model : " + deviceModel + " \n" +
                        "Product : " + deviceProduct + "(don't need) \n" +
                        "Manufacturer : " + deviceManufact + "(not necessary) \n" +
                        "Serial : " + deviceSerial + " \n" +
                        "Brand : " + deviceBrand + " \n" +
                        "Display : " + deviceDisplay + "(don't need) \n" +
                        "Screen Width : " + deviceScreenWidth + " \n" +
                        "Screen Height : " + deviceScreenHeight + " \n" +
                        "Hardware : " + deviceHardware + " \n" +
                        "Version SDK : API " + deviceVersionSDK + " \n" +
                        "IMEI : " + deviceIMEI + "\n" +
                        "Fingerprint : " + deviceFingerprint + "\n";

        Log.i(TAG, "\n" + logDeviceInfo);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "DestroyView()");
    }
}
