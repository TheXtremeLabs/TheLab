package com.riders.thelab.ui.deviceinformation;

import com.riders.thelab.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DeviceInformationView extends BaseViewImpl<DeviceInformationPresenter>
        implements DeviceInformationContract.View {

    private DeviceInformationActivity context;

    @Inject
    DeviceInformationView(DeviceInformationActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));
    }

    @Override
    public void onDestroy() {

    }
}
