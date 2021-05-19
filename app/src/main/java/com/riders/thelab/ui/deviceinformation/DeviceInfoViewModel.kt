package com.riders.thelab.ui.deviceinformation

import android.util.DisplayMetrics
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.LabDeviceManager
import com.riders.thelab.data.local.model.DeviceInformation
import timber.log.Timber

class DeviceInfoViewModel : ViewModel() {

    var deviceInformation: MutableLiveData<DeviceInformation> = MutableLiveData()

    fun getDeviceInformation(): LiveData<DeviceInformation> {
        return deviceInformation
    }

    fun fetchDeviceInformation(activity: DeviceInformationActivity) {
        Timber.d("getDeviceInfo()")

        //Retrieve Screen's height and width
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)

        val mDeviceInfo = DeviceInformation(
                LabDeviceManager.getDevice().toString(),
                LabDeviceManager.getBrand().toString(),
                LabDeviceManager.getModel(),
                LabDeviceManager.getSerial().toString(),
                LabDeviceManager.getFingerPrint().toString(),
                LabDeviceManager.getHardware().toString(),
                LabDeviceManager.getSerial().toString(),
                LabDeviceManager.getID().toString(),
                metrics.widthPixels,
                metrics.heightPixels,
                LabCompatibilityManager.getOSName(),
                LabDeviceManager.getSdkVersion(),
                LabDeviceManager.getRelease().toString(),
                LabDeviceManager.isRooted()
        )

        deviceInformation.value = mDeviceInfo
    }

}