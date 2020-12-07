package com.riders.thelab.ui.deviceinformation;

import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class DeviceInformationPresenter extends BasePresenterImpl<DeviceInformationView>
        implements DeviceInformationContract.Presenter {

    @Inject
    DeviceInformationPresenter() {
    }
}
