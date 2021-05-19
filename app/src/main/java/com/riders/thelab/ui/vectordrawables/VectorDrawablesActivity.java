package com.riders.thelab.ui.vectordrawables;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.bean.SnackBarType;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class VectorDrawablesActivity extends AppCompatActivity {

    @BindView(R.id.iv_avd_google_io16)
    ShapeableImageView ivGoogleIO16;

    @BindView(R.id.iv_avd_progress_bar)
    ShapeableImageView ivProgressBar;

    AnimatedVectorDrawable googleIOAnimatedVectorDrawable;
    AnimatedVectorDrawable progressBarAnimatedVectorDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LabCompatibilityManager.isLollipop()) {
            UIManager.showActionInSnackBar(
                    this,
                    findViewById(android.R.id.content),
                    "Device running API blelow API 21 (Lollipop), canoont launch this activity",
                    SnackBarType.ALERT,
                    "LEAVE",
                    v -> finish());
        } else {
            setContentView(R.layout.activity_vector_drawables);

            ButterKnife.bind(this);
            getSupportActionBar().setTitle(getString(R.string.activity_title_vector_drawables));
        }
    }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

        if (!LabCompatibilityManager.isLollipop()) {
            UIManager.showActionInSnackBar(
                    this,
                    findViewById(android.R.id.content),
                    "Device running API blelow API 21 (Lollipop), canoont launch this activity",
                    SnackBarType.ALERT,
                    "LEAVE",
                    v -> finish());
            /*new TheLabToast.Builder(this)
                    .setType(ToastTypeEnum.ERROR)
                    .setText("Your device's API cannot run this animation. Requires target API 21 (Lollipop)")
                    .show();*/
            return;
        }

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
