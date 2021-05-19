package com.riders.thelab.ui.deviceinformation;

import com.riders.thelab.data.local.model.DeviceInformation;

public interface DeviceInformationContract {

    interface View extends BaseView {
        void updateViews(DeviceInformation deviceInformation);
    }

    interface Presenter {
        void getDeviceInfo();

    }
}
