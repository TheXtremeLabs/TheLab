package com.riders.thelab.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class BaseFragment<V extends BaseView> extends DaggerFragment {

    @Inject
    public V view;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view.onActivityCreated();
    }

    @Override
    public void onDetach() {
        view.onDetach();
        super.onDetach();
    }
}
