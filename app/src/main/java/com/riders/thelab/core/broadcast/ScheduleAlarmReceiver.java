package com.riders.thelab.core.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.riders.thelab.core.bus.AlarmEvent;
import com.riders.thelab.core.utils.UIManager;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

public class ScheduleAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 1889310;

    @Override
    public void onReceive(Context context, Intent intent) {

        Timber.d("MyBroadcastReceiver - onReceive()");

        // Post event with cll info object set
        EventBus.getDefault().post(new AlarmEvent());

        // Show toast when broadcast receive intent
        UIManager.showActionInToast(context, "Alarm...");
    }
}