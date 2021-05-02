package com.riders.thelab.ui.schedule;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.broadcast.ScheduleAlarmReceiver;
import com.riders.thelab.core.bus.AlarmEvent;
import com.riders.thelab.ui.base.BaseViewImpl;
import com.riders.thelab.utils.Validator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class ScheduleView extends BaseViewImpl<SchedulePresenter>
        implements ScheduleContract.View {

    @BindView(R.id.progressBar_loading_horizontal)
    ProgressBar mProgressBar;
    @BindView(R.id.time)
    EditText mEditText;
    @BindView(R.id.button)
    MaterialButton start;
    @BindView(R.id.ll_delay_time_container)
    LinearLayout llDelayTimeContainer;
    @BindView(R.id.tv_delay_time)
    MaterialTextView tvDelayTime;
    // TAG & Context
    private ScheduleActivity context;
    private PendingIntent mPendingIntent;
    private ServiceConnection mServiceConnection;
    private ScheduleAlarmReceiver mAlarmBroadcast;
    private AlarmManager mAlarmManager;


    @Inject
    ScheduleView(ScheduleActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        //setListeners();

        mAlarmBroadcast = new ScheduleAlarmReceiver();
    }


    @Override
    public void onStart() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (ScheduleAlarmReceiver.REQUEST_CODE == resultCode) {
            Timber.e("result code caught !!!!");

            hideCountDownView();
        }
    }

    @Override
    public void onResume() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        context.registerReceiver(mAlarmBroadcast, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);

        context.unregisterReceiver(mAlarmBroadcast);
    }

    @Override
    public void showCountDownView() {
        llDelayTimeContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCountDownView() {
        llDelayTimeContainer.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updateContDownUI(long millisUntilFinished) {

        context.runOnUiThread(() -> tvDelayTime.setText("" + millisUntilFinished));
    }

    @Override
    public void onDestroy() {
        if (null != mServiceConnection)
            context.unbindService(mServiceConnection);

        if (mPendingIntent != null && mAlarmManager != null) {
            mAlarmManager.cancel(mPendingIntent);
        }

        getPresenter().detachView();
        context = null;
    }


    ////////////////////////////
    //
    // BUTTERKNIFE
    //
    ////////////////////////////
    @OnClick(R.id.button)
    void onStartAlarmButtonClicked() {

        Timber.d("onStartAlarmButtonClicked()");

        String text = mEditText.getText().toString();

        Timber.d("Check if field is empty");
        // Check if field is empty
        if (Validator.isEmpty(text)) {
            Timber.e("Field is empty, display error");
            mEditText.setError("Le champ ne peut être vide, entrez un chiffre ou un nombre");
        }
        // Is not empty
        else {
            Timber.d("Field is not empty");
            // Check if is numeric
            if (!Validator.isNumeric(text)) {
                Timber.e("The string is not a numeric sequence, display not numeric error message");
                mEditText.setError("Les lettres ne sont pas autorisées\nVeuillez enter un chiffre/nombre");
            }
            // Is numeric
            else {
                Timber.d("The string is a numeric sequence. Start Alarm");
                getPresenter().startAlert(text);
            }
        }
    }


    ////////////////////////////
    //
    // EVENT BUS
    //
    ////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlarmEventCaught(AlarmEvent event) {
        Timber.e("onAlarmEventCaught()");
        Timber.d("Count down finished event");

        hideCountDownView();
    }
}

