package com.riders.thelab.navigator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

    public void callYoutubeDetailActivity(Intent intent) {
        context.startActivity(intent);
    }

    public void callYoutubeDetailActivityWithTransition(Intent intent, Bundle optionsCompat) {
        context.startActivity(intent, optionsCompat);
    }


    public void callIntentActivity(Class<? extends Activity> targetClass) {
        context.startActivity(new Intent(context, targetClass));
    }

    public void callIntentForPackageActivity(String intentPackageName) {
        context.startActivity(
                context
                        .getPackageManager()
                        .getLaunchIntentForPackage(intentPackageName));
    }

}
