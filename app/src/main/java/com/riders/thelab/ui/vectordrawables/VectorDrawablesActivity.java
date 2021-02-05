package com.riders.thelab.ui.vectordrawables;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.riders.thelab.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class VectorDrawablesActivity extends AppCompatActivity {

    @BindView(R.id.iv_avd_google_io16)
    ImageView ivGoogleIO16;

    @BindView(R.id.iv_avd_progress_bar)
    ImageView ivProgressBar;

    AnimatedVectorDrawable googleIOAnimatedVectorDrawable;
    AnimatedVectorDrawable progressBarAnimatedVectorDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_drawables);

        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Drawable drawableGoogleIO16 = ivGoogleIO16.getDrawable();
        Drawable drawableProgressBar = ivProgressBar.getDrawable();

        if (drawableGoogleIO16 instanceof AnimatedVectorDrawable) {
            // AVD GoogleIO 16'
            googleIOAnimatedVectorDrawable = (AnimatedVectorDrawable) drawableGoogleIO16;
            googleIOAnimatedVectorDrawable.start();
        }

        if (drawableProgressBar instanceof AnimatedVectorDrawable) {
            // AVD Progress Bar
            progressBarAnimatedVectorDrawable = (AnimatedVectorDrawable) drawableProgressBar;
            progressBarAnimatedVectorDrawable.start();
        }
    }
}
