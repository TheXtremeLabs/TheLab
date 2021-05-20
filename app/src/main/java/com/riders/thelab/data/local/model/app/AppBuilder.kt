package com.riders.thelab.data.local.model.app

import android.app.Activity
import android.graphics.drawable.Drawable
import com.riders.thelab.core.exception.LabApplicationInitializedException

object AppBuilder {

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


    // From Packages
    fun withAppName(appName: String): AppBuilder {
        this.name = appName
        return this

    }

    fun withAppDrawableIcon(appDrawableIcon: Drawable): AppBuilder {
        this.drawableIcon = appDrawableIcon
        return this
    }

    fun withAppVersion(appVersion: String): AppBuilder {
        this.version = appVersion
        return this
    }

    fun withAppPackageName(appPackageName: String): AppBuilder {
        this.packageName = appPackageName
        return this
    }


    // From activities
    fun withActivityTitle(activityTitle: String): AppBuilder {
        this.title = activityTitle
        return this
    }

    fun withActivityDescription(activityDescription: String): AppBuilder {
        this.description = activityDescription
        return this
    }

    fun withActivityIcon(activityIcon: Int): AppBuilder {
        this.icon = activityIcon
        return this
    }

    fun withActivityClass(targetActivity: Class<out Activity>?): AppBuilder {
        this.activity = targetActivity
        return this
    }


    fun build(): App {

        name?.let { appName: String ->
            drawableIcon?.let { appDrawableIcon ->
                version?.let { appVersion ->
                    packageName?.let { appPackageName ->
                        return App(appName, appDrawableIcon, appVersion, appPackageName)
                    }
                }
            }
        }

        title?.let { activityTitle: String ->
            description?.let { activityDescription ->
                activity?.let { targetActivity ->
                    return App(activityTitle, activityDescription, icon, targetActivity)
                }

            }
        }

        if (title == "WIP" && activity == null)
            return App(title!!, description!!, icon, activity)

        throw LabApplicationInitializedException()
    }
}