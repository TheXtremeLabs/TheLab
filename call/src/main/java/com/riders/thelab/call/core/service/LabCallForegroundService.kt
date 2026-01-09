package com.riders.thelab.call.core.service

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import timber.log.Timber

class LabCallForegroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (LabCompatibilityManager.isUpsideDownCake()) {
            startForeground(
                NOTIFICATION_ID,
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
            )
        } else {
            startForeground(NOTIFICATION_ID, createNotification())
        }

        Timber.d("onStartCommand()")

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        Timber.d("createNotification()")

        createNotificationChannel(this)

        return NotificationCompat.Builder(this, "call_channel")
            .setContentTitle("Call in progress")
            .setSmallIcon(R.drawable.ic_menu_call)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel(context: Context) {
        if (LabCompatibilityManager.isOreo()) {
            Timber.i("createNotificationChannel() | API Oreo + | Creating notification channel")

            val channel = NotificationChannel(
                "call_channel",
                "Call Service",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Active call"
                setSound(null, null)
                enableVibration(false)
            }

            val manager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1
    }
}