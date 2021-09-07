package com.riders.thelab.data.local.model.app

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class App(
    var id: Long,
    var appName: String, var appDrawableIcon: @RawValue Drawable?,
    var appVersion: String, var appPackageName: String,
    var appTitle: String, var appDescription: String,
    var appActivity: Class<out Activity?>?
) : Parcelable {

    // From Packages
    private var name: String? = null
    private var drawableIcon: Drawable? = null
    private var version: String? = null
    private var packageName: String? = null

    // From activities
    private var title: String? = null
    private var description: String? = null
    private var activity: Class<out Activity?>? = null

    constructor(
        name: String,
        drawableIcon: Drawable,
        version: String,
        packageName: String
    ) : this(
        -1L,
        name, drawableIcon, version, packageName,
        "", "", null
    ) {
        this.name = name
        this.drawableIcon = drawableIcon
        this.version = version
        this.packageName = packageName
    }

    constructor(
        id: Long,
        title: String,
        description: String,
        icon: Drawable,
        activity: Class<out Activity>?
    ) : this(
        id,
        "", icon, "", "",
        title, description, activity
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.drawableIcon = icon
        this.activity = activity
    }

    override fun toString(): String {
        return "App(id=$id, appName='$appName', appDrawableIcon=$appDrawableIcon, appVersion='$appVersion', appPackageName='$appPackageName', appTitle='$appTitle', appDescription='$appDescription', appActivity=$appActivity, name=$name, drawableIcon=$drawableIcon, version=$version, packageName=$packageName, title=$title, description=$description, activity=$activity)"
    }

}
