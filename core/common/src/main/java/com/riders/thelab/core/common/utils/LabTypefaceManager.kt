package com.riders.thelab.core.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface

object LabTypefaceManager {

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     *
     * @param context                    to work with assets
     * @param defaultFontNameToOverride  for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    @SuppressLint("BinaryOperationInTimber")
    fun overrideFont(
        context: Context,
        defaultFontNameToOverride: String,
        customFontFileNameInAssets: String
    ) {

        val customFontTypeface =
            Typeface.createFromAsset(context.assets, customFontFileNameInAssets)

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
    }
}
