package com.riders.thelab.navigator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.riders.thelab.ui.contacts.addcontact.AddContactActivity;
import com.riders.thelab.ui.mainactivity.MainActivity;
import com.riders.thelab.ui.splashscreen.SplashScreenActivity;

import javax.inject.Inject;

public class Navigator {

    private final Activity context;


    @Inject
    public Navigator(Activity context) {
        this.context = context;
    }


    /* Activities */
    public void callSplashActivity() {
        context.startActivity(new Intent(context, SplashScreenActivity.class));
    }

    public void callMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    /* Detail Activities */
    public void callContactDetailActivity(Intent intent) {
        context.startActivity(intent);
    }

    public void callAddContactActivity() {
        context.startActivity(new Intent(context, AddContactActivity.class));
    }

    public void callMultipaneDetailActivity(Intent intent) {
        context.startActivity(intent);
    }

    public void callYoutubeDetailActivity(Intent intent) {
        context.startActivity(intent);
    }

    public void callYoutubeDetailActivityWithTransition(Intent intent, Bundle optionsCompat) {
        context.startActivity(intent, optionsCompat);
    }


    /* Activities */
    public void callIntentActivity(Class<? extends Activity> targetClass) {
        context.startActivity(new Intent(context, targetClass));
    }

    /* Packages */
    public void callIntentForPackageActivity(String intentPackageName) {
        context.startActivity(
                context
                        .getPackageManager()
                        .getLaunchIntentForPackage(intentPackageName));
    }

}
