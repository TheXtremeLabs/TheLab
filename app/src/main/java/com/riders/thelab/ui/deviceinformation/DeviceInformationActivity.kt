package com.riders.thelab.ui.deviceinformation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.DeviceInformation
import com.riders.thelab.databinding.ActivityDeviceInformationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceInformationActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityDeviceInformationBinding

    private val mViewModel: DeviceInfoViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityDeviceInformationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mViewModel
            .getDeviceInformation()
            .observe(this, {
                updateUI(it)
            })

        mViewModel.fetchDeviceInformation(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(deviceInformation: DeviceInformation) {

        viewBinding.contentDeviceLayout.tvDeviceName.text = deviceInformation.name
        viewBinding.contentDeviceLayout.tvDeviceBrand.text = deviceInformation.brand
        viewBinding.contentDeviceLayout.tvDeviceModel.text = deviceInformation.model
        viewBinding.contentDeviceLayout.tvDeviceHardware.text = deviceInformation.hardware
        viewBinding.contentDeviceLayout.tvDeviceScreenDimensions.text =
            deviceInformation.screenWidth.toString() + " x " + deviceInformation.screenHeight
        viewBinding.contentDeviceLayout.tvDeviceFingerprint.text = deviceInformation.fingerPrint

        viewBinding.contentDeviceLayout.tvDeviceAndroidName.text =
            deviceInformation.androidVersionName
        viewBinding.contentDeviceLayout.tvDeviceAndroidSdk.text =
            deviceInformation.sdkVersion.toString() + ""
        viewBinding.contentDeviceLayout.tvDeviceAndroidRelease.text =
            deviceInformation.androidRelease
        viewBinding.contentDeviceLayout.tvDeviceAndroidRooted.text =
            if (deviceInformation.rooted) getString(R.string.action_yes)
            else getString(R.string.action_no)
    }

}