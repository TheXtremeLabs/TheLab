package com.riders.thelab.ui.locationonmaps;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.riders.thelab.ui.base.BaseView;

public interface LocationOnMapsContract {

    interface View extends BaseView {
        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

        void onPause();

        void onResume();
    }

    interface Presenter {
        void getDirections();
    }
}
