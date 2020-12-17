package com.riders.thelab.ui.multipane;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;

public class MultipaneActivity extends BaseActivity<MultipaneView> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_multi_pane);
//        view.onCreate();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        view.onPause();
        super.onPause();
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
