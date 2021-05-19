package com.riders.thelab.ui.deviceinformation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.data.local.model.DeviceInformation
import com.riders.thelab.databinding.ActivityDeviceInformationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceInformationActivity : AppCompatActivity() {
    private var context: Context? = null

    lateinit var viewBinding: ActivityDeviceInformationBinding

    private val mViewModel: DeviceInfoViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityDeviceInformationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = context!!.getString(R.string.activity_title_device_information)

        mViewModel
                .getDeviceInformation()
                .observe(this, {
                    updateUI(it)
                })

        mViewModel.fetchDeviceInformation(this)
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

        viewBinding.contentDeviceLayout.tvDeviceAndroidName.text = deviceInformation.androidVersionName
        viewBinding.contentDeviceLayout.tvDeviceAndroidSdk.text = deviceInformation.sdkVersion.toString() + ""
        viewBinding.contentDeviceLayout.tvDeviceAndroidRelease.text = deviceInformation.androidRelease
        viewBinding.contentDeviceLayout.tvDeviceAndroidRooted.text =
                if (deviceInformation.rooted) context!!.getString(R.string.action_yes)
                else context!!.getString(R.string.action_no)
    }

}