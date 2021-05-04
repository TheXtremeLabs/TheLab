package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;


@SuppressLint("NonConstantResourceId")
public class RecyclerViewActivity extends BaseActivity<RecyclerViewView> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_recycler_view);
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
