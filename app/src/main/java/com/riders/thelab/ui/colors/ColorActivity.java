package com.riders.thelab.ui.colors;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    String[] colors;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_colors));

        String baseColor = "rgb(33,214,9)";
        String baseColor2 = "rgb(255,156,0)";
        String baseColor3 = "rgb(255,0,0)";

        colors = new String[]{baseColor, baseColor2, baseColor3};
    }

    @OnClick(R.id.change_color_button)
    public void changeColor(View view) {

        Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(colors[new Random().nextInt(colors.length)]);

        if (m.matches()) {

            int red = Integer.parseInt(m.group(1).trim());
            int green = Integer.parseInt(m.group(2).trim());
            int blue = Integer.parseInt(m.group(3).trim());


            Timber.e("int value of red : " + red + ", green : " + green + " blue : " + blue);

//            String hex = "0x" + Integer.toHexString(Color.rgb(red, green, blue)).toUpperCase().substring(2);

            int R = (red >> 16) & 0xff;
            int G = (green >> 8) & 0xff;
            int B = (blue) & 0xff;

            Timber.e("hex value of R : " + R + ", G : " + G + " B : " + B);

            int color = (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);

            Timber.e("value of color variable : " + color);

            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{color, 0x00000000});
            gd.setCornerRadius(0f);

            targetColorTextView.setTextColor(Color.rgb(red, green, blue));
        }
    }
}
