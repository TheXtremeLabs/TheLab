package com.riders.thelab.ui.schedule

import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.TheLabTopAppBar
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.utils.findActivity
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.core.views.toast.ToastTypeEnum
import com.riders.thelab.data.local.model.compose.ScheduleJobAlarmUiState
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ScheduleContent(viewModel: ScheduleViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context.findActivity() as ScheduleActivity
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    val focusManager: FocusManager = LocalFocusManager.current

    val scheduleState by viewModel.scheduleJobUiState.collectAsStateWithLifecycle()

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
                        value = viewModel.countDownQuery,
                        onValueChange = { viewModel.updateCountDownQuery(it) },
                        placeholder = { Text(text = "Enter a value to start the count down") },
                        label = { Text(text = "Count Down") },
                        singleLine = true,
                        maxLines = 1,
                    )

                    Button(
                        onClick = {
                            Timber.d("Start Count Down clicked")

                            viewModel.countDownQuery.isNotBlank().let {
                                if (it) {
                                    viewModel.startAlert(activity, viewModel.countDownQuery)
                                } else {
                                    UIManager.showCustomToast(
                                        activity,
                                        ToastTypeEnum.WARNING,
                                        "Field cannot be empty. Please enter a valid number"
                                    )
                                }
                            }

                        },
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
                            Text(text = "Time remaining ${viewModel.uiCountDown} s")
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(viewModel.isCountDownStarted) {
        Timber.d("LaunchedEffect(viewModel.isCountDownStarted) with: ${viewModel.isCountDownStarted}")

        // Should update loading view
        viewModel.updateLoadingViewVisible(viewModel.isCountDownStarted)

        if (viewModel.isCountDownStarted) {
            focusManager.clearFocus(true)
            keyboardController?.hide()

            val millsFuture = (viewModel.uiCountDown * 1000).toLong()

            scope.launch {
                object : CountDownTimer(millsFuture, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        viewModel.updateUiContDown(millisUntilFinished / 1000)
                    }

                    override fun onFinish() {
                        Timber.e("Count down finished")
                        viewModel.updateCountDownDone(true)
                        viewModel.updateUIState(ScheduleJobAlarmUiState.Done)
                    }
                }.start()
            }
        }
    }

    LaunchedEffect(viewModel.isCountDownDone) {
        Timber.d("LaunchedEffect(viewModel.isCountDownDone) with: ${viewModel.isCountDownDone}")

        viewModel.updateLoadingViewVisible(viewModel.isCountDownDone)

        if (viewModel.isCountDownDone) {
            viewModel.updateCountDownStarted(false)
            // viewModel.updateUIState(ScheduleJobAlarmUiState.Done)
            focusManager.clearFocus(true)
            keyboardController?.hide()
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewScheduleContent() {
    val viewModel: ScheduleViewModel = hiltViewModel()

    TheLabTheme {
        ScheduleContent(viewModel = viewModel)
    }
}