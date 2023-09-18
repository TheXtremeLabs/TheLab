package com.riders.thelab.core.data.local.model.app

import android.app.Activity
import android.graphics.drawable.Drawable
import com.riders.thelab.core.exception.LabApplicationInitializedException

object AppBuilder {

    private var id: Long = -1L

    // From Packages
    private var name: String? = null
    private var drawableIcon: Drawable? = null
    private var version: String? = null
    private var packageName: String? = null

    // From activities
    private var title: String? = null
    private var description: String? = null
    private var icon: Drawable? = null
    private var activity: Class<out Activity?>? = null
    private var date: String? = null


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
    fun withId(activityID: Long): AppBuilder {
        this.id = activityID
        return this
    }

    fun withActivityTitle(activityTitle: String): AppBuilder {
        this.title = activityTitle
        return this
    }

    fun withActivityDescription(activityDescription: String): AppBuilder {
        this.description = activityDescription
        return this
    }

    fun withActivityIcon(activityIcon: Drawable): AppBuilder {
        this.icon = activityIcon
        return this
    }

    fun withActivityClass(targetActivity: Class<out Activity>?): AppBuilder {
        this.activity = targetActivity
        return this
    }

    fun withActivityDate(activityDate: String): AppBuilder {
        this.date = activityDate
        return this
    }


    @Throws(LabApplicationInitializedException::class)
    fun build(): App {

        name?.let { appName: String ->
            drawableIcon?.let { appDrawableIcon ->
                version?.let { appVersion ->
                    packageName?.let { appPackageName ->
                        return PackageApp(appName, appDrawableIcon, appVersion, appPackageName)
                    }
                }
            }
        }
        id.let {
            title?.let { activityTitle: String ->
                icon?.let { activityIcon ->
                    description?.let { activityDescription ->
                        activity?.let { targetActivity ->
                            date?.let { activityDate ->
                                return LocalApp(
                                    id,
                                    activityTitle,
                                    activityDescription,
                                    activityIcon,
                                    targetActivity,
                                    activityDate
                                )
                            }
                        }
                    }
                }
            }
        }

        if (title == "WIP" && activity == null)
            return LocalApp(id, title!!, description!!, icon!!, null, date!!)

        throw LabApplicationInitializedException()
    }
}