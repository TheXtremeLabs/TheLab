package com.riders.thelab.core.ui.utils

import  com.riders.thelab.core.ui.R

object LabColorsManager {

    /**
     * Get list of colors
     */
    fun getDefaultColors(): IntArray = intArrayOf(
        R.color.white,
        R.color.red,
        R.color.blue,
        R.color.green,
        R.color.orange,
        R.color.purple,
        R.color.yellow,
        R.color.teal_700
    )

    /**
     * Get a random color
     */
    fun getRandomColor(excludeColor: Int? = null): Int = getDefaultColors()
        .filterNot { it == excludeColor }
        .random()
}