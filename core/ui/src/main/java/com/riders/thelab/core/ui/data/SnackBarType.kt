package com.riders.thelab.core.ui.data

import  com.riders.thelab.core.ui.R

enum class SnackBarType(val backgroundColor: Int, val textColor: Int) {
    NORMAL(R.color.contactsDatabaseColorPrimaryDark, R.color.white),
    WARNING(R.color.warning, R.color.white),
    ALERT(R.color.error, R.color.white);
}