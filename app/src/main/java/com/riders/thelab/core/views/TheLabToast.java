package com.riders.thelab.core.views;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.riders.thelab.R;

import timber.log.Timber;

public class TheLabToast extends Toast {

    private final Context context;

    private LayoutInflater inflater;
    private int resourceId;
    private final View layout;
    private final LinearLayout container;
    private final ImageView imageView;

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

        inflater = ((Activity) context).getLayoutInflater();
        layout = inflater.inflate(
                R.layout.custom_toast_layout,
                (ViewGroup) ((Activity) context).findViewById(R.id.custom_toast_container));


        container = (LinearLayout) layout.findViewById(R.id.custom_toast_container);
        imageView = (ImageView) layout.findViewById(R.id.ivLol);

        this.setGravity(Gravity.BOTTOM, 0, 250);
        this.setDuration(Toast.LENGTH_LONG);
        this.setView(layout);
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

    @Override
    public void setText(CharSequence s) {
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(s);
    }


    public void setType(ToastTypeEnum toastTypeEnum) {
        Timber.e("type : %s", toastTypeEnum.toString());

        switch (toastTypeEnum) {
            case SUCCESS:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.iccheck_circle));
                container.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.success), PorterDuff.Mode.SRC_IN);
                break;

            case WARNING:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_warning));
                container.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.warning), PorterDuff.Mode.SRC_IN);
                break;

            case ERROR:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_error));
                container.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.error), PorterDuff.Mode.SRC_IN);
                break;

            default:
                break;
        }
    }

    public void setBackgroundColor(int color) {
        container.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void setText(int resId) {
        super.setText(resId);
    }

    @Override
    public void show() {
        Timber.d("show()");
        super.show();
    }

    @Override
    public void cancel() {
        Timber.d("cancel()");
        super.cancel();
    }


    public enum ToastTypeEnum {
        SUCCESS,
        WARNING,
        ERROR;
    }
}
