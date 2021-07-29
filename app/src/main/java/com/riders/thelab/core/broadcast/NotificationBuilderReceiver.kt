package com.riders.thelab.core.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationBuilderReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1
        private val TAG = NotificationBuilderReceiver::class.java.simpleName
        const val TAG_INFO = "BuilderReceiver"

    }

    var mBuilder: NotificationCompat.Builder? = null
    var mNotifyMgr: NotificationManager? = null
    var NotificationTitle: CharSequence = "Material Design Features app is here!"
    var NotificationContent: CharSequence = "Click here to try it :D"

    override fun onReceive(context: Context?, intent: Intent?) {

        /*Utils.showActionInToast(context, "onReceive notification builder()");
        Log.e(TAG_INFO, "onReceive notification builder()");

        Intent notificationIntent = new Intent( context, SplashActivity.class );
        notificationIntent.addFlags( Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        PendingIntent contentIntent = PendingIntent.getActivity( context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT );

        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon( R.mipmap.ic_launcher )
                    .setContentTitle( NotificationTitle )
                        .setContentText( NotificationContent )
                            .setTicker( NotificationTitle )
                                .setAutoCancel( false )
                                    .addAction( R.drawable.ic_security_black, "Cancel notification", contentIntent );

        mBuilder.setContentIntent( contentIntent );

        mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        mNotifyMgr.notify( NOTIFICATION_ID, mBuilder.build() );*/
    }
}