package com.riders.thelab.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class BaseActivity<V extends BaseView> extends DaggerAppCompatActivity {

    @Inject
    public V view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view.onCreate();
    }



    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
