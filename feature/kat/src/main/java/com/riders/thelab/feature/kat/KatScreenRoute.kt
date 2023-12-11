package com.riders.thelab.feature.kat

import androidx.annotation.StringRes

sealed class KatScreenRoute(val route: String, @StringRes val resourceId: Int) {
    data object Chat : KatScreenRoute("chat", R.string.title_chat)
    data object Profile : KatScreenRoute("profile", R.string.title_profile)
}
