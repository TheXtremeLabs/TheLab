package com.riders.thelab.feature.mlkit.data.local.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.io.Serializable

@Stable
@Immutable
@kotlinx.serialization.Serializable
data class BarcodeField(val type:String, val value:String):Serializable
