package com.riders.thelab.ui.colors;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabAnimationsManager;
import com.riders.thelab.core.utils.LabColorsManager;
import com.riders.thelab.ui.base.SimpleActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ColorActivity extends SimpleActivity {

    // Views
    @BindView(R.id.target_color_textView)
    MaterialTextView targetColorTextView;
    @BindView(R.id.change_color_button)
    MaterialButton changeColorButton;

    int[] colors;

    int fromColor;
    int toColor;

    int randomColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_colors));

        colors = LabColorsManager.getDefaultColors(this);
    }

    @OnClick(R.id.change_color_button)
    public void changeColor(View view) {

        // Get a random color
        randomColor = colors[new Random().nextInt(colors.length)];

        // Set as target color to use in animation
        toColor = randomColor;
        // Get current TextView color
        fromColor = targetColorTextView.getCurrentTextColor();
        // Apply fade color transition to TextView
        LabAnimationsManager.applyFadeColorAnimationToView(
                targetColorTextView,
                fromColor,
                toColor,
                LabAnimationsManager
                        .getInstance()
                        .getShortAnimationDuration());

        // Get current button color
        fromColor = changeColorButton.getCurrentTextColor();
        // Apply fade color transition to Button
        LabAnimationsManager.applyFadeColorAnimationToView(
                changeColorButton,
                fromColor,
                toColor,
                LabAnimationsManager
                        .getInstance()
                        .getShortAnimationDuration());
    }
}
