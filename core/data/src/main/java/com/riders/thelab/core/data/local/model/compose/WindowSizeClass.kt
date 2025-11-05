package com.riders.thelab.core.data.local.model.compose

enum class WindowSizeClass {
    COMPACT, MEDIUM, EXPANDED;

    companion object {
        fun getWindowSizeClassBasedOnWidth(width: Float) = when {
            width < 600f -> WindowSizeClass.COMPACT
            width < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        fun getWindowSizeClassBasedOnHeight(height: Float) = when {
            height < 480f -> WindowSizeClass.COMPACT
            height < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        fun getWindowSizeClass(width: Float, height: Float) {
            val widthWindowSizeClass = when {
                width < 600f -> WindowSizeClass.COMPACT
                width < 840f -> WindowSizeClass.MEDIUM
                else -> WindowSizeClass.EXPANDED
            }

            val heightWindowSizeClass = when {
                height < 480f -> WindowSizeClass.COMPACT
                height < 900f -> WindowSizeClass.MEDIUM
                else -> WindowSizeClass.EXPANDED
            }
        }
    }
}