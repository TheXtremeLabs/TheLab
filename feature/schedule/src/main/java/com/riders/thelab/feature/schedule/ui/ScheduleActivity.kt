package com.riders.thelab.feature.schedule.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.bus.KotlinBus
import com.riders.thelab.core.common.bus.Listen
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.schedule.core.ScheduleAlarmReceiver
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

class ScheduleActivity : BaseComponentActivity() {

    private val mViewModel: ScheduleViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    val scope = rememberCoroutineScope()
                    val tooltipState = TooltipState()
                    val scheduleState by mViewModel.scheduleJobUiState.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            ScheduleContent(
                                scheduleState = scheduleState,
                                onUpdateUIState = { mViewModel.updateUIState(it) },
                                tooltipState = tooltipState,
                                onStartButtonClicked = {
                                    Timber.d("Start Count Down clicked")
                                    if (mViewModel.countDownQuery.isNotBlank()) {
                                        mViewModel.startAlert(
                                            this@ScheduleActivity,
                                            mViewModel.countDownQuery
                                        )
                                    } else {
                                        if (!tooltipState.isVisible) {
                                            scope.launch { tooltipState.show() }
                                        }
                                    }
                                },
                                countDownQuery = mViewModel.countDownQuery,
                                onUpdateCountDownQuery = { mViewModel.updateCountDownQuery(it) },
                                uiCountDown = mViewModel.uiCountDown,
                                isCountDownStarted = mViewModel.isCountDownStarted,
                                onUpdateCountDownStarted = { mViewModel.updateCountDownStarted(it) },
                                onUpdateLoadingViewVisible = {
                                    mViewModel.updateLoadingViewVisible(
                                        it
                                    )
                                },
                                onUpdateUiCountDown = { millisUntilFinished: Long ->
                                    mViewModel.updateUiCountDown(
                                        millisUntilFinished
                                    )
                                },
                                isCountDownDone = mViewModel.isCountDownDone,
                                onUpdateCountDownDone = { mViewModel.updateCountDownDone(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (ScheduleAlarmReceiver.REQUEST_CODE == resultCode) {
            Timber.e("result code caught !!!!")
        }
    }

    override fun onPause() {
        try {
            mViewModel.unregisterReceiver(this)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        try {
            mViewModel.registerReceiver(this)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override fun backPressed() {
        finish()
    }

    override fun onDestroy() {
        mViewModel.unbindService(this)
        super.onDestroy()
    }

    ////////////////////////////
    //
    // EVENT BUS
    //
    ////////////////////////////
    @OptIn(DelicateCoroutinesApi::class)
    @Listen
    fun onEventTriggered() {
        Timber.d("onEventTriggered()")
        lifecycleScope.launch {
            KotlinBus.subscribe<String> {
                Timber.d("Received | Count down finished event with, $it")
                mViewModel.updateCountDownDone(true)
            }
        }
    }
}

