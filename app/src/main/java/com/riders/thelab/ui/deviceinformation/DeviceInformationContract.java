package com.riders.thelab.ui.deviceinformation;

import com.riders.thelab.data.local.model.DeviceInformation;
import com.riders.thelab.ui.base.BaseView;

public interface DeviceInformationContract {

    interface View extends BaseView {
        void updateViews(DeviceInformation deviceInformation);
    }

    interface Presenter {
        void getDeviceInfo();

    }
}
