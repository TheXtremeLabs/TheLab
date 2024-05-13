package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable

@Stable
data class Download(
    val id: Int = 0,
    val filename: String,
    var progress: Int = 0,
    var isComplete: Boolean = false,
    var isError: Pair<Boolean, String>? = null,
    var downloadRefId: Long = -1
) {
    override fun toString(): String {
        return "Filename: $filename with progress: $progress (id: $downloadRefId | completed? $isComplete)"
    }
}
