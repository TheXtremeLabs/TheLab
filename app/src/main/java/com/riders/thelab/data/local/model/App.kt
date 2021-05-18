package com.riders.thelab.data.local.model

import android.app.Activity
import android.graphics.drawable.Drawable
import lombok.Getter
import lombok.Setter
import lombok.ToString

@Setter
@Getter
@ToString
data class App constructor(var appName: String? = null, var appDrawableIcon: Drawable? = null, var appVersion: String? = null, var appPackageName: String? = null) {

    // From Packages
    /*private var name: String? = null
    private var drawableIcon: Drawable? = null
    private var version: String? = null
    private var packageName: String? = null*/

    // From activities
    private var title: String? = null
    private var description: String? = null
    private var icon = 0
    private var activity: Class<out Activity?>? = null

    constructor() : this("", null, "", "")

    constructor(title: String, description: String, icon: Int, activity: Class<out Activity>?) : this() {
        this.title = title
        this.description = description
        this.icon = icon
        this.activity = activity
    }
}
