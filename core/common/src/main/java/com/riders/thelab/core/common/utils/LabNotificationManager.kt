package com.riders.thelab.core.common.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver

import timber.log.Timber

object LabNotificationManager {

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
        pendingIntent: PendingIntent,
        @DrawableRes smallIcon: Int,
        @StringRes contentTitle: Int,
        @StringRes contentText: Int,
        @StringRes bigText: Int,
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .apply {
                Timber.d("buildMainNotification()")
                setSmallIcon(smallIcon)
                setContentTitle(context.getString(contentTitle))
                setContentText(context.getString(contentText))
                priority = NotificationCompat.PRIORITY_HIGH
                // Set the intent that will fire when the user taps the notification
                setContentIntent(pendingIntent)
                setChannelId(Constants.NOTIFICATION_CHANNEL_ID)
                setStyle(
                    NotificationCompat
                        .BigTextStyle()
                        .bigText(context.getString(bigText))
                )
                setAutoCancel(false)
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            }


    @SuppressLint("MissingPermission", "ForegroundServiceType")
    fun <T : Service> displayMusicNotification(
        context: Context,
        mediaSession: MediaSessionCompat,
        controller: MediaControllerCompat,
        mServiceMusic: T,
        @DrawableRes smallIcon: Int,
        contentTitle: String,
        contentText: String,
        @DrawableRes largeIcon: Int,
        @DrawableRes actionIcon: Int,
        @StringRes actionTitle: Int
    ) {
        Timber.d("displayMusicNotification()")

        val mediaMetadata = controller.metadata
        val description = mediaMetadata?.description

        val notification = NotificationCompat.Builder(
            context,
            Constants.NOTIFICATION_MUSIC_CHANNEL_ID
        ).apply {

            // Add the metadata for the currently playing track
            setContentTitle(description?.title)
            setContentText(description?.subtitle)
            setSubText(description?.description)
            setLargeIcon(description?.iconBitmap)

            // Enable launching the player by clicking the notification
            setContentIntent(controller.sessionActivity)

            // Stop the service when the notification is swiped away
            setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_STOP
                )
            )

            // Show controls on lock screen even when user hides sensitive content.
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            // Add an app icon and set its accent color
            // Be careful about the color
            setSmallIcon(smallIcon)

            // Add a pause button
            addAction(
                NotificationCompat.Action(
                    actionIcon,
                    context.getString(actionTitle),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                    )
                )
            )


            // Add media control buttons that invoke intents in your media service
            //.addAction(R.drawable.ic_previous, "Previous", prevPendingIntent) // #0
            //.addAction(R.drawable.ic_pause, "Pause", pausePendingIntent) // #1
            //.addAction(R.drawable.ic_next, "Next", nextPendingIntent) // #2
            // Apply the media style template
            setStyle(
                // Take advantage of MediaStyle features
                androidx.media.app.NotificationCompat.MediaStyle()
                    //  #1: pause button
                    .setShowActionsInCompactView(0)
                    .setMediaSession(mediaSession.sessionToken)

                    // Add a cancel button
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            context,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
            )
            setContentTitle(contentTitle)
            setContentText(contentText)
            setLargeIcon(largeIcon.toBitmap(context))

            setSilent(true)
        }

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(Constants.NOTIFICATION_MUSIC_ID, notification.build())
            // Provide a unique integer for the "notificationId" of each notification.
            mServiceMusic.startForeground(Constants.NOTIFICATION_MUSIC_ID, notification.build())
        }
    }
}
