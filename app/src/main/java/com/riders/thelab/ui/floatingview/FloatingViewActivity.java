package com.riders.thelab.ui.floatingview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.riders.thelab.R;
import com.riders.thelab.core.service.FloatingViewService;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class FloatingViewActivity extends SimpleActivity {

    @BindView(R.id.notify_me)
    MaterialButton btnNotifyMe;

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_view);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_floating_view));


        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API < 23. But for API > 23
        //you have to ask for the permission in runtime.
        if (LabCompatibilityManager.isMarshmallow()
                && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {
        Timber.d("initializeView()");
        findViewById(R.id.notify_me).setOnClickListener(view -> {
            startService(new Intent(FloatingViewActivity.this, FloatingViewService.class));
            moveAppInBackground();
        });
    }

    @OnClick(R.id.notify_me)
    public void onNotifyMeClicked() {
        Timber.d("onNotifyMeClicked()");
        startService(new Intent(FloatingViewActivity.this, FloatingViewService.class));
        moveAppInBackground();
    }

    /**
     * Move the app in background
     *
     * Source : https://stackoverflow.com/questions/10461095/moving-application-in-background-on-back-button-event/10461254
     */
    private void moveAppInBackground() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult()");
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Timber.e("onActivityResult() - Permission is not available");
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
