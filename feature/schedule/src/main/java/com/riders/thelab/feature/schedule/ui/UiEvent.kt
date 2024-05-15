package com.riders.thelab.feature.schedule.ui

import com.riders.thelab.core.data.local.model.compose.ScheduleJobAlarmUiState

sealed interface UiEvent {
    data class OnUpdateScheduleJobAlarmUiState(val newState: ScheduleJobAlarmUiState) : UiEvent
    data object OnStartButtonClicked : UiEvent
    data class OnUpdateCountDownQuery(val value: String) : UiEvent
    data class OnUpdateCountDownStarted(val isStared: Boolean) : UiEvent
    data class OnUpdateLoadingViewVisible(val isLoadingVisible: Boolean) : UiEvent
    data class OnUpdateUiCountDown(val millisUntilFinished: Long) : UiEvent
    data class OnUpdateCountDownDone(val isCountDownDone: Boolean) : UiEvent
}

