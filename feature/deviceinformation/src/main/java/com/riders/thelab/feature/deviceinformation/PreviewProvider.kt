package com.riders.thelab.feature.deviceinformation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.DeviceInformation

class PreviewProviderDeviceInformation : PreviewParameterProvider<DeviceInformation> {
    override val values: Sequence<DeviceInformation>
        get() = sequenceOf<DeviceInformation>(
            DeviceInformation(
                "Galaxy Note 8",
                "Samsung",
                "SM-N950F",
                "F344983456T56",
                "FingerPrint_439EZRFVGO",
                "KNOX_345956T",
                "334556056",
                "3",
                1934,
                2250,
                "Android Pie",
                29,
                "Android Pie",
                false
            ),
            DeviceInformation(
                "Galaxy Note 20 Ultra 5G",
                "Samsung",
                "SM-N980B",
                "F344983456T56",
                "FingerPrint_439EZRFVGO",
                "KNOX_659567T",
                "334556056",
                "1",
                1934,
                2650,
                "Tiramisu",
                33,
                "Tiramisu",
                false
            ),
        )
}