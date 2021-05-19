package com.riders.thelab.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import timber.log.Timber
import java.util.*

class LabTypefaceManager private constructor() {

    companion object {

        /**
         * Using reflection to override default typeface
         * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
         *
         * @param context                    to work with assets
         * @param defaultFontNameToOverride  for example "monospace"
         * @param customFontFileNameInAssets file name of the font from assets
         */
        @SuppressLint("BinaryOperationInTimber")
        fun overrideFont(context: Context,
                         defaultFontNameToOverride: String,
                         customFontFileNameInAssets: String) {

            val customFontTypeface = Typeface.createFromAsset(context.assets, customFontFileNameInAssets)

            if (LabCompatibilityManager.isLollipop()) {
                val newMap: MutableMap<String, Typeface> = HashMap()
                newMap["serif"] = customFontTypeface
                try {
                    val staticField = Typeface::class.java
                            .getDeclaredField("sSystemFontMap")
                    staticField.isAccessible = true
                    staticField[null] = newMap
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            } else {
                try {
                    val defaultFontTypefaceField = Typeface::class.java.getDeclaredField(defaultFontNameToOverride)
                    defaultFontTypefaceField.isAccessible = true
                    defaultFontTypefaceField[null] = customFontTypeface
                } catch (e: Exception) {
                    Timber.e(
                            "Can not set custom font " + customFontFileNameInAssets
                                    + " instead of " + defaultFontNameToOverride)
                }
            }
        }

    }
}