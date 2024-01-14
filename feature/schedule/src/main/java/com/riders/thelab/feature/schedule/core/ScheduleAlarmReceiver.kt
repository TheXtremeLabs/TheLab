package com.riders.thelab.feature.schedule.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.feature.schedule.bus.AlarmEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
class ScheduleAlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {
        Timber.d("MyBroadcastReceiver - onReceive()")

        // Post event with cll info object set
        GlobalScope.launch(Main) {
            AlarmEvent().triggerEvent()
        }

        // Show toast when broadcast receive intent
        UIManager.showToast(context, "Alarm...")
    }

    companion object {
        const val REQUEST_CODE = 1889310
    }
}