package com.riders.thelab.core.ui.compose.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.glance.LocalContext


@Composable
fun stringResource(@StringRes id: Int, vararg args: Any): String {
    return LocalContext.current.getString(id, args)
}
