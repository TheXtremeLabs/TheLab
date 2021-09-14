package com.riders.thelab.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.riders.thelab.R
import com.riders.thelab.utils.Constants
import timber.log.Timber

class LabNotificationManager private constructor() {

    companion object {
        fun createNotificationChannel(
            context: Context,
            notificationName: String,
            notificationDescription: String,
            notificationImportance: Int,
            notificationChannelId: String
        ) {
            Timber.d("createNotificationChannel()")

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val channel =
                    NotificationChannel(
                        notificationChannelId,
                        notificationName,
                        notificationImportance
                    ).apply {
                        description = notificationDescription
                    }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun buildMainNotification(
            context: Context,
            mPendingIntent: PendingIntent
        ): NotificationCompat.Builder {
            return NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .apply {
                    setSmallIcon(R.mipmap.ic_lab_six_round)
                    setContentTitle(context.getString(R.string.notification_title))
                    setContentText(context.getString(R.string.notification_content_text))
                    priority = NotificationCompat.PRIORITY_HIGH
                    // Set the intent that will fire when the user taps the notification
                    setContentIntent(mPendingIntent)
                    setChannelId(Constants.NOTIFICATION_CHANNEL_ID)
                    setStyle(
                        NotificationCompat
                            .BigTextStyle()
                            .bigText(context.getString(R.string.notification_big_text))
                    )
                    setAutoCancel(false)
                    setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                }
        }
    }
}