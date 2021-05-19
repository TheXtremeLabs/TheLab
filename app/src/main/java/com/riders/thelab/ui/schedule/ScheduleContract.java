package com.riders.thelab.ui.schedule;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface ScheduleContract {

    interface View extends BaseView {

        void onStart();

        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

        void onResume();

        void onStop();

        void showCountDownView();

        void hideCountDownView();

        void updateContDownUI(long millisUntilFinished);
    }

    interface Presenter {

        void startAlert(String countDownTime);

        void startCountDown(int countDown);
    }
}
