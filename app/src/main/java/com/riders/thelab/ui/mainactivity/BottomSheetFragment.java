package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.riders.thelab.R;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class BottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.tv_bottom_brand)
    TextView tvBrand;
    @BindView(R.id.tv_bottom_model)
    TextView tvModel;
    @BindView(R.id.tv_bottom_screen_height)
    TextView tvScreenHeight;
    @BindView(R.id.tv_bottom_screen_width)
    TextView tvScreenWidth;
    @BindView(R.id.tv_bottom_version)
    TextView tvVersion;


    //Variables
    private String deviceModel;
    private String deviceBrand;
    private int deviceScreenHeight = 0;
    private int deviceScreenWidth = 0;
    private int deviceVersionSDK = 0;
    private Field[] fields;
    private String OSName = "UNKNOWN";

    public BottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.i("onViewCreated()");

        //Retrieve data
        getDeviceInfo();
        setViews();
    }

    private void getDeviceInfo() {
        Timber.i("getDeviceInfo()");

        deviceBrand = Build.BRAND;
        deviceModel = Build.MODEL;

        //Retrieve Screen's height and width
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity()
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);

        deviceScreenHeight = metrics.heightPixels;
        deviceScreenWidth = metrics.widthPixels;

        deviceVersionSDK = Build.VERSION.SDK_INT;

        fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            Timber.i(field.toString());
            try {
                if (field.getInt(Build.VERSION_CODES.class) == Build.VERSION.SDK_INT) {
                    OSName = field.getName();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setViews() {
        tvBrand.setText(deviceBrand);
        tvModel.setText(deviceModel);
        tvScreenHeight.setText(deviceScreenHeight + "");
        tvScreenWidth.setText(deviceScreenWidth + "");
        tvVersion.setText(deviceVersionSDK + " " + OSName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.e("DestroyView()");
    }
}
