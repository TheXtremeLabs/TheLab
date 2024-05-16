package com.riders.thelab.feature.schedule.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.ScheduleJobAlarmUiState

class PreviewProvider : PreviewParameterProvider<ScheduleJobAlarmUiState> {
    override val values: Sequence<ScheduleJobAlarmUiState>
        get() = sequenceOf(
            ScheduleJobAlarmUiState.Started(true, 20L),
            ScheduleJobAlarmUiState.Done,
            ScheduleJobAlarmUiState.Idle
        )
}