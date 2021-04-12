package com.riders.thelab.ui.transition;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint({"NonConstantResourceId", "NewApi"})
public class TransitionDetailActivity extends SimpleActivity {

    @BindView(R.id.iv_logo)
    ShapeableImageView ivTarget;
    @BindView(R.id.button_detail)
    MaterialButton button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setAllowEnterTransitionOverlap(true);
        setContentView(R.layout.activity_transition_detail);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(getString(R.string.activity_title_transition_detail));
    }

    @OnClick(R.id.button_detail)
    public void onButtonClicked() {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAfterTransition();
    }
}
