package com.riders.thelab.core.data.local.model.compose

import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class WhatsNewAppModel(
    val title: String,
    val subtitle: String,
    val icon: Bitmap,
    val appActivity: Class<out Activity>,
)
