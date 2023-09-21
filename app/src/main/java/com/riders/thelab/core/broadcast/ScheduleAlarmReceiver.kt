package com.riders.thelab.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riders.thelab.core.bus.AlarmEvent
import com.riders.thelab.core.ui.utils.UIManager.showActionInToast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
class ScheduleAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val REQUEST_CODE = 1889310
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("MyBroadcastReceiver - onReceive()")

        // Post event with cll info object set
        GlobalScope.launch(Main) {
            AlarmEvent().triggerEvent()
        }

        // Show toast when broadcast receive intent
        showActionInToast(context, "Alarm...")
    }
}