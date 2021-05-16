package com.riders.thelab.ui.spring;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.dynamicanimation.animation.DynamicAnimation.X;
import static androidx.dynamicanimation.animation.DynamicAnimation.Y;

@SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
public class SpringActivity extends SimpleActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.imageView5)
    ImageView imageView5;

    private SpringAnimation xAnimation;
    private SpringAnimation yAnimation;

    private float dX;
    private float dY;


    private final ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = () -> {
        xAnimation = createSpringAnimation(imageView, X, imageView.getX(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        yAnimation = createSpringAnimation(imageView, Y, imageView.getY(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
    };


    private final View.OnTouchListener touchListener = (v, event) -> {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // capture the difference between view's top left corner and touch point
                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                // cancel animations
                xAnimation.cancel();
                yAnimation.cancel();
                break;
            case MotionEvent.ACTION_MOVE:
                //  a different approach would be to change the view's LayoutParams.
                imageView.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();

                float newX = event.getRawX() + dX;
                float newY = event.getRawY() + dY;

                v
                        .animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start();
                break;
            case MotionEvent.ACTION_UP:
                xAnimation.start();
                yAnimation.start();
                break;

            default:
                break;
        }
        return true;
    };


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring);

        ButterKnife.bind(this);

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        chainedSpringAnimation();
    }

    public SpringAnimation createSpringAnimation(View view,
                                                 DynamicAnimation.ViewProperty property,
                                                 float finalPosition,
                                                 float stiffness,
                                                 float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new SpringForce(finalPosition);
        springForce.setStiffness(stiffness);
        springForce.setDampingRatio(dampingRatio);
        animation.setSpring(springForce);
        return animation;
    }


    private void chainedSpringAnimation() {

        final SpringAnimation xAnimation2 = createSpringAnimation(imageView2, X, imageView2.getX(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation yAnimation2 = createSpringAnimation(imageView2, Y, imageView2.getY(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation xAnimation3 = createSpringAnimation(imageView3, X, imageView3.getX(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation yAnimation3 = createSpringAnimation(imageView3, Y, imageView3.getY(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation xAnimation4 = createSpringAnimation(imageView4, X, imageView4.getX(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation yAnimation4 = createSpringAnimation(imageView4, Y, imageView4.getY(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation xAnimation5 = createSpringAnimation(imageView5, X, imageView5.getX(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        final SpringAnimation yAnimation5 = createSpringAnimation(imageView5, Y, imageView5.getY(),
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);

        final ViewGroup.MarginLayoutParams imageView2Params = (ViewGroup.MarginLayoutParams) imageView2.getLayoutParams();
        final ViewGroup.MarginLayoutParams imageView3Params = (ViewGroup.MarginLayoutParams) imageView3.getLayoutParams();
        final ViewGroup.MarginLayoutParams imageView4Params = (ViewGroup.MarginLayoutParams) imageView4.getLayoutParams();
        final ViewGroup.MarginLayoutParams imageView5Params = (ViewGroup.MarginLayoutParams) imageView5.getLayoutParams();


        xAnimation2.addUpdateListener((dynamicAnimation, v, v1) ->
                xAnimation3.animateToFinalPosition(
                        v + ((imageView2.getWidth() - imageView3.getWidth()) / 2)
                )
        );


        yAnimation2.addUpdateListener((dynamicAnimation, v, v1) ->
                yAnimation3.animateToFinalPosition(v + imageView2.getHeight() + imageView3Params.topMargin)
        );


        xAnimation3.addUpdateListener((dynamicAnimation, v, v1) ->
                xAnimation4.animateToFinalPosition(
                        v + ((imageView3.getWidth() - imageView4.getWidth()) / 2)
                )
        );


        yAnimation3.addUpdateListener((dynamicAnimation, v, v1) ->
                yAnimation4.animateToFinalPosition(v + imageView3.getHeight() + imageView4Params.topMargin)
        );


        xAnimation4.addUpdateListener((dynamicAnimation, v, v1) ->
                xAnimation5.animateToFinalPosition(
                        v + ((imageView4.getWidth() - imageView5.getWidth()) / 2)
                )
        );

        yAnimation4.addUpdateListener((dynamicAnimation, v, v1) ->
                yAnimation5.animateToFinalPosition(v + imageView4.getHeight() + imageView5Params.topMargin)
        );


        imageView.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - motionEvent.getRawX();
                    dY = view.getY() - motionEvent.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float newX = motionEvent.getRawX() + dX;
                    float newY = motionEvent.getRawY() + dY;

                    view
                            .animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start();
                    xAnimation2.animateToFinalPosition(
                            newX + ((imageView.getWidth() - imageView2.getWidth()) / 2));
                    yAnimation2.animateToFinalPosition(
                            newY + imageView2.getHeight() + imageView2Params.topMargin);
                    break;

            }
            return true;
        });
    }
}
