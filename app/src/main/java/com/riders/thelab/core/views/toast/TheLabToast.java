package com.riders.thelab.core.views.toast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;

import timber.log.Timber;


/**
 * Reference : https://developer.android.com/guide/topics/ui/notifiers/toasts#CustomToastView
 */

public class TheLabToast extends Toast {

    private final Context context;

    private final LinearLayout container;
    private final ShapeableImageView imageView;
    private MaterialTextView textView;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public TheLabToast(Context context) {
        super(context);

        this.context = context;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(
                R.layout.custom_toast_layout,
                (ViewGroup) ((Activity) context).findViewById(R.id.custom_toast_container));

        container = (LinearLayout) layout.findViewById(R.id.custom_toast_container);
        imageView =  layout.findViewById(R.id.ivLol);
        textView =  layout.findViewById(R.id.text);

        // Ref : https://developer.android.com/reference/android/widget/Toast#setGravity(int,%20int,%20int)
        this.setGravity(Gravity.BOTTOM, 0, 250);
        this.setDuration(Toast.LENGTH_LONG);
        this.setView(layout);
    }

    @Override
    public void setText(CharSequence s) {
        textView.setText(s);
    }

    public void setType(ToastTypeEnum toastTypeEnum) {
        Timber.e("type : %s", toastTypeEnum.toString());

        setImageResource(toastTypeEnum.getDrawable());
        setBackgroundColor(toastTypeEnum.getColor());
    }

    public void setImageResource(int drawableResourceID) {
        imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableResourceID));
    }

    /**
     * Reference : https://stackoverflow.com/questions/47837460/how-to-set-layout-background-tint-from-string-programmatically
     * @param color
     */
    public void setBackgroundColor(int color) {
        container.getBackground().setColorFilter(
                ContextCompat.getColor(context, color),
                PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void show() {
        Timber.d("show()");

        TranslateAnimation translateAnimation =
                new TranslateAnimation(0, 0, 180, container.getTop());

        translateAnimation.setDuration(800);
        translateAnimation.setInterpolator(new LinearOutSlowInInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Timber.d("onAnimationStart()");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Timber.e("onAnimationEnd()");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);

        container.startAnimation(animationSet);
        super.show();
    }

    @Override
    public void cancel() {
        Timber.e("cancel()");
        super.cancel();
    }


    public static class Builder {

        Context context;
        String text;
        ToastTypeEnum type;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setType(ToastTypeEnum type) {
            this.type = type;
            return this;
        }

        public void show() {
            TheLabToast toast = new TheLabToast(context);

            toast.setText(text);
            toast.setType(type);

            toast.show();
        }
    }
}
