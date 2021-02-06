package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.LabDeviceManager;
import com.riders.thelab.data.local.model.Contact;
import com.riders.thelab.ui.contacts.ContactDetailActivity;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class BottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.tv_bottom_brand)
    MaterialTextView tvBrand;
    @BindView(R.id.tv_bottom_model)
    MaterialTextView tvModel;
    @BindView(R.id.tv_bottom_screen_height)
    MaterialTextView tvScreenHeight;
    @BindView(R.id.tv_bottom_screen_width)
    MaterialTextView tvScreenWidth;
    @BindView(R.id.tv_bottom_version)
    MaterialTextView tvVersion;


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

    public static BottomSheetFragment newInstance(Contact contact) {

        Bundle args = new Bundle();

        args.putString(ContactDetailActivity.CONTACT_NAME, contact.toString());

        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
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

        //setViews();

        if (null != getArguments()) {
            String szBundle = getArguments().getString(ContactDetailActivity.CONTACT_NAME);
            Timber.d(szBundle);
        } else {
            setViews();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setViews() {
        tvBrand.setText(LabDeviceManager.getBrand());
        tvModel.setText(LabDeviceManager.getModel());
        tvScreenHeight.setText(LabDeviceManager.getScreenHeight(getActivity()) + "");
        tvScreenWidth.setText(LabDeviceManager.getScreenWidth(getActivity()) + "");
        tvVersion.setText(LabDeviceManager.getSdkVersion() + " " + LabCompatibilityManager.getOSName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.e("DestroyView()");
    }
}
