package com.riders.thelab.ui.customtoast;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
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

    private Context context;

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

        context = this;

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_custom_toast));
    }

    @OnClick(R.id.button_custom)
    public void onDisplayToastButtonClicked() {
        /*int[] colors = {
                ContextCompat.getColor(context, R.color.success),
                ContextCompat.getColor(context, R.color.warning),
                ContextCompat.getColor(context, R.color.error)
        };

        int height = circularProgressIndicator.getHeight();
        Shader shader= new LinearGradient(
                0f, 0f, 0f, height,
                colors,
                null,
                Shader.TileMode.REPEAT);

        Paint paint = new Paint();
        paint.setShader(shader);
        circularProgressIndicator.setLayerPaint(paint);*/

        circularProgressIndicator.animate()
                .setDuration(2000)
                .alpha(1.0f)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        circularProgressIndicator.setVisibility(View.VISIBLE);
                        circularProgressIndicator.setAlpha(0f);

                        circularProgressIndicator.setIndicatorColor(
                                ContextCompat.getColor(context, R.color.success),
                                ContextCompat.getColor(context, R.color.warning),
                                ContextCompat.getColor(context, R.color.error)
                        );
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        new Handler().postDelayed(() -> {

                            circularProgressIndicator.animate()
                                    .setDuration(1500)
                                    .alpha(0.0f)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            circularProgressIndicator.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                        }, 2000);
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

        MaterialTextView text = (MaterialTextView) layout.findViewById(R.id.text);
        text.setText("This is a custom toast");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }
}
