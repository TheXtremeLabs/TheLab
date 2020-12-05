package com.riders.thelab.core.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;


/**
 * Created by michael on 07/03/2016.
 */
// TODO : Check this class
public class NotificationBuilderReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationBuilderReceiver.class.getSimpleName();
    private static final String TAG_INFO = "BuilderReceiver";

    public NotificationCompat.Builder mBuilder;
    public NotificationManager mNotifyMgr;
    public static final int NOTIFICATION_ID = 001;

    CharSequence NotificationTitle = "Material Design Features app is here!";
    CharSequence NotificationContent = "Click here to try it :D";

    @Override
    public void onReceive(Context context, Intent intent) {

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
