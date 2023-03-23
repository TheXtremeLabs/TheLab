package com.riders.thelab.core.utils

import android.content.Context
import android.graphics.PorterDuff
import android.view.MenuItem
import androidx.core.content.ContextCompat

object BrowserUtils {
    fun isSameDomain(url: String, url1: String): Boolean {
        return getRootDomainUrl(url.lowercase()) == getRootDomainUrl(url1.lowercase())
    }

    private fun getRootDomainUrl(url: String): String {
        val domainKeys = url.split("/").toTypedArray()[2].split("\\.").toTypedArray()
        val length = domainKeys.size
        val dummy = if (domainKeys[0] == "www") 1 else 0

        return if (length - dummy == 2) domainKeys[length - 2] + "." + domainKeys[length - 1]
        else {
            if (domainKeys[length - 1].length == 2) {
                domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1]
            } else {
                domainKeys[length - 2] + "." + domainKeys[length - 1]
            }
        }
    }

    fun tintMenuIcon(context: Context?, item: MenuItem, color: Int) {
        val drawable = item.icon
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate()
            drawable.setColorFilter(
                ContextCompat.getColor(context!!, color),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    fun bookmarkUrl(context: Context, url: String?) {
        val pref = context.getSharedPreferences("androidhive", 0) // 0 - for private mode
        val editor = pref.edit()

        // if url is already bookmarked, unbookmark it
        editor.putBoolean(url, !pref.getBoolean(url, false))
        editor.commit()
    }

    fun isBookmarked(context: Context, url: String?): Boolean {
        val pref = context.getSharedPreferences("androidhive", 0)
        return pref.getBoolean(url, false)
    }
}
