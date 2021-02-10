package com.riders.thelab.ui.colors;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class ColorActivity extends SimpleActivity {

    @BindView(R.id.target_color_textView)
    MaterialTextView targetColorTextView;

    @BindView(R.id.change_color_button)
    MaterialButton changeColorButton;

    int[] colors;

    int fromColor;
    int toColor;

    int randomColor;

    private int shortAnimationDuration;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_colors));


        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


        colors = new int[]{
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.blue),
                ContextCompat.getColor(this, R.color.green),
                ContextCompat.getColor(this, R.color.orange),
                ContextCompat.getColor(this, R.color.purple),
                ContextCompat.getColor(this, R.color.yellow),
                ContextCompat.getColor(this, R.color.teal_700)
        };
    }

    @OnClick(R.id.change_color_button)
    public void changeColor(View view) {

        randomColor = colors[new Random().nextInt(colors.length)];

        Timber.d("color : %d ", randomColor);

        toColor = randomColor;
        fromColor = targetColorTextView.getCurrentTextColor();
        applyColorFade(targetColorTextView, fromColor, toColor);

        fromColor = changeColorButton.getCurrentTextColor();
        applyColorFade(changeColorButton, fromColor, toColor);
    }


    @SuppressLint("RestrictedApi")
    private void applyColorFade(View view, int fromColor, int toColor) {
        ObjectAnimator fadeAnimator = null;

        if (view instanceof MaterialTextView) {
            fadeAnimator = ObjectAnimator.ofObject(
                    view,
                    "textColor",
                    new ArgbEvaluator(),
                    fromColor,
                    toColor
            );
        }

        if (view instanceof MaterialButton) {
            fadeAnimator = ObjectAnimator.ofObject(
                    view,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    fromColor,
                    toColor
            );
        }

        if (null != fadeAnimator) {

            fadeAnimator.setDuration(shortAnimationDuration);
            fadeAnimator.start();
        }
    }
}
