package com.riders.thelab.ui.mainactivity;

import android.view.View;

import com.riders.thelab.data.local.model.App;

public interface MainActivityAppClickListener {
    void onAppItemCLickListener(View view, App item, int position);
}
