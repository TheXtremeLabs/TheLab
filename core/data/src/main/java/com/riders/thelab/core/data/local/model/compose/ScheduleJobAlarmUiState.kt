package com.riders.thelab.core.data.local.model.compose

import androidx.compose.runtime.Stable

@Stable
sealed interface ScheduleJobAlarmUiState {
    data class Started(
        val started: Boolean,
        val countDownTime: Long,
        var millisUntilFinished: Long = countDownTime
    ) : ScheduleJobAlarmUiState

    data object Done : ScheduleJobAlarmUiState
    data object Idle : ScheduleJobAlarmUiState
}
