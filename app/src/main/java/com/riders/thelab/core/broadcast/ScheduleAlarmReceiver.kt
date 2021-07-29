package com.riders.thelab.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riders.thelab.core.bus.AlarmEvent
import com.riders.thelab.core.utils.UIManager.Companion.showActionInToast
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class ScheduleAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val REQUEST_CODE = 1889310
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("MyBroadcastReceiver - onReceive()")

        // Post event with cll info object set
        EventBus.getDefault().post(AlarmEvent())

        // Show toast when broadcast receive intent
        showActionInToast(context, "Alarm...")
    }
}