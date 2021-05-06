package com.riders.thelab.data.local.bean;

import com.riders.thelab.R;

/**
 * Created by Michaël on 07/03/2017.
 */

public enum SnackBarType {
    NORMAL(R.color.contactsDatabaseColorPrimaryDark, R.color.white),
    WARNING(R.color.warning, R.color.white),
    ALERT(R.color.error, R.color.white);

    private final int backgroundColor;
    private final int textColor;

    SnackBarType(int backgroundColor, int textColor) {

        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
}
