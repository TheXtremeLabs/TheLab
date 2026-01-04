package com.riders.thelab.feature.videocall.ui.video

sealed interface VideoCallUiEvent {
    object OnJoinClick : VideoCallUiEvent
    object OnLeaveClick : VideoCallUiEvent
    object OnSwitchCameraClick : VideoCallUiEvent
}