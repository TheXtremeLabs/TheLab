package com.riders.ktweather.data.local.bean

import com.riders.thelab.R


enum class SnackBarType(private val backgroundColor: Int, private val textColor: Int) {
    NORMAL(R.color.contactsDatabaseColorPrimaryDark, R.color.white),
    WARNING(R.color.warning, R.color.white),
    ALERT(R.color.error, R.color.white);

    open fun getBackgroundColor(): Int {
        return backgroundColor
    }

    open fun getTextColor(): Int {
        return textColor
    }
}