package com.riders.thelab.core.data.local.model.compose

enum class WindowSizeClass {
    COMPACT, MEDIUM, EXPANDED;

    companion object {
        fun getWindowSizeClassBasedOnWidth(width: Float) = when {
            width < 600f -> COMPACT
            width < 840f -> MEDIUM
            else -> EXPANDED
        }

        fun getWindowSizeClassBasedOnHeight(height: Float) = when {
            height < 480f -> COMPACT
            height < 900f -> MEDIUM
            else -> EXPANDED
        }

        fun getWindowSizeClass(width: Float, height: Float) {
            val widthWindowSizeClass = when {
                width < 600f -> COMPACT
                width < 840f -> MEDIUM
                else -> EXPANDED
            }

            val heightWindowSizeClass = when {
                height < 480f -> COMPACT
                height < 900f -> MEDIUM
                else -> EXPANDED
            }
        }
    }
}