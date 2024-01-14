package com.riders.thelab.core.data.local.model.app

import android.graphics.drawable.Drawable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.RawValue

data class PackageApp(
    var name: String,
    var drawableIcon: @RawValue Drawable?,
    var version: String,
    var packageName: String
) : App(
    appName = name,
    appDrawableIcon = drawableIcon,
    appVersion = version,
    appPackageName = packageName,
    id = -1,
    appTitle = null,
    appDescription = null,
    appActivity = null,
    appDate = null
) {

    // From Packages
    @IgnoredOnParcel
    var paName: String? = null

    @IgnoredOnParcel
    var paDrawableIcon: Drawable? = null

    @IgnoredOnParcel
    var paVersion: String? = null

    @IgnoredOnParcel
    var paPackageName: String? = null
}
