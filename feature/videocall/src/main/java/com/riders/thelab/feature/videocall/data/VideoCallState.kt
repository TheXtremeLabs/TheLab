package com.riders.thelab.feature.videocall.data

import androidx.compose.runtime.Stable
import io.getstream.video.android.core.Call

enum class CallState {
    IDLE,
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    FAILED,
    CANCELLED,
}

@Stable
data class VideoCallState(
    val call: Call,
    val state: CallState? = null,
    val error: Throwable? = null
)
