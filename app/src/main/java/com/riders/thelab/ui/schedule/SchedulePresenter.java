package com.riders.thelab.ui.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.riders.thelab.TheLabApplication;
import com.riders.thelab.core.broadcast.ScheduleAlarmReceiver;
import com.riders.thelab.core.service.ScheduleAlarmService;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.BIND_AUTO_CREATE;

public class SchedulePresenter extends BasePresenterImpl<ScheduleView>
        implements ScheduleContract.Presenter {


    @Inject
    ScheduleActivity activity;

    private ScheduleAlarmService mAlarmService;

    @Inject
    SchedulePresenter() {
    }

    @Override
    public void startAlert(String countDownTime) {
        Timber.d("Time entered : %s second(s)", countDownTime);

        int i = Integer.parseInt(countDownTime);

        Intent mBroadcastIntent = new Intent(activity, ScheduleAlarmReceiver.class);
        PendingIntent mPendingIntent = PendingIntent
                .getBroadcast(
                        TheLabApplication.getContext(),
                        ScheduleAlarmReceiver.REQUEST_CODE,
                        mBroadcastIntent,
                        0);

        AlarmManager mAlarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        mAlarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + (i * 1000),
                mPendingIntent);

        UIManager.showActionInToast(activity, "Alarm set in " + i + " seconds");

        startCountDown(i);

        ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Timber.i("Service bounded");

                mAlarmService = new ScheduleAlarmService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Timber.e("Service disconnected");
            }
        };

        activity.bindService(
                new Intent(activity, ScheduleAlarmService.class),
                mServiceConnection,
                BIND_AUTO_CREATE
        );
    }

    @Override
    public void startCountDown(int countDown) {

        getView().showCountDownView();

        long millsFuture = countDown * 1000;

        Completable.complete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> new CountDownTimer(millsFuture, 1000) {

                            public void onTick(long millisUntilFinished) {
                                getView().updateContDownUI(millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                Timber.d("Count down finished");
                            }
                        }.start(),
                        Timber::e);
    }

    public ScheduleAlarmService getmAlarmService() {
        return mAlarmService;
    }

    public void setmAlarmService(ScheduleAlarmService mAlarmService) {
        this.mAlarmService = mAlarmService;
    }
}
