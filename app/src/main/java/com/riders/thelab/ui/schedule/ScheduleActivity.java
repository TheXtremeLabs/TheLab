package com.riders.thelab.ui.schedule;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;

public class ScheduleActivity extends BaseActivity<ScheduleView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_schedule);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        view.onStart();
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        view.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        view.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
