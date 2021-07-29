package com.riders.thelab.core.views.toast;

import com.riders.thelab.R;

public enum ToastTypeEnum {

    SUCCESS(R.drawable.ic_check_circle, R.color.success),
    WARNING(R.drawable.ic_round_warning, R.color.warning),
    ERROR(R.drawable.ic_error, R.color.error);

    final int drawable;
    final int color;

    ToastTypeEnum(int drawable, int color) {
        this.drawable = drawable;
        this.color = color;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getColor() {
        return color;
    }
}
