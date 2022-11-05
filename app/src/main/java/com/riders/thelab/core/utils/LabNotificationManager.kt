package com.riders.thelab.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver
import com.riders.thelab.R
import com.riders.thelab.core.service.MusicMediaPlaybackService
import com.riders.thelab.data.local.model.music.SongModel
import com.riders.thelab.utils.Constants
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
        mPendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        Timber.d("buildMainNotification()")
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

    fun displayMusicNotification(
        context: Context,
        mediaSession: MediaSessionCompat,
        controller: MediaControllerCompat,
        mServiceMusic: MusicMediaPlaybackService,
        songModel: SongModel
    ) {
        Timber.d("displayMusicNotification()")

        val mediaMetadata = controller.metadata
        val description = mediaMetadata?.description

        val notification =
            NotificationCompat.Builder(context, Constants.NOTIFICATION_MUSIC_CHANNEL_ID).apply {

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
                setSmallIcon(R.drawable.ic_music)

                // Add a pause button
                addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_pause,
                        context.getString(R.string.action_pause),
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
                        .setShowActionsInCompactView(0 /* #1: pause button */)
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
                setContentTitle(songModel.name)
                setContentText(songModel.path)
                setLargeIcon(
                    UIManager.getDrawable(
                        context,
                        R.drawable.logo_colors
                    )!!
                )
            }

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(Constants.NOTIFICATION_MUSIC_ID, notification.build())
            // Provide a unique integer for the "notificationId" of each notification.
            mServiceMusic.startForeground(Constants.NOTIFICATION_MUSIC_ID, notification.build())
        }
    }
}
