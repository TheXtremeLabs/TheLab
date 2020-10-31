package com.riders.thelab.navigator;

import android.app.Activity;
import android.content.Intent;

import com.riders.thelab.ui.mainactivity.MainActivity;
import com.riders.thelab.ui.splashscreen.SplashScreenActivity;

import javax.inject.Inject;

public class Navigator {

    private final Activity context;


    @Inject
    public Navigator(Activity context) {
        this.context = context;
    }

    public void callSplashActivity() {
        context.startActivity(new Intent(context, SplashScreenActivity.class));
    }

    public void callMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
