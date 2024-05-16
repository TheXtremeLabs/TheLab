package com.riders.thelab.feature.schedule.ui

import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.ScheduleJobAlarmUiState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_surfaceTint
import com.riders.thelab.core.ui.compose.theme.md_theme_light_surfaceTint
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleContent(
    scheduleState: ScheduleJobAlarmUiState,
    tooltipState: TooltipState,
    countDownQuery: String,
    uiCountDown: Long,
    isCountDownStarted: Boolean,
    isCountDownDone: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    val focusManager: FocusManager = LocalFocusManager.current

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TheLabTopAppBar(stringResource(id = R.string.activity_title_schedule_jobs)) }
        ) { contentPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.TopCenter),
                    visible = scheduleState is ScheduleJobAlarmUiState.Started
                ) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(.7f)
                            .focusRequester(focusRequester),
                        value = countDownQuery,
                        onValueChange = { newValue ->
                            uiEvent.invoke(
                                UiEvent.OnUpdateCountDownQuery(
                                    newValue
                                )
                            )
                        },
                        placeholder = { Text(text = "Enter a value to start the count down") },
                        label = { Text(text = "Count Down") },
                        singleLine = true,
                        maxLines = 1,
                        trailingIcon = {
                            if (countDownQuery.isBlank()) {
                                TooltipBox(
                                    tooltip = { Text(text = "Field can't be empty. Please enter a valid number") },
                                    state = tooltipState,
                                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
                                ) {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                if (!tooltipState.isVisible) {
                                                    tooltipState.show()
                                                } else {
                                                    tooltipState.dismiss()
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.QuestionMark,
                                            contentDescription = null,
                                            tint = if (countDownQuery.isEmpty()) com.riders.thelab.core.ui.compose.theme.error else if (!isSystemInDarkTheme()) md_theme_light_surfaceTint else md_theme_dark_surfaceTint
                                        )
                                    }
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Done,
                            showKeyboardOnFocus = true
                        ),
                        readOnly = false
                    )

                    Button(
                        onClick = { uiEvent.invoke(UiEvent.OnStartButtonClicked) },
                        enabled = scheduleState !is ScheduleJobAlarmUiState.Started,
                    ) {
                        Text(modifier = Modifier.padding(end = 8.dp), text = "Start count Down")
                        Icon(
                            modifier = Modifier.padding(start = 8.dp),
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = "clock icon"
                        )
                    }

                    AnimatedVisibility(visible = scheduleState is ScheduleJobAlarmUiState.Started) {
                        Row {
                            Text(text = "Time remaining $uiCountDown s")
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(isCountDownStarted) {
        Timber.d("LaunchedEffect(viewModel.isCountDownStarted) with: $isCountDownStarted")

        // Should update loading view
        uiEvent.invoke(UiEvent.OnUpdateLoadingViewVisible(isCountDownStarted))

        if (isCountDownStarted) {
            focusManager.clearFocus(true)
            keyboardController?.hide()

            val millsFuture: Long = (uiCountDown * 1000)

            scope.launch {
                object : CountDownTimer(millsFuture, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        uiEvent.invoke(UiEvent.OnUpdateUiCountDown(millisUntilFinished / 1000))
                    }

                    override fun onFinish() {
                        Timber.e("Count down finished")
                        uiEvent.invoke(UiEvent.OnUpdateCountDownDone(true))
                        uiEvent.invoke(
                            UiEvent.OnUpdateScheduleJobAlarmUiState(ScheduleJobAlarmUiState.Done)
                        )
                    }
                }.start()
            }
        }
    }

    LaunchedEffect(isCountDownDone) {
        Timber.d("LaunchedEffect(viewModel.isCountDownDone) with: $isCountDownDone")

        uiEvent.invoke(UiEvent.OnUpdateLoadingViewVisible(isCountDownDone))

        if (isCountDownDone) {
            uiEvent.invoke(UiEvent.OnUpdateCountDownStarted(false))
            // viewModel.updateUIState(ScheduleJobAlarmUiState.Done)
            focusManager.clearFocus(true)
            keyboardController?.hide()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
private fun PreviewScheduleContent(@PreviewParameter(PreviewProvider::class) scheduleState: ScheduleJobAlarmUiState) {
    val tooltipState = TooltipState()

    TheLabTheme {
        ScheduleContent(
            scheduleState,
            tooltipState,
            "2",
            1000L,
            true,
            false
        ) {}
    }
}