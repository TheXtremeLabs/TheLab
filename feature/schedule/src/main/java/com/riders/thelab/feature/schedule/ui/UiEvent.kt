package com.riders.thelab.feature.schedule.ui

import androidx.compose.runtime.Stable
import com.riders.thelab.core.data.local.model.compose.ScheduleJobAlarmUiState

@Stable
sealed interface UiEvent {
    data class OnUpdateScheduleJobAlarmUiState(val newState: ScheduleJobAlarmUiState) : UiEvent
    data object OnStartButtonClicked : UiEvent
    data class OnUpdateCountDownQuery(val newValue: String) : UiEvent
    data class OnUpdateCountDownStarted(val isStared: Boolean) : UiEvent
    data class OnUpdateLoadingViewVisible(val isLoadingVisible: Boolean) : UiEvent
    data class OnUpdateUiCountDown(val millisUntilFinished: Long) : UiEvent
    data class OnUpdateCountDownDone(val isCountDownDone: Boolean) : UiEvent
}

