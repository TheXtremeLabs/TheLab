package com.riders.thelab.core.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.service.TheLabBootService
import com.riders.thelab.core.ui.utils.UIManager
import timber.log.Timber

class TheLabBootBroadcastReceiver : BroadcastReceiver() {

    @SuppressLint("NewApi")
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("onReceive()")
        if (null != intent) {
            val action = intent.action

            if (null != action) {
                if (action == "android.intent.action.BOOT_COMPLETED") {
                    UIManager.showActionInToast(
                        context,
                        "action == \"android.intent.action.BOOT_COMPLETED\""
                    )
                    val i = Intent(context!!, TheLabBootService::class.java)

                    if (LabCompatibilityManager.isOreo()) {
                        ContextCompat.startForegroundService(context, i)
                    } else {
                        context.startService(i)
                    }
                }
            }
        }
    }
}