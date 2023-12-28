package com.riders.thelab.feature.deviceinformation

import android.util.DisplayMetrics
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.common.utils.LabDeviceManager
import com.riders.thelab.core.data.local.model.DeviceInformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class DeviceInformationViewModel : ViewModel() {

    var deviceInfo: DeviceInformation? by mutableStateOf(null)
        private set

    fun updateDeviceInfo(deviceInformation: DeviceInformation) {
        this.deviceInfo = deviceInformation
    }


    fun fetchDeviceInformation(activity: DeviceInformationActivity) {
        Timber.d("getDeviceInfo()")

        viewModelScope.launch(Dispatchers.IO) {
            //Retrieve Screen's height and width
            val metrics = DisplayMetrics()

            @Suppress("DEPRECATION")
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

            delay(1_500)

            withContext(Dispatchers.Main) {
                updateDeviceInfo(mDeviceInfo)
            }
        }
    }
}