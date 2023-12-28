package com.riders.thelab.ui.schedule

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.riders.thelab.TheLabApplication
import com.riders.thelab.core.broadcast.ScheduleAlarmReceiver
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.compose.ScheduleJobAlarmUiState
import com.riders.thelab.core.service.ScheduleAlarmService
import com.riders.thelab.core.ui.utils.UIManager
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class ScheduleViewModel : ViewModel() {
    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    private var mPendingIntent: PendingIntent? = null
    private var mServiceConnection: ServiceConnection? = null
    private var mAlarmBroadcast: ScheduleAlarmReceiver? = null
    private var mAlarmManager: AlarmManager? = null


    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    private var _scheduleJobUiState =
        MutableStateFlow<ScheduleJobAlarmUiState>(ScheduleJobAlarmUiState.None)
    val scheduleJobUiState = _scheduleJobUiState
    fun updateUIState(state: ScheduleJobAlarmUiState) {
        _scheduleJobUiState.value = state
    }

    var countDownQuery by mutableStateOf("")
        private set
    var isCountDownStarted by mutableStateOf(false)
        private set
    var isCountDownDone by mutableStateOf(false)
        private set
    private var isLoadingViewVisible by mutableStateOf(false)
        private set
    var uiCountDown by mutableStateOf(0L)
        private set

    fun updateCountDownQuery(countDown: String) {
        countDownQuery = countDown
    }

    fun updateCountDownStarted(started: Boolean) {
        isCountDownStarted = started
    }

    fun updateCountDownDone(done: Boolean) {
        isCountDownDone = done
    }

    fun updateLoadingViewVisible(visible: Boolean) {
        isLoadingViewVisible = visible
    }

    fun updateUiContDown(millisUntilFinished: Long) {
        uiCountDown = millisUntilFinished
    }


    //////////////////////////////////////////
    //
    // Class Methods
    //
    //////////////////////////////////////////
    fun startAlert(activity: Activity, countDownTime: String) {
        Timber.d("Time entered : %s second(s)", countDownTime)

        updateCountDownDone(false)
        updateUIState(ScheduleJobAlarmUiState.Started(true, countDownTime.toLong()))
        updateCountDownStarted(true)

        mAlarmBroadcast = ScheduleAlarmReceiver()
        var mAlarmService: ScheduleAlarmService? = null

        val i = (scheduleJobUiState.value as ScheduleJobAlarmUiState.Started).millisUntilFinished

        val mBroadcastIntent = Intent(activity, ScheduleAlarmReceiver::class.java)
        mPendingIntent =
            PendingIntent
                .getBroadcast(
                    TheLabApplication.getInstance().getContext(),
                    ScheduleAlarmReceiver.REQUEST_CODE,
                    mBroadcastIntent,
                    if (LabCompatibilityManager.isMarshmallow()) PendingIntent.FLAG_IMMUTABLE else 0
                )


        mAlarmManager =
            (activity as ScheduleActivity).getSystemService(Context.ALARM_SERVICE) as AlarmManager


        mAlarmManager?.let { alarm ->
            mPendingIntent?.let { pendingIntent ->
                alarm[AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + i * 1000] =
                    pendingIntent
            }
        }

        UIManager.showActionInToast(activity, "Alarm set in $i seconds")

        updateUiContDown(i)

        mServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                Timber.i("Service bounded")
                mAlarmService = ScheduleAlarmService()
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                Timber.e("Service disconnected")
            }
        }
        activity.bindService(
            Intent(activity, ScheduleAlarmService::class.java),
            mServiceConnection as ServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    fun registerReceiver(activity: Activity) {
        activity.registerReceiver(
            mAlarmBroadcast,
            IntentFilter("android.intent.action.AIRPLANE_MODE")
        )
    }

    fun unregisterReceiver(activity: Activity) {
        activity.unregisterReceiver(mAlarmBroadcast)
    }

    fun unbindService(activity: ScheduleActivity) {

        if (null != mServiceConnection)
            activity.unbindService(mServiceConnection!!)

        mAlarmManager?.let { alarm ->
            mPendingIntent?.let { pendingIntent ->
                alarm.cancel(pendingIntent)
            }
        }
    }
}