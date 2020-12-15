package com.riders.thelab.ui.palette;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.ui.base.BaseActivity;

public class PaletteActivity extends BaseActivity<PaletteView> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (LabCompatibilityManager.isTablet(this))
            //Force screen orientation to Landscape
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_palette);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}
