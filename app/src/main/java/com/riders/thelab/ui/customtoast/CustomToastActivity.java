package com.riders.thelab.ui.customtoast;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.riders.thelab.R;
import com.riders.thelab.core.views.toast.TheLabToast;
import com.riders.thelab.core.views.toast.ToastTypeEnum;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class CustomToastActivity extends AppCompatActivity {

    @BindView(R.id.display_toast_btn)
    MaterialButton displayToastButton;

    @BindView(R.id.progressIndicator)
    CircularProgressIndicator circularProgressIndicator;

    @BindView(R.id.button_custom)
    LinearLayout customButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_custom_toast);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_custom_toast));
    }

    @OnClick(R.id.button_custom)
    public void onDisplayToastButtonClicked() {

        circularProgressIndicator.animate()
                .setDuration(1500)
                .alpha(1.0f)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        circularProgressIndicator.setVisibility(View.VISIBLE);
                        circularProgressIndicator.setAlpha(0f);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();

        displayCustomToastUsingClass();

//        displayCustomToastGoogleDeveloperBasicImplementation();
    }

    private void displayCustomToastUsingClass() {

        int random = new Random().nextInt(ToastTypeEnum.values().length);
        Timber.d("random : %s", random);

        new TheLabToast.Builder(this)
                .setText("Testing a new text")
                .setType(ToastTypeEnum.values()[random])
                .show();
    }

    private void displayCustomToastGoogleDeveloperBasicImplementation() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(
                R.layout.custom_toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("This is a custom toast");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }
}
