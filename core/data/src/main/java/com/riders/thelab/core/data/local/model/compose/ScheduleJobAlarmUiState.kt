package com.riders.thelab.core.data.local.model.compose

sealed class ScheduleJobAlarmUiState {
    data class Started(
        val started: Boolean,
        val countDownTime: Long,
        var millisUntilFinished: Long = countDownTime
    ) : ScheduleJobAlarmUiState()

    object Done : ScheduleJobAlarmUiState()
    object None : ScheduleJobAlarmUiState()
}
