package com.riders.thelab.feature.videocall.data

import android.os.Build
import androidx.compose.runtime.Stable
import com.riders.thelab.core.common.utils.LabDeviceManager
import com.riders.thelab.feature.videocall.BuildConfig

@Stable
data class ConnectState(
    val name: String = when {
        BuildConfig.DEBUG && Build.MODEL.contains(
            LabDeviceManager.MODEL_NAME_GALAXY_NOTE_8,
            ignoreCase = true
        ) -> "note8"

        BuildConfig.DEBUG && Build.MODEL.contains(
            LabDeviceManager.MODEL_NAME_GALAXY_NOTE_20_ULTRA,
            ignoreCase = true
        ) -> "note20"

        else -> ""
    },
    val isConnected: Boolean = false,
    val errorMessage: String? = null
) {

    override fun toString(): String {
        return "ConnectState(name=$name, isConnected=$isConnected, errorMessage=$errorMessage)"
    }
}