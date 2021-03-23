package com.riders.thelab.ui.youtubelike;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseActivity;

public class YoutubeLikeActivity extends BaseActivity<YoutubeLikeView> {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Window w = getWindow();
        w.setAllowEnterTransitionOverlap(true);
        setContentView(R.layout.activity_youtube);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        view.onStart();
    }

    @Override
    protected void onStop() {
        view.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
