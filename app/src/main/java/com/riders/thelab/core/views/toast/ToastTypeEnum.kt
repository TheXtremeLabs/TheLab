package com.riders.thelab.core.views.toast

import com.riders.thelab.R

enum class ToastTypeEnum(val drawable: Int, val color: Int) {

    SUCCESS(R.drawable.ic_check_circle, R.color.success),

    WARNING(R.drawable.ic_round_warning, R.color.warning),

    ERROR(R.drawable.ic_error, R.color.error);
}