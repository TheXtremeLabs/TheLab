package com.riders.thelab.data.local.model

import android.app.Activity
import android.graphics.drawable.Drawable

data class App constructor(
        var appName: String? = null, var appDrawableIcon: Drawable? = null,
        var appVersion: String? = null, var appPackageName: String? = null,
        var appTitle: String?, var appDescription: String?,
        var appIcon: Int = 0, var appActivity: Class<out Activity?>?
) {

    // From Packages
    private var name: String? = null
    private var drawableIcon: Drawable? = null
    private var version: String? = null
    private var packageName: String? = null

    // From activities
    private var title: String? = null
    private var description: String? = null
    private var icon = 0
    private var activity: Class<out Activity?>? = null

    constructor() : this(
            "", null, "", "",
            "", "", 0, null)

    constructor(name: String, drawableIcon: Drawable, version: String, packageName: String) : this() {
        this.name = name
        this.drawableIcon = drawableIcon
        this.version = version
        this.packageName = packageName
    }

    constructor(title: String, description: String, icon: Int, activity: Class<out Activity>?) : this() {
        this.title = title
        this.description = description
        this.icon = icon
        this.activity = activity
    }
}
