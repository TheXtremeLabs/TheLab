package com.riders.thelab.feature.kat.data.local.compose

import androidx.annotation.StringRes
import com.riders.thelab.feature.kat.R

sealed class KatScreenRoute(val route: String, @StringRes val resourceId: Int) {
    data object Chat : KatScreenRoute("chat", R.string.title_chat)
    data object Profile : KatScreenRoute("profile", R.string.title_profile)
}
